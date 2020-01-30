package cn.epicfx.winfxk.mostbrain.cmd;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import cn.nukkit.utils.Utils;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

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
		commandParameters.put(getMessage("stopMsg"), new CommandParameter[] {
				new CommandParameter(getMessage("stopMsg"), false, new String[] { "stop", "停止" }) });
		commandParameters.put(getMessage("setLang"), new CommandParameter[] {
				new CommandParameter(getMessage("setLang"), false, new String[] { "lang", "语言", "language" }) });
		commandParameters.put(getMessage("Langs"), new CommandParameter[] { new CommandParameter(getMessage("Langs"),
				false, new String[] { "langs", "语言列表", "languages", "ll" }) });
		commandParameters.put(getMessage("reloadMsg"), new CommandParameter[] {
				new CommandParameter(getMessage("reloadMsg"), false, new String[] { "reload", "重载" }) });
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!ac.getMostBrain().isEnabled())
			return true;
		if (!sender.hasPermission(Permission)) {
			sender.sendMessage(msg.getMessage("权限不足", (Player) sender));
			return true;
		}
		if (args == null || args.length == 0) {
			sender.sendMessage(Tool.getCommandHelp(this));
			return true;
		}
		File file;
		switch (args[0].toLowerCase()) {
		case "reload":
		case "重载":
			ac.reloadMostConfig(false);
			sender.sendMessage(getMessage("reloadOK", sender.isPlayer() ? (Player) sender : null));
			if (sender.isPlayer())
				ac.getMostBrain().getLogger().warning(getMessage("管理员重载", (Player) sender));
			break;
		case "lang":
		case "语言":
		case "language":
			if (args.length < 2) {
				sender.sendMessage(getMessage("请输入想要设置的语言", sender.isPlayer() ? (Player) sender : null));
				return true;
			}
			String l;
			file = new File(ac.getMostBrain().getDataFolder(), Activate.LanguageDirName);
			String[] sk = { "{Player}", "{Money}", "{Error}" };
			List<String> Files = Arrays.asList(file.list());
			if (Tool.isInteger(args[1])) {
				int Is = Tool.ObjectToInt(args[1]);
				if (Is >= Files.size()) {
					sender.sendMessage(msg.getSun("Command", "AdminCommand", "请输入正确的语言Key", sk,
							new Object[] { sender.getName(),
									sender.isPlayer() ? MyPlayer.getMoney(sender.getName()) : 0,
									"0-" + (ac.langs.size() - 1) + "或" + Arrays.asList(Files) }));
					return true;
				}
				l = Files.get(Is);
			} else {
				if (!Files.contains(args[1]) && !Files.contains(args[1] + ".yml")) {
					sender.sendMessage(msg.getSun("Command", "AdminCommand", "请输入正确的语言Key", sk,
							new Object[] { sender.getName(),
									sender.isPlayer() ? MyPlayer.getMoney(sender.getName()) : 0,
									"0-" + (Files.size() - 1) + "或" + Files }));
					return true;
				}
				l = Files.contains(args[1]) ? args[1] : args[1] + ".yml";
			}
			try {
				Utils.writeFile(Message.getFile(), Utils.readFile(new File(file, l)));
				msg.reload();
				sender.sendMessage(msg.getSun("Command", "AdminCommand", "语言设置成功",
						new String[] { "{Player}", "{Money}", "{Language}" },
						new Object[] { sender.getName(), sender.isPlayer() ? MyPlayer.getMoney(sender.getName()) : 0,
								msg.getConfig().getString("lang") }));
			} catch (IOException e) {
				e.printStackTrace();
				sender.sendMessage(getMessage("语言设置失败"));
			}
			return true;
		case "langs":
		case "语言列表":
		case "languages":
		case "ll":
			if (ac.langs.size() < 1) {
				sender.sendMessage(msg.getMessage("暂无已支持的语言", sender.isPlayer() ? (Player) sender : null));
				return true;
			}
			String s = "", t;
			String[] ss;
			sender.sendMessage(getMessage("当前已支持以下语言", sender.isPlayer() ? (Player) sender : null));
			Map<String, Object> map;
			DumperOptions dumperOptions = new DumperOptions();
			dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
			Yaml yaml = new Yaml(dumperOptions);
			file = new File(ac.getMostBrain().getDataFolder(), Activate.LanguageDirName);
			for (String string : file.list())
				if (string != null && string.contains(".")) {
					ss = string.split("\\.");
					s = "";
					for (int i = 0; i < ss.length - 1; i++)
						s += (s.isEmpty() ? "" : ".") + ss[i];
					try {
						t = Utils.readFile(new File(file, string));
						map = yaml.loadAs(t, Map.class);
						sender.sendMessage(Tool.getRandColor() + s + Tool.getRandColor() + ": "
								+ ac.getMessage().getText(map.get("lang")));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			return true;
		case "stop":
		case "停止":
		case "终止":
			if (!ac.isGameSettingUp) {
				sender.sendMessage(msg.getMessage("游戏未设置"));
				return true;
			}
			if (!ac.isStartGame || (!ac.gameHandle.ReadyisModel() && !ac.gameHandle.StartGame())) {
				sender.sendMessage(getMessage("游戏未开始"));
				return true;
			}
			ac.gameHandle.setCommandSender(sender).setAdminStopGame(true);
			sender.sendMessage(getMessage("终止成功"));
			return true;
		}
		if (!sender.isPlayer()) {
			sender.sendMessage(msg.getMessage("请在游戏内自行命令"));
			return true;
		}
		LinkedHashMap<String, Object> map;
		Player player = (Player) sender;
		switch (args[0].toLowerCase()) {
		case "del":
		case "remove":
		case "删除":
			if (ac.isStartGame) {
				sender.sendMessage(getMessage("已开始游戏", player));
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
				sender.sendMessage(ac.getMessage().getMessage("游戏未设置", player));
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
						if (startLevel != null)
							for (double i = config2.MinX; i < config2.MaxX + 1; i++)
								for (double j = config2.MinY; j < config2.MaxY + 1; j++)
									for (double k = config2.MinZ; k < config2.MaxZ + 1; k++)
										startLevel.setBlock(new Vector3(i, j, k), Block.get(0, 0));
						if (signLevel != null)
							Tool.setSign(signLevel.getBlock(config2.getStart()), " ");
					}
				}
			}.start();
			config.setAll(map);
			config.save();
			ac.setGameConfig(config);
			ac.isGameSettingUp = false;
			sender.sendMessage(getMessage("删除游戏", player));
			if (sender.isPlayer())
				ac.getMostBrain().getLogger().info(getMessage("管理员删除游戏", player));
			break;
		case "set":
		case "设置":
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
		default:
			player.sendMessage(Tool.getCommandHelp(this));
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
