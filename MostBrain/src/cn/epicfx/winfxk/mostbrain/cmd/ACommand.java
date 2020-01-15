package cn.epicfx.winfxk.mostbrain.cmd;

import cn.epicfx.winfxk.mostbrain.Activate;
import cn.epicfx.winfxk.mostbrain.Message;
import cn.epicfx.winfxk.mostbrain.MyPlayer;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * @author Winfxk
 */
public class ACommand extends Command {
	private Activate ac;
	private static final String Name_ = "Admin";
	public static final String Permission = "MostBrain.Command.Admin";
	private Message msg;
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
		if (!sender.isPlayer()) {
			sender.sendMessage(msg.getMessage("请在游戏内自行命令"));
			return true;
		}
		Player player = (Player) sender;
		if (!sender.hasPermission(Permission)) {
			sender.sendMessage(msg.getMessage("权限不足", player));
			return true;
		}
		if (args == null || args.length == 0)
			return false;
		switch (args[0].toLowerCase()) {
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
			myPlayer.SettingModel = true;
			ac.setPlayers(player, myPlayer);
			Item item = new Item(290, 0, 1);
			item.setCustomName("§e游戏创作者");
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
}
