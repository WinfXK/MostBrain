package cn.epicfx.winfxk.mostbrain;

import java.io.File;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;

/**
 * @author Winfxk
 */
public class MyPlayer {
	private Activate ac;
	public Config config;
	private Player player;
	public boolean SettingModel = false;
	public boolean GameModel = false;
	public GameData gameData;

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
