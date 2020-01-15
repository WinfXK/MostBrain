package cn.epicfx.winfxk.mostbrain.cmd;

import cn.epicfx.winfxk.mostbrain.Activate;
import cn.epicfx.winfxk.mostbrain.Message;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;

/**
 * @author Winfxk
 */
public class ACommand extends Command {
	private Activate ac;
	private static final String Name_ = "Admin";
	public static final String Permission = "MostBrain.Command.Admin";
	private Message msg;

	public ACommand(Activate brain) {
		super(Name_ + brain.getName(), brain.getName() + "管理员命令", "/" + Name_ + brain.getName() + " help",
				brain.getCommands("AdminCommand"));
		this.ac = brain;
		msg = ac.getMessage();
		this.commandParameters.clear();
		commandParameters.put(getMessage("set"), new CommandParameter[] {
				new CommandParameter(getMessage("set"), false, new String[] { "set", "设置" }) });
		commandParameters.put(getMessage("del"), new CommandParameter[] {
				new CommandParameter(getMessage("del"), false, new String[] { "del", "删除" }) });
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!ac.getMostBrain().isEnabled())
			return true;
		
		return true;
	}

	public String getMessage(String Key) {
		return msg.getSun("Command", "PlayerCommand", Key);
	}
}
