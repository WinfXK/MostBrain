package cn.epicfx.winfxk.mostbrain.game;

import java.util.Map;

import cn.epicfx.winfxk.mostbrain.Activate;
import cn.nukkit.block.Block;
import cn.nukkit.level.Location;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.Config;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class MostConfig {
	public Config config;
	/**
	 * 木牌的位置
	 */
	public Map<String, Double> Start;
	public Map<String, Double> MaxLocation;
	public Map<String, Double> MinLocation;
	public Map<String, Double> Spawn;
	public String Creator, CreatorTime;
	/**
	 * 木牌所在的世界
	 */
	public String Level;
	/**
	 * 游戏区域所在的世界
	 */
	public String StartLevel;
	public double MaxX, MinX, MaxY, MinY, MaxZ, MinZ;

	public MostConfig(Activate ac) {
		try {
			config = ac.getGameConfig();
			Level = config.getString("Level");
			StartLevel = config.getString("StartLevel");
			Start = (Map<String, Double>) config.get("Start");
			Spawn = (Map<String, Double>) config.get("Spawn");
			MaxLocation = (Map<String, Double>) config.get("MaxLocation");
			MinLocation = (Map<String, Double>) config.get("MinLocation");
			Creator = config.getString("Creator");
			CreatorTime = config.getString("CreatorTime");
			MaxX = MaxLocation.get("X");
			MaxY = MaxLocation.get("Y");
			MaxZ = MaxLocation.get("Z");
			MinX = MinLocation.get("X");
			MinY = MinLocation.get("Y");
			MinZ = MinLocation.get("Z");
			ac.gameEvent = new GameEvent(ac);
			ac.gameHandle = new GameHandle(ac);
		} catch (Exception e) {
			ac.getMostBrain().getLogger().error(ac.getMessage().getMessage("数据加载错误"));
			ac.isStartGame = false;
			ac.SettingModel = false;
			ac.settingGame = null;
			ac.isGameSettingUp = false;
			ac.setMostConfig(null);
			ac.GameError = true;
		}
	}

	public Config getConfig() {
		return config;
	}

	/**
	 * 得到创建游戏的玩家名
	 * 
	 * @return
	 */
	public String getCreator() {
		return Creator;
	}

	/**
	 * 得到创建游戏的时间
	 * 
	 * @return
	 */
	public String getCreatorTime() {
		return CreatorTime;
	}

	/**
	 * 判断方块是否收到保护
	 * 
	 * @param block
	 * @return
	 */
	public boolean isNotBreakBlock(Block block) {
		Location v3 = block.getLocation();
		String level = v3.level.getFolderName();
		if (level.equals(StartLevel))
			if (v3.x == Start.get("X") && v3.y == Start.get("Y") && v3.z == Start.get("Z"))
				return true;
		if (level.equals(Level))
			return (v3.x >= MinX && v3.y >= MinY && v3.z >= MinZ && v3.x <= MaxX && v3.y <= MaxY && v3.z <= MaxZ);
		return false;
	}

	/**
	 * 判断方块是否收到保护
	 * 
	 * @param v3
	 * @return
	 */
	public boolean isNotBreakBlock(Location v3) {
		String level = v3.level.getFolderName();
		if (level.equals(StartLevel))
			if (v3.x == Start.get("X") && v3.y == Start.get("Y") && v3.z == Start.get("Z"))
				return true;
		if (level.equals(Level))
			return (v3.x >= MinX && v3.y >= MinY && v3.z >= MinZ && v3.x <= MaxX && v3.y <= MaxY && v3.z <= MaxZ);
		return false;
	}

	/**
	 * 判断方块是否收到保护
	 * 
	 * @param level
	 * @param vector3
	 * @return
	 */
	public boolean isNotBreakBlock(String level, Vector3 v3) {
		if (level.equals(StartLevel))
			if (v3.x == Start.get("X") && v3.y == Start.get("Y") && v3.z == Start.get("Z"))
				return true;
		if (level.equals(Level))
			return (v3.x >= MinX && v3.y >= MinY && v3.z >= MinZ && v3.x <= MaxX && v3.y <= MaxY && v3.z <= MaxZ);
		return false;
	}

	/**
	 * 得到出生点
	 * 
	 * @return
	 */
	public Vector3 getSpawn() {
		return new Vector3(Spawn.get("X"), Spawn.get("Y"), Spawn.get("Z"));
	}

	/**
	 * 得到木牌的位置
	 * 
	 * @return
	 */
	public Vector3 getStart() {
		return new Vector3(Start.get("X"), Start.get("Y"), Start.get("Z"));
	}
}
