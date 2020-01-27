package cn.epicfx.winfxk.mostbrain;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.Player;
import cn.nukkit.utils.Config;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class Message {
	private Activate ac;
	private static final String[] Key = { "{n}", "{ServerName}", "{PluginName}", "{MoneyName}", "{Time}", "{Date}" };
	private String[] Data;
	private Config Message;

	public Config getConfig() {
		return Message;
	}

	/**
	 * 文本变量快速插入♂
	 *
	 * @param ac
	 */
	public Message(Activate ac) {
		this(ac, true);
	}

	/**
	 * 文本变量快速插入♂
	 *
	 * @param ac
	 */
	public Message(Activate ac, boolean isLog) {
		this.ac = ac;
		Message = new Config(getFile(), 2);
		if (isLog)
			ac.getMostBrain().getLogger().info("§6Load the language: §e" + Message.getString("lang"));
		load();
	}

	public void reload() {
		Message = new Config(getFile(), 2);
		ac.getMostBrain().getLogger().info("§6Load the language: §e" + Message.getString("lang"));
		load();
	}

	public static File getFile() {
		return new File(Activate.getActivate().getMostBrain().getDataFolder(), Activate.MessageFileName);
	}

	/**
	 * 刷新全局变量的数据
	 */
	private void load() {
		Data = new String[] { "\n", ac.getMostBrain().getServer().getMotd(), ac.getMostBrain().getName(),
				ac.getMoneyName(), Tool.getTime(), Tool.getDate() };
	}

	/**
	 * 从配置文件中获取三级默认文本并插入数据
	 *
	 * @param t   一级Key
	 * @param Son 二级Key
	 * @param Sun 三级Key
	 * @return
	 */
	public String getSun(String t, String Son, String Sun) {
		return getSun(t, Son, Sun, new String[] {}, new String[] {});
	}

	/**
	 * 从配置文件中获取三级默认文本并插入数据
	 *
	 * @param t   一级Key
	 * @param Son 二级Key
	 * @param Sun 三级Key
	 * @param k   要插入的对应变量
	 * @param d   要插入的数据
	 * @return
	 */
	public String getSun(String t, String Son, String Sun, String[] k, Object[] d) {
		if (Message.exists(t) && (Message.get(t) instanceof Map)) {
			HashMap<String, Object> map = (HashMap<String, Object>) Message.get(t);
			if (map.containsKey(Son) && (map.get(Son) instanceof Map)) {
				map = (HashMap<String, Object>) map.get(Son);
				if (map.containsKey(Sun))
					return getText(map.get(Sun).toString(), k, d);
			}
		}
		return null;
	}

	/**
	 * 从配置文件中获取三级默认文本并插入数据
	 *
	 * @param t      一级Key
	 * @param Son    二级Key
	 * @param Sun    三级Key
	 * @param player 默认处理的玩家数据对象
	 * @return
	 */
	public String getSun(String t, String Son, String Sun, Player player) {
		return player == null ? getSun(t, Son, Sun)
				: getSun(t, Son, Sun, new String[] { "{Player}", "{Money}" },
						new Object[] { player.getName(), MyPlayer.getMoney(player.getName()) });
	}

	/**
	 * 从配置文件中获取三级默认文本并插入数据
	 *
	 * @param t        一级Key
	 * @param Son      二级Key
	 * @param Sun      三级Key
	 * @param myPlayer 默认处理的玩家数据对象
	 * @return
	 */
	public String getSun(String t, String Son, String Sun, MyPlayer myPlayer) {
		return getSun(t, Son, Sun, new String[] { "{Player}", "{Money}" },
				new Object[] { myPlayer.getPlayer().getName(), myPlayer.getMoney() });
	}

	/**
	 * 从配置文件中获取二级默认文本并插入数据
	 *
	 * @param t   一级Key
	 * @param Son 二级Key
	 * @return
	 */
	public String getSon(String t, String Son) {
		return getSon(t, Son, new String[] {}, new String[] {});
	}

	/**
	 * 从配置文件中获取二级默认文本并插入数据
	 *
	 * @param t      一级Key
	 * @param Son    二级Key
	 * @param player 默认处理的玩家数据对象
	 * @return
	 */
	public String getSon(String t, String Son, Player player) {
		return getSon(t, Son, new String[] { "{Player}", "{Money}" },
				new Object[] { player.getName(), MyPlayer.getMoney(player.getName()) });
	}

	/**
	 * 从配置文件中获取二级默认文本并插入数据
	 *
	 * @param t        一级Key
	 * @param Son      二级Key
	 * @param myPlayer 默认处理的玩家数据对象
	 * @return
	 */
	public String getSon(String t, String Son, MyPlayer myPlayer) {
		return getSon(t, Son, new String[] { "{Player}", "{Money}" },
				new Object[] { myPlayer.getPlayer().getName(), myPlayer.getMoney() });
	}

	/**
	 * 从配置文件中获取二级默认文本并插入数据
	 *
	 * @param t   一级Key
	 * @param Son 二级Key
	 * @param k   要插入的对应变量
	 * @param d   要插入的数据
	 * @return
	 */
	public String getSon(String t, String Son, String[] k, Object[] d) {
		if (Message.exists(t) && (Message.get(t) instanceof Map)) {
			HashMap<String, Object> map = (HashMap<String, Object>) Message.get(t);
			if (map.containsKey(Son))
				return getText(map.get(Son).toString(), k, d);
		}
		return null;
	}

	/**
	 * 从配置文件中获取一级默认文本并插入数据
	 *
	 * @param t 一级Key
	 * @return
	 */
	public String getMessage(String t) {
		return getMessage(t, new String[] {}, new Object[] {});
	}

	/**
	 * 从配置文件中获取一级默认文本并插入数据
	 *
	 * @param t      一级Key
	 * @param player 默认处理的玩家数据对象
	 * @return
	 */
	public String getMessage(String t, Player player) {
		return player == null ? getMessage(t)
				: getMessage(t, new String[] { "{Player}", "{Money}" },
						new Object[] { player.getName(), MyPlayer.getMoney(player.getName()) });
	}

	/**
	 * 从配置文件中获取一级默认文本并插入数据
	 *
	 * @param t        一级Key
	 * @param myPlayer 默认处理的玩家数据对象
	 * @return
	 */
	public String getMessage(String t, MyPlayer myPlayer) {
		return getMessage(t, new String[] { "{Player}", "{Money}" },
				new Object[] { myPlayer.getPlayer().getName(), myPlayer.getMoney() });
	}

	/**
	 * 从配置文件中获取一级默认文本并插入数据
	 *
	 * @param t 一级Key
	 * @param k 要插入的对应变量
	 * @param d 要插入的数据
	 * @return
	 */
	public String getMessage(String t, String[] k, Object[] d) {
		if (Message.exists(t))
			return getText(Message.getString(t), k, d);
		return null;
	}

	/**
	 * 将数据插入文本中
	 *
	 * @param tex 要插入修改的文本
	 * @return
	 */
	public String getText(Object t, MyPlayer myPlayer) {
		return getText(t, new String[] { "{Player}", "{Money}" },
				new Object[] { myPlayer.getPlayer().getName(), myPlayer.getMoney() });
	}

	/**
	 * 将数据插入文本中
	 *
	 * @param tex 要插入修改的文本
	 * @return
	 */
	public String getText(Object text, Player player) {
		return getText(text, new String[] { "{Player}", "{Money}" },
				new Object[] { player.getName(), MyPlayer.getMoney(player.getName()) });
	}

	/**
	 * 将数据插入文本中
	 *
	 * @param tex 要插入修改的文本
	 * @return
	 */
	public String getText(Object text) {
		return getText(text, new String[] {}, new String[] {});
	}

	/**
	 * 将数据插入文本中
	 *
	 * @param tex 要插入修改的文本
	 * @param k   对应的变量
	 * @param d   对应的数据
	 * @return
	 */
	public String getText(Object tex, String[] k, Object[] d) {
		if (tex == null)
			return null;
		load();
		String text = String.valueOf(tex);
		if (text == null || text.isEmpty())
			return null;
		for (int i = 0; i < Key.length; i++)
			if (text.contains(Key[i]))
				text = text.replace(Key[i], Data[i]);
		for (int i = 0; (i < k.length && i < d.length); i++)
			if (text.contains(k[i]))
				text = text.replace(k[i], String.valueOf(d[i]));
		if (text.contains("{RandColor}")) {
			String[] strings = text.split("\\{RandColor\\}");
			text = "";
			for (String s : strings)
				text += Tool.getRandColor() + s;
		}
		if (text.contains("{RGBTextStart}") && text.contains("{RGBTextEnd}")) {
			String rgb = "", rString, gString;
			String[] rgbStrings = text.split("\\{RGBTextEnd\\}");
			for (String rgbString : rgbStrings)
				if (rgbString.contains("{RGBTextStart}")) {
					rString = rgbString + "{RGBTextEnd}";
					gString = Tool.cutString(rString, "{RGBTextStart}", "{RGBTextEnd}");
					if (gString == null || gString.isEmpty())
						gString = "";
					rgb += rString.replace("{RGBTextStart}" + gString + "{RGBTextEnd}", Tool.getColorFont(gString));
				} else
					rgb += rgbString;
			text = rgb;
		}
		return text;
	}
}
