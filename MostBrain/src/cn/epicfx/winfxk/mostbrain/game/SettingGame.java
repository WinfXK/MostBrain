package cn.epicfx.winfxk.mostbrain.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.epicfx.winfxk.mostbrain.Activate;
import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntitySign;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Config;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class SettingGame {
	private Activate ac;
	private Player player;
	/**
	 * 木牌
	 */
	private Block startSign;
	/**
	 * 场地的开始和结尾
	 */
	public Block start, end;
	private Map<String, Object> GameMessage;
	private boolean lastSet = false;
	private List<String> NotBreakBlocks;
	private double resX, resY, resZ;
	public Item sbItem;
	public int gameMode;

	public SettingGame(Activate ac, Player player) {
		this.ac = ac;
		this.player = player;
		GameMessage = (Map<String, Object>) ac.getMessage().getConfig().get("Game");
	}

	public Block getStartSign() {
		return startSign;
	}

	public void Click(BlockBreakEvent e) {
		Click(new PlayerInteractEvent(e.getPlayer(), e.getItem(), e.getBlock(), e.getFace()));
	}

	public void Click(PlayerInteractEvent e) {
		Item item = e.getItem();
		if (!isItem(item))
			return;
		if (lastSet) {
			player.sendMessage(getMessage("正在生成游戏区域"));
			return;
		}
		Block block = e.getBlock();
		if (startSign == null) {
			if (isItemID(block, 63) || isItemID(block, 323) || isItemID(block, 68)) {
				startSign = block;
				Level level = block.getLevel();
				BlockEntity blockEntity = level.getBlockEntity(block);
				BlockEntitySign sign = (blockEntity instanceof BlockEntitySign) ? (BlockEntitySign) blockEntity
						: new BlockEntitySign(block.getLevel().getChunk(block.getFloorX() >> 4, block.getFloorZ() >> 4),
								BlockEntity.getDefaultCompound(block, BlockEntity.SIGN));
				List<?> list = (List<?>) GameMessage.get("NotStartSign");
				sign.setText(ac.getMessage().getText(Tool.objToString(list.get(0), ""), player),
						ac.getMessage().getText(Tool.objToString(list.get(1), ""), player),
						ac.getMessage().getText(Tool.objToString(list.get(2), ""), player),
						ac.getMessage().getText(Tool.objToString(list.get(3), ""), player));
				player.sendMessage(getMessage("点击木牌"));
				return;
			}
			return;
		}
		if (start == null) {
			start = block;
			player.sendMessage(getMessage("点击第一个角"));
			block.getLevel().setBlock(new Vector3(block.getX(), block.getY(), block.getZ()), Block.get(41, 0));
			return;
		}
		if (end == null) {
			if (start.getLevel().equals(block.getLevel())) {
				if (Math.abs(start.getX() - block.getX()) < 5 || Math.abs(start.getY() - block.getY()) < 5
						|| Math.abs(start.getZ() - block.getZ()) < 3) {
					player.sendMessage(getMessage("游戏区域过小"));
					return;
				}
				lastSet = true;
				end = block;
				player.sendMessage(getMessage("设置完成"));
				start();
				return;
			}
			player.sendMessage(getMessage("不同世界设置区域"));
			return;
		}
		player.sendMessage(getMessage("设置错误"));
	}

	private void start() {
		new Thread() {
			@Override
			public void run() {
				int x1 = (int) (start.getX() > end.getX() ? start.getX() : end.getX());
				int x2 = (int) (start.getX() < end.getX() ? start.getX() : end.getX());
				int y1 = (int) (start.getY() > end.getY() ? start.getY() : end.getY());
				int y2 = (int) (start.getY() < end.getY() ? start.getY() : end.getY());
				int z1 = (int) (start.getZ() > end.getZ() ? start.getZ() : end.getZ());
				int z2 = (int) (start.getZ() < end.getZ() ? start.getZ() : end.getZ());
				resX = x2 + ((x1 - x2) / 2);
				resY = y2 + 1;
				resZ = z2 + ((z1 - z2) / 2);
				Level level = start.getLevel();
				for (int i = x2; i < x1; i++)
					for (int j = y2; j < y1; j++)
						for (int k = z2; k < z1; k++)
							if ((int) startSign.getX() != i || (int) startSign.y != j || (int) startSign.z != k)
								level.setBlock(new Vector3(i, j, k), Block.get(0, 0));
				NotBreakBlocks = new ArrayList<>();
				int sb = 0;
				Vector3 v1;
				for (int i = x2; i < x1 + 1; i++)
					for (int j = y2; j < y1 + 1; j++) {
						sb++;
						v1 = new Vector3(i, j, z2);
						if ((int) v1.x != (int) startSign.x || (int) v1.z != (int) startSign.z
								|| (int) v1.y != (int) startSign.y) {
							NotBreakBlocks.add("Le." + level.getFolderName() + " x." + i + " y." + j + " z." + z1);
							level.setBlock(v1, sb % 6 == 0 ? Block.get(169, 0) : Block.get(35, sb % 2 == 0 ? 0 : 15));
						}
						v1 = new Vector3(i, j, z1);
						if ((int) v1.x != (int) startSign.x || (int) v1.z != (int) startSign.z
								|| (int) v1.y != (int) startSign.y) {
							level.setBlock(new Vector3(i, j, z1),
									sb % 6 == 0 ? Block.get(169, 0) : Block.get(35, sb % 2 == 0 ? 0 : 15));
							NotBreakBlocks.add("Le." + level.getFolderName() + " x." + i + " y." + j + " z." + z2);
						}
					}
				for (int k = z2; k < z1 + 1; k++)
					for (int i = x2; i < x1 + 1; i++) {
						sb++;
						v1 = new Vector3(i, y1, k);
						if ((int) v1.x != (int) startSign.x || (int) v1.z != (int) startSign.z
								|| (int) v1.y != (int) startSign.y) {
							NotBreakBlocks.add("Le." + level.getFolderName() + " x." + i + " y." + y1 + " z." + k);
							level.setBlock(v1, sb % 6 == 0 ? Block.get(169, 0) : Block.get(35, sb % 2 == 0 ? 0 : 15));
						}
						v1 = new Vector3(i, y2, k);
						if ((int) v1.x != (int) startSign.x || (int) v1.z != (int) startSign.z
								|| (int) v1.y != (int) startSign.y) {
							level.setBlock(v1, sb % 6 == 0 ? Block.get(169, 0) : Block.get(35, sb % 2 == 0 ? 0 : 15));
							NotBreakBlocks.add("Le." + level.getFolderName() + " x." + i + " y." + y2 + " z." + k);
						}
					}
				for (int j = y2; j < y1 + 1; j++)
					for (int k = z2; k < z1 + 1; k++) {
						sb++;
						v1 = new Vector3(x1, j, k);
						if ((int) v1.x != (int) startSign.x || (int) v1.z != (int) startSign.z
								|| (int) v1.y != (int) startSign.y) {
							NotBreakBlocks.add("Le." + level.getFolderName() + " x." + x1 + " y." + j + " z." + k);
							level.setBlock(v1, sb % 6 == 0 ? Block.get(169, 0) : Block.get(35, sb % 2 == 0 ? 0 : 15));
						}
						v1 = new Vector3(x2, j, k);
						if ((int) v1.x != (int) startSign.x || (int) v1.z != (int) startSign.z
								|| (int) v1.y != (int) startSign.y) {
							level.setBlock(v1, sb % 6 == 0 ? Block.get(169, 0) : Block.get(35, sb % 2 == 0 ? 0 : 15));
							NotBreakBlocks.add("Le." + level.getFolderName() + " x." + x2 + " y." + j + " z." + k);
						}
					}
				ac.SettingModel = false;
				ac.setPlayer = null;
				lastSet = false;
				Config config = ac.getGameConfig();
				NotBreakBlocks.add("Le." + level.getFolderName() + " x." + (int) startSign.getX() + " y."
						+ (int) startSign.getY() + " z." + (int) startSign.getZ());
				if (config.exists("Remove"))
					config.remove("Remove");
				config.set("GameSettingUp", true);
				config.set("Creator", player.getName());
				config.set("CreatorTime", Tool.getDate() + " " + Tool.getTime());
				Map<String, Double> map = new HashMap<>();
				map.put("X", startSign.getX());
				map.put("Y", startSign.getY());
				map.put("Z", startSign.getZ());
				config.set("Start", map);
				map = new HashMap<>();
				map.put("X", resX);
				map.put("Y", resY);
				map.put("Z", resZ);
				config.set("Spawn", map);
				config.set("Played", new HashMap<String, Integer>());
				config.set("PlayingGame", new ArrayList<Player>());
				config.set("NotBreakBlocks", NotBreakBlocks);
				config.save();
				ac.setGameConfig(config);
				ac.isGameSettingUp = true;
				player.getInventory().setItem(0, sbItem);
				player.setGamemode(gameMode);
				start.getLevel().setBlock(start.getLocation(), start);
				ac.reloadMostConfig();
				ac.settingGame = null;
				ac.getMostBrain().getServer().broadcastMessage(getMessage("生成完成"));
				super.run();
			}
		}.start();
	}

	private boolean isItem(Item item) {
		return (item.getCustomBlockData() == null ? new CompoundTag() : item.getCustomBlockData())
				.getBoolean(ac.getMostBrain().getName());
	}

	private boolean isItemID(Block item, int ID) {
		return item.getId() == ID;
	}

	public String getMessage(String Key) {
		return ac.getMessage().getSon("SettingGame", Key, player);
	}
}
