package cn.epicfx.winfxk.mostbrain.cmd;

import java.io.File;
import java.io.FilenameFilter;

import cn.epicfx.winfxk.mostbrain.Activate;
import cn.epicfx.winfxk.mostbrain.Message;
import cn.epicfx.winfxk.mostbrain.MyPlayer;
import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.level.Level;

/**
 * @author Winfxk
 */
public class PCommand extends Command implements FilenameFilter {
	private Activate ac;
	public static final String Permission = "MostBrain.Command.main";
	private static final String MainKey = "Command", SunKey = "PlayerCommand";
	private Message msg;

	public PCommand(Activate ac) {
		super(ac.getName(), ac.getName() + "命令", "/" + ac.getName() + " help", ac.getCommands("PlayerCommand"));
		this.ac = ac;
		msg = ac.getMessage();
		commandParameters.clear();
		commandParameters.put(getMessage("QuitGame"), new CommandParameter[] {
				new CommandParameter(getMessage("QuitGame"), false, new String[] { "quit", "退出" }) });
		commandParameters.put(getMessage("JoinGame"), new CommandParameter[] {
				new CommandParameter(getMessage("JoinGame"), false, new String[] { "join", "加入" }) });
		commandParameters.put(getMessage("TraGame"), new CommandParameter[] {
				new CommandParameter(getMessage("TraGame"), false, new String[] { "tra", "传送", "transfer" }) });
		commandParameters.put(getMessage("ReferScore"), new CommandParameter[] {
				new CommandParameter(getMessage("ReferScore"), false, new String[] { "sco", "查询", "score" }) });
		commandParameters.put(getMessage("List"), new CommandParameter[] {
				new CommandParameter(getMessage("List"), false, new String[] { "list", "列表" }) });
		commandParameters.put(getMessage("TopMsg"), new CommandParameter[] {
				new CommandParameter(getMessage("TopMsg"), false, new String[] { "tm", "top", "排行", "排行榜" }) });
		commandParameters.put(getMessage("TopDMsg"), new CommandParameter[] {
				new CommandParameter(getMessage("TopDMsg"), false, new String[] { "td", "topd", "死亡排行", "死亡排行榜" }) });
		commandParameters.put(getMessage("TopSMsg"), new CommandParameter[] {
				new CommandParameter(getMessage("TopSMsg"), false, new String[] { "ts", "tops", "得分排行", "得分排行榜" }) });
		commandParameters.put(getMessage("TopHMsg"), new CommandParameter[] {
				new CommandParameter(getMessage("TopHMsg"), false, new String[] { "th", "toph", "荣耀排行", "荣耀排行榜" }) });

	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!ac.getMostBrain().isEnabled())
			return true;
		if (isHelp(args)) {
			sender.sendMessage(Tool.getCommandHelp(this));
			return true;
		}
		String s = "";
		switch (args[0].toLowerCase()) {
		case "th":
		case "toph":
		case "荣耀排行":
		case "荣耀排行榜":
			s = ac.getTopH();
			if (s == null || s.isEmpty()) {
				sender.sendMessage(getMessage("暂无玩家数据", sender));
				return true;
			}
			sender.sendMessage(s);
			return true;
		case "ts":
		case "tops":
		case "得分排行":
		case "得分排行榜":
			s = ac.getTopS();
			if (s == null || s.isEmpty()) {
				sender.sendMessage(getMessage("暂无玩家数据", sender));
				return true;
			}
			sender.sendMessage(s);
			return true;
		case "td":
		case "topd":
		case "死亡排行":
		case "死亡排行榜":
			s = ac.getTopD();
			if (s == null || s.isEmpty()) {
				sender.sendMessage(getMessage("暂无玩家数据", sender));
				return true;
			}
			sender.sendMessage(s);
			return true;
		case "t":
		case "top":
		case "tm":
		case "排行":
		case "排行榜":
			s = ac.getTop();
			if (s == null || s.isEmpty()) {
				sender.sendMessage(getMessage("暂无玩家数据", sender));
				return true;
			}
			sender.sendMessage(s);
			return true;
		}
		if (!sender.isPlayer()) {
			sender.sendMessage(msg.getMessage("请在游戏内自行命令"));
			return true;
		}
		if (!sender.hasPermission(Permission)) {
			sender.sendMessage(msg.getMessage("权限不足", (Player) sender));
			return true;
		}
		Player player = (Player) sender;
		Level level;
		MyPlayer myPlayer = ac.getPlayers(player.getName());
		switch (args[0].toLowerCase()) {
		case "l":
		case "list":
		case "列表":
			player.sendMessage(getMessage("OpenList", player));
			return ac.makeForm.MakeMain(player);
		case "sco":
		case "查询":
		case "score":
		case "s":
			player.sendMessage(msg.getSun("Command", "PlayerCommand", "您的游戏数据",
					new String[] { "{Player}", "{Money}", "{Score}", "{Honor}" }, new Object[] { player.getName(),
							myPlayer.getMoney(), myPlayer.getConfig().get("得分"), myPlayer.getConfig().get("荣耀") }));
			break;
		case "tra":
		case "传送":
		case "transfer":
		case "t":
			if (!ac.isGameSettingUp) {
				player.sendMessage(msg.getMessage("游戏未设置", player));
				return true;
			}
			level = Server.getInstance().getLevelByName(ac.getMostConfig().Level);
			if (level == null) {
				player.sendMessage(getMessage("无法传送", player));
				return true;
			}
			player.teleport(ac.getMostConfig().getStart());
			player.sendMessage(getMessage("传送成功", player));
			break;
		case "join":
		case "进入":
		case "j":
			if (!ac.isGameSettingUp) {
				player.sendMessage(msg.getMessage("游戏未设置", player));
				return true;
			}
			level = Server.getInstance().getLevelByName(ac.getMostConfig().Level);
			if (level == null) {
				player.sendMessage(getMessage("无法加入游戏", player));
				return true;
			}
			Block block = level.getBlock(ac.getMostConfig().getStart());
			ac.gameEvent.Start(new PlayerInteractEvent(player, player.getInventory().getItemInHand(), block, null));
			break;
		case "退出":
		case "quit":
		case "q":
			if (!ac.isGameSettingUp) {
				player.sendMessage(msg.getMessage("游戏未设置", player));
				return true;
			}
			if (!ac.isStartGame || (!myPlayer.GameModel && !myPlayer.ReadyModel)) {
				player.sendMessage(getMessage("未加入游戏", player));
				return true;
			}
			ac.gameHandle.QuitGame(player, true, true, true, true);
			player.sendMessage(getMessage("中途退出", player));
			break;
		default:
			player.sendMessage(Tool.getCommandHelp(this));
		}
		return true;
	}

	private String getMessage(String key, CommandSender sender) {
		return getMessage(key, sender.isPlayer() ? (Player) sender : null);
	}

	public String getMessage(String Key) {
		return msg.getSun(MainKey, SunKey, Key);
	}

	public String getMessage(String Key, Player player) {
		return msg.getSun(MainKey, SunKey, Key, player);
	}

	@Override
	public boolean accept(File arg0, String arg1) {
		return new File(arg0, arg1).isFile();
	}

	private boolean isHelp(String[] s) {
		if (s == null || s.length <= 0 || s[0] == null || s[0].isEmpty())
			return true;
		switch (s[0].toLowerCase()) {
		case "h":
		case "help":
		case "帮助":
			return true;
		}
		return false;
	}
}
