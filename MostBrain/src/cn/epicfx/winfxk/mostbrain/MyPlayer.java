package cn.epicfx.winfxk.mostbrain;

import java.io.File;
import java.util.List;
import java.util.Map;

import cn.epicfx.winfxk.mostbrain.effect.EffectItem;
import cn.epicfx.winfxk.mostbrain.game.GameData;
import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;

/**
 * @author Winfxk
 */
public class MyPlayer {
	private Activate ac;
	public Config config;
	private Player player;
	public GameData gameData;
	public boolean GameModel = false;
	public boolean ReadyModel = false;
	public boolean SettingModel = false;
	/**
	 * 玩家的游戏特效
	 */
	public List<EffectItem> items;

	/**
	 * 记录存储玩家的一些数据
	 * 
	 * @param player
	 */
	public MyPlayer(Player player) {
		this.player = player;
		ac = Activate.getActivate();
		config = getConfig(player.getName());
	}

	public Player getPlayer() {
		return player;
	}

	/**
	 * 获取逗比玩家的金币数量
	 * 
	 * @return
	 */
	public double getMoney() {
		return ac.getEconomy().getMoney(player.getName());
	}

	/**
	 * 将玩家存在配置文件的背包数据设置到玩家
	 * 
	 * @return
	 */
	public MyPlayer setInventory() {
		if (!config.exists("Inventory"))
			return this;
		Map<Integer, Item> map = loadInventory();
		if (map != null && map.size() > 0)
			player.getInventory().setContents(map);
		config.remove("Inventory");
		config.save();
		return this;
	}

	/**
	 * 保存玩家的背包
	 * 
	 * @return
	 */
	public Map<Integer, Item> loadInventory() {
		List<Map<String, Object>> list = config.getList("Inventory");
		if (list == null || list.size() < 1)
			return null;
		return Tool.loadInventory(list);
	}

	/**
	 * 保存玩家的背包
	 * 
	 * @return
	 */
	public MyPlayer saveInventory() {
		config.set("Inventory", Tool.saveInventory(player));
		config.save();
		return this;
	}

	/**
	 * 获取逗比玩家的金币数量
	 * 
	 * @return
	 */
	public static double getMoney(String player) {
		return Activate.getActivate().getEconomy().getMoney(player);
	}

	public Config getConfig() {
		return config;
	}

	public static Config getConfig(String player) {
		return Activate.getActivate().resCheck.Check(new Config(getFile(player), Config.YAML));
	}

	public File getFile() {
		return new File(new File(ac.getMostBrain().getDataFolder(), Activate.PlayerDataDirName),
				player.getName() + ".yml");
	}

	public static File getFile(String player) {
		return new File(new File(Activate.getActivate().getMostBrain().getDataFolder(), Activate.PlayerDataDirName),
				player + ".yml");
	}
}
