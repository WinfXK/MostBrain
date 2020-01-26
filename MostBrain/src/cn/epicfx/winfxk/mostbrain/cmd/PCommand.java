package cn.epicfx.winfxk.mostbrain.cmd;

import cn.epicfx.winfxk.mostbrain.Activate;
import cn.epicfx.winfxk.mostbrain.Message;
import cn.epicfx.winfxk.mostbrain.MyPlayer;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.level.Level;

/**
 * @author Winfxk
 */
public class PCommand extends Command {
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

	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!ac.getMostBrain().isEnabled())
			return true;
		if (!sender.isPlayer()) {
			sender.sendMessage(msg.getMessage("请在游戏内自行命令"));
			return true;
		}
		if (!sender.hasPermission(Permission)) {
			sender.sendMessage(msg.getMessage("权限不足", (Player) sender));
			return true;
		}
		Player player = (Player) sender;
		if (args.length < 1)
			return false;
		Level level;
		MyPlayer myPlayer = ac.getPlayers(player.getName());
		switch (args[0].toLowerCase()) {
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
			ac.gameEvent.QuitGame(new PlayerQuitEvent(player, ""));
			player.sendMessage(getMessage("中途退出", player));
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
