package cn.epicfx.winfxk.mostbrain.cmd;

import cn.epicfx.winfxk.mostbrain.Activate;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

/**
 * @author Winfxk
 */
public class PCommand extends Command {
	private Activate ac;
	public static final String Permission = "MostBrain.Command.main";

	public PCommand(Activate ac) {
		super(ac.getName(), ac.getName() + "命令", "/" + ac.getName() + " help", ac.getCommands("PlayerCommand"));
		this.ac = ac;
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!ac.getMostBrain().isEnabled())
			return true;
		return false;
	}
}
