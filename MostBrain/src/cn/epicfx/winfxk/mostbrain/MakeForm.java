package cn.epicfx.winfxk.mostbrain;

import java.util.List;

import cn.epicfx.winfxk.mostbrain.tool.ItemIDSunName;
import cn.epicfx.winfxk.mostbrain.tool.SimpleForm;
import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponseSimple;

/**
 * @author Winfxk
 */
public class MakeForm {
	private Activate ac;
	public static final String[] SecondaryKey = { "{Player}", "{Money}", "{Name}", "{Hint}", "{Text}", "{Author}" };

	public MakeForm(Activate ac) {
		this.ac = ac;
	}

	/**
	 * 显示一个Buff详情页面
	 *
	 * @param item
	 * @return
	 */
	public boolean ShowEffectitem(Player player, EffectItem item) {
		SimpleForm form = new SimpleForm(ac.FormID.getID(1), ac.getMessage().getSun("UI", "Secondary", "Title", player),
				ac.getMessage().getSun("UI", "Secondary", "Content", SecondaryKey,
						new Object[] { player.getName(), MyPlayer.getMoney(player.getName()), item.getName(),
								item.getHint(), item.getText(), item.getAuthor() }));
		form.sendPlayer(player);
		return true;
	}

	/**
	 * 处理玩家点击主页按钮的事件
	 *
	 * @param player
	 * @param data
	 * @return
	 */
	public boolean disMain(Player player, FormResponseSimple data) {
		List<EffectItem> items = ac.getEffecttor().getList();
		int id = data.getClickedButtonId();
		id = id >= items.size() - 1 ? items.size() - 1 : id;
		return ShowEffectitem(player, items.get(id));
	}

	/**
	 * 发送Buff列表给一个玩家
	 *
	 * @param player
	 * @return
	 */
	public boolean MakeMain(Player player) {
		List<EffectItem> items = ac.getEffecttor().getList();
		SimpleForm form = new SimpleForm(ac.getFormID().getID(0), ac.getMessage().getSun("UI", "Main", "Title", player),
				ac.getMessage().getSun("UI", "Main", "Content", player));
		String Path;
		for (EffectItem item : items) {
			Path = ItemIDSunName.getIDByPath(item.getID(), item.getDamage() <= 0 ? 0 : item.getDamage());
			if (Path == null || Path.isEmpty())
				Path = ItemIDSunName.getIDByPath(item.getID(), 0);
			form.addButton(Tool.getRandColor() + item.getName(), true, Path);
		}
		if (form.getButtonSize() <= 0) {
			player.sendMessage(ac.getMessage().getSun("UI", "Main", "无法解析Buff数量", player));
			return true;
		}
		form.sendPlayer(player);
		return true;
	}
}
