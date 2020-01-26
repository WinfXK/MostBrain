package cn.epicfx.winfxk.mostbrain;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.epicfx.winfxk.mostbrain.effect.EffectItem;
import cn.epicfx.winfxk.mostbrain.game.GameData;
import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.utils.Config;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class MyPlayer {
	private Activate ac;
	public Config config;
	private Player player;
	public boolean GameModel = false;
	public boolean ReadyModel = false;
	public boolean SettingModel = false;
	public GameData gameData;
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
	 * 增加游戏总分
	 * 
	 * @param score
	 * @return
	 */
	public MyPlayer addScore(int score) {
		long h = config.getLong("得分");
		config.set("得分", h + score);
		config.save();
		return this;
	}

	/**
	 * 增加玩家的荣耀
	 * 
	 * @param honor
	 * @return
	 */
	public MyPlayer addHonor(int honor) {
		long h = config.getLong("荣耀");
		config.set("荣耀", h + honor);
		config.save();
		return this;
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
	 * 加载玩家的背包
	 * 
	 * @return
	 */
	public MyPlayer loadInventory() {
		Object obj = config.get("Inventory");
		Map<Integer, Map<String, Object>> map = (obj == null || !(obj instanceof Map)) ? new HashMap<>()
				: (HashMap<Integer, Map<String, Object>>) obj;
		config.remove("Inventory");
		config.save();
		Map<Integer, Item> map2 = Tool.loadInventory(map);
		if (map2 != null && map2.size() > 0)
			player.getInventory().setContents(map2);
		else
			player.getInventory().clearAll();
		return this;
	}

	/**
	 * 保存玩家的背包
	 * 
	 * @return
	 */
	public MyPlayer saveInventory() {
		Map<Integer, Map<String, Object>> map = Tool.saveInventory(player);
		config.set("Inventory", map);
		config.save();
		return this;
	}

	/**
	 * 加载玩家的游戏模式
	 */
	public MyPlayer loadGameMode() {
		player.setGamemode(config.getInt("GameMode"));
		config.remove("GameMode");
		config.save();
		return this;
	}

	/**
	 * 保存玩家的游戏模式
	 * 
	 * @return
	 */
	public MyPlayer saveGameMode() {
		config.set("GameMode", player.getGamemode());
		config.save();
		return this;
	}

	/**
	 * 保存玩家的血量数据
	 * 
	 * @return
	 */
	public MyPlayer saveHealth() {
		config.set("Health", player.getHealth());
		config.set("MaxHealth", player.getMaxHealth());
		config.save();
		return this;
	}

	/**
	 * 加载玩家的位置
	 * 
	 * @return
	 */
	public MyPlayer loadXYZ() {
		Level level = Server.getInstance().getLevelByName(config.getString("Level"));
		if (level == null) {
			level = player.getLevel();
			player.teleport(level.getSpawnLocation());
		} else
			player.teleport(new Location(config.getDouble("X"), config.getDouble("Y"), config.getDouble("Z"), level));
		config.remove("X");
		config.remove("Y");
		config.remove("Z");
		config.remove("Level");
		config.save();
		return this;
	}

	/**
	 * 保存玩家所在的位置
	 * 
	 * @return
	 */
	public MyPlayer saveXYZ() {
		config.set("X", player.getX());
		config.set("Y", player.getY());
		config.set("Z", player.getZ() + 1);
		config.set("Level", player.getLevel().getFolderName());
		config.save();
		return this;
	}

	/**
	 * 加载玩家的血量
	 * 
	 * @return
	 */
	public MyPlayer setHealth() {
		player.setMaxHealth(config.getInt("MaxHealth"));
		player.setHealth(Tool.objToFloat(config.get("Health"), player.getMaxHealth() + 0.0000001f));
		config.remove("MaxHealth");
		config.remove("Health");
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
