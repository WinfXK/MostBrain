package cn.epicfx.winfxk.mostbrain.game;

import java.util.List;

import cn.epicfx.winfxk.mostbrain.Activate;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;

/**
 * @author Winfxk
 */
public class GameHandle {
	private Activate ac;
	private GameThread gameThread;
	/**
	 * 已等待时间
	 */
	private int ReadyisTime;
	/**
	 * 剩余等待时间
	 */
	private int ReadyTime;
	private List<Player> gamePlayers;
	private boolean StartGame = false;
	/**
	 * 重生点
	 */
	private Location location;
	private MostConfig mostConfig;
	/**
	 * 重生点所在的世界
	 */
	private Level SpawnLevel;

	public GameHandle(Activate ac) {
		this.ac = ac;
		mostConfig = ac.getMostConfig();
		SpawnLevel = Server.getInstance().getLevelByName(mostConfig.StartLevel);
		location = new Location(mostConfig.Spawn.get("X"), mostConfig.Spawn.get("Y"), mostConfig.Spawn.get("Z"),
				SpawnLevel);
	}

	public void addPlayer(Player player) {
		if (StartGame)
			return;
		if (gamePlayers.contains(player))
			return;

	}

	private class GameThread extends Thread {
		@Override
		public void run() {
			super.run();
		}
	}
}
