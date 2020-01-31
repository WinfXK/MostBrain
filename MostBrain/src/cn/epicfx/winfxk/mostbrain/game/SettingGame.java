package cn.epicfx.winfxk.mostbrain.game;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.epicfx.winfxk.mostbrain.Activate;
import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.Player;
import cn.nukkit.block.Block;
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
				List<?> list = (List<?>) GameMessage.get("NotStartSign");
				player.sendMessage(getMessage("点击木牌"));
				Tool.setSign(startSign = block, ac.getMessage().getText(Tool.objToString(list.get(0), ""), player),
						ac.getMessage().getText(Tool.objToString(list.get(1), ""), player),
						ac.getMessage().getText(Tool.objToString(list.get(2), ""), player),
						ac.getMessage().getText(Tool.objToString(list.get(3), ""), player));
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
				double x1 = (start.getX() > end.getX() ? start.getX() : end.getX());
				double x2 = (start.getX() < end.getX() ? start.getX() : end.getX());
				double y1 = (start.getY() > end.getY() ? start.getY() : end.getY());
				double y2 = (start.getY() < end.getY() ? start.getY() : end.getY());
				double z1 = (start.getZ() > end.getZ() ? start.getZ() : end.getZ());
				double z2 = (start.getZ() < end.getZ() ? start.getZ() : end.getZ());
				resX = x2 + ((x1 - x2) / 2);
				resY = y2 + 1;
				resZ = z2 + ((z1 - z2) / 2);
				Level level = start.getLevel();
				String SignLevel = startSign.getLevel().getFolderName();
				String StartLevel = start.getLevel().getFolderName();
				for (double i = x2; i < x1; i++)
					for (double j = y2; j < y1; j++)
						for (double k = z2; k < z1; k++)
							if (startSign.x != i || startSign.y != j || startSign.z != k
							|| !SignLevel.equals(StartLevel))
								level.setBlock(new Vector3(i, j, k), Block.get(0, 0));
				if (ac.getConfig().getBoolean("生成外壳")) {
					int sb = 0;
					Vector3 v1;
					for (double i = x2; i < x1 + 1; i++) {
						sb++;
						for (double j = y2; j < y1 + 1; j++) {
							v1 = new Vector3(i, j, z2);
							if (v1.x != startSign.x || v1.z != startSign.z || v1.y != startSign.y
									|| !SignLevel.equals(StartLevel))
								level.setBlock(v1,
										sb % 6 == 0 ? Block.get(169, 0) : Block.get(35, sb % 2 == 0 ? 0 : 15));
							v1 = new Vector3(i, j, z1);
							if (v1.x != startSign.x || v1.z != startSign.z || v1.y != startSign.y
									|| !SignLevel.equals(StartLevel))
								level.setBlock(new Vector3(i, j, z1),
										sb % 6 == 0 ? Block.get(169, 0) : Block.get(35, sb % 2 == 0 ? 0 : 15));
						}
					}
					sb = 0;
					for (double k = z2; k < z1 + 1; k++) {
						sb++;
						for (double i = x2; i < x1 + 1; i++) {
							sb++;
							v1 = new Vector3(i, y1, k);
							if (v1.x != startSign.x || v1.z != startSign.z || v1.y != startSign.y
									|| !SignLevel.equals(StartLevel))
								level.setBlock(v1,
										sb % 6 == 0 ? Block.get(169, 0) : Block.get(35, sb % 2 == 0 ? 0 : 15));
							v1 = new Vector3(i, y2, k);
							if (v1.x != startSign.x || v1.z != startSign.z || v1.y != startSign.y
									|| !SignLevel.equals(StartLevel))
								level.setBlock(v1,
										sb % 6 == 0 ? Block.get(169, 0) : Block.get(35, sb % 2 == 0 ? 0 : 15));
						}
					}
					sb = 0;
					for (double j = y2; j < y1 + 1; j++) {
						sb++;
						for (double k = z2; k < z1 + 1; k++) {
							sb++;
							v1 = new Vector3(x1, j, k);
							if (v1.x != startSign.x || v1.z != startSign.z || v1.y != startSign.y
									|| !SignLevel.equals(StartLevel))
								level.setBlock(v1,
										sb % 6 == 0 ? Block.get(169, 0) : Block.get(35, sb % 2 == 0 ? 0 : 15));
							v1 = new Vector3(x2, j, k);
							if (v1.x != startSign.x || v1.z != startSign.z || v1.y != startSign.y
									|| !SignLevel.equals(StartLevel))
								level.setBlock(v1,
										sb % 6 == 0 ? Block.get(169, 0) : Block.get(35, sb % 2 == 0 ? 0 : 15));
						}
					}
				}
				ac.SettingModel = false;
				ac.setPlayer = null;
				lastSet = false;
				Config config = ac.getGameConfig();
				config.setAll(new LinkedHashMap<String, Object>());
				config.set("GameSettingUp", true);
				config.set("Creator", player.getName());
				config.set("StartLevel", SignLevel);
				config.set("Level", StartLevel);
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
				map = new HashMap<>();
				map.put("X", x1);
				map.put("Y", y1);
				map.put("Z", z1);
				config.set("MaxLocation", map);
				map = new HashMap<>();
				map.put("X", x2);
				map.put("Y", y2);
				map.put("Z", z2);
				config.set("MinLocation", map);
				config.save();
				ac.setGameConfig(config);
				ac.isGameSettingUp = true;
				player.getInventory().setItem(0, sbItem);
				player.setGamemode(gameMode);
				start.getLevel().setBlock(start.getLocation(), start);
				ac.reloadMostConfig(true);
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
