package cn.epicfx.winfxk.mostbrain.cmd;

import java.util.LinkedHashMap;

import cn.epicfx.winfxk.mostbrain.Activate;
import cn.epicfx.winfxk.mostbrain.Message;
import cn.epicfx.winfxk.mostbrain.MyPlayer;
import cn.epicfx.winfxk.mostbrain.game.MostConfig;
import cn.epicfx.winfxk.mostbrain.game.SettingGame;
import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Config;

/**
 * @author Winfxk
 */
public class ACommand extends Command {
	private Activate ac;
	private Message msg;
	private static final String Name_ = "Admin";
	public static final String Permission = "MostBrain.Command.Admin";
	private static final String MainKey = "Command", SunKey = "AdminCommand";

	public ACommand(Activate brain) {
		super(Name_ + brain.getName(), brain.getName() + "管理员命令", "/" + Name_ + brain.getName() + " help",
				brain.getCommands("AdminCommand"));
		this.ac = brain;
		msg = ac.getMessage();
		this.commandParameters.clear();
		commandParameters.put(getMessage("setMsg"), new CommandParameter[] {
				new CommandParameter(getMessage("setMsg"), false, new String[] { "set", "设置" }) });
		commandParameters.put(getMessage("delMsg"), new CommandParameter[] {
				new CommandParameter(getMessage("delMsg"), false, new String[] { "del", "删除" }) });
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!ac.getMostBrain().isEnabled())
			return true;
		if (!sender.hasPermission(Permission)) {
			if (sender.isPlayer())
				sender.sendMessage(msg.getMessage("权限不足", (Player) sender));
			else
				sender.sendMessage(msg.getMessage("权限不足"));
			return true;
		}
		if (!sender.isPlayer()) {
			sender.sendMessage(msg.getMessage("请在游戏内自行命令"));
			return true;
		}
		if (args == null || args.length == 0)
			return false;
		LinkedHashMap<String, Object> map;
		Player player;
		switch (args[0].toLowerCase()) {
		case "del":
		case "remove":
		case "删除":
			if (ac.isStartGame) {
				if (sender.isPlayer())
					sender.sendMessage(getMessage("已开始游戏", (Player) sender));
				else
					sender.sendMessage(getMessage("已开始游戏"));
				return true;
			}
			if (!ac.isGameSettingUp && ac.SettingModel) {
				if (sender.getName().equals(ac.setPlayer.getName())) {
					ac.SettingModel = false;
					ac.setPlayer.setGamemode(ac.settingGame.gameMode);
					ac.setPlayer.getInventory().setItem(0, ac.settingGame.sbItem);
					if (ac.settingGame.start != null)
						ac.settingGame.start.getLevel().setBlock(ac.settingGame.start.getLocation(),
								ac.settingGame.start);
					ac.settingGame = null;
					ac.setPlayer.sendMessage(getMessage("停止设置", ac.setPlayer));
					ac.getMostBrain().getLogger().info(getMessage("停止设置", ac.setPlayer));
					ac.setPlayer = null;
					return true;
				}
				return true;
			} else if (!ac.isGameSettingUp && !ac.SettingModel) {
				sender.sendMessage(ac.getMessage().getMessage("游戏未设置"));
				return true;
			}
			Config config = ac.getGameConfig();
			map = new LinkedHashMap<>();
			map.put("Remove", sender.getName());
			new Thread() {
				@Override
				public void run() {
					MostConfig config2 = ac.getMostConfig();
					if (config2 != null) {
						String SignLevelName = config2.StartLevel;
						String StartLevelName = config2.Level;
						Level signLevel = Server.getInstance().getLevelByName(SignLevelName);
						Level startLevel = Server.getInstance().getLevelByName(StartLevelName);
						if (startLevel != null) {
							for (double i = config2.MinX; i < config2.MaxX + 1; i++)
								for (double j = config2.MinY; j < config2.MaxY + 1; j++)
									for (double k = config2.MinZ; k < config2.MaxZ + 1; k++)
										startLevel.setBlock(new Vector3(i, j, k), Block.get(0, 0));
						}
						if (signLevel != null)
							Tool.setSign(signLevel.getBlock(config2.getStart()), " ");
					}
				}
			}.start();
			config.setAll(map);
			config.save();
			ac.setGameConfig(config);
			ac.isGameSettingUp = false;
			sender.sendMessage(getMessage("删除游戏"));
			if (sender.isPlayer())
				ac.getMostBrain().getLogger().info(getMessage("管理员删除游戏"));
			break;
		case "set":
		case "设置":
			player = (Player) sender;
			if (ac.isGameSettingUp) {
				player.sendMessage(msg.getSun(MainKey, SunKey, "游戏已设置完毕", player));
				return true;
			}
			if (ac.SettingModel) {
				player.sendMessage(ac.setPlayer.getName().equals(player.getName())
						? msg.getSun(MainKey, SunKey, "正在设置", player)
						: msg.getSun(MainKey, SunKey, "已有玩家正在设置", new String[] { "{Player}", "{Money}", "{ByPlayer}" },
								new Object[] { player.getName(), ac.getPlayers(player.getName()).getMoney(),
										ac.setPlayer.getName() }));
				return true;
			}
			ac.SettingModel = true;
			ac.setPlayer = player;
			MyPlayer myPlayer = ac.getPlayers(player.getName());
			ac.settingGame = new SettingGame(ac, player);
			ac.settingGame.sbItem = player.getInventory().getItem(0);
			ac.settingGame.gameMode = player.getGamemode();
			player.setGamemode(1);
			myPlayer.SettingModel = true;
			ac.setPlayers(player, myPlayer);
			int[] ID = Tool.IDtoFullID(ac.getConfig().get("快捷工具"));
			Item item = new Item(ID[0], ID[1], 1);
			item.setCustomName(getMessage("Tool", player));
			item.addEnchantment(Enchantment.get(28));
			CompoundTag tag = item.getCustomBlockData() == null ? new CompoundTag() : item.getCustomBlockData();
			tag.putBoolean(ac.getMostBrain().getName(), true);
			item.setCustomBlockData(tag);
			player.getInventory().setItem(0, item);
			player.sendMessage(msg.getSun(MainKey, SunKey, "StartGame", player));
			break;
		}
		return true;
	}

	public String getMessage(String Key) {
		return msg.getSun(MainKey, SunKey, Key);
	}

	public String getMessage(String Key, Player player) {
		return msg.getSun(MainKey, SunKey, Key, player);
	}
}
