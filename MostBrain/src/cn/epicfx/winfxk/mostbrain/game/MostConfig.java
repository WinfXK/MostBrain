package cn.epicfx.winfxk.mostbrain.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.epicfx.winfxk.mostbrain.Activate;
import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.Config;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class MostConfig {
	private Config config;
	private List<String> NotBreakBlocks;
	private Map<String, Double> Start;
	private List<Player> PlayingGame;
	private Map<String, Integer> Played;
	private Map<String, Double> Spawn;
	private String Creator, CreatorTime;

	public MostConfig(Activate ac) {
		config = ac.getGameConfig();
		NotBreakBlocks = config.getList("NotBreakBlocks");
		Start = (Map<String, Double>) config.get("Start");
		Played = (Map<String, Integer>) config.get("Played");
		Spawn = (Map<String, Double>) config.get("Spawn");
		PlayingGame = new ArrayList<>();
		Creator = config.getString("Creator");
		CreatorTime = config.getString("CreatorTime");
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
	 * 得到受保护的方块列表
	 * 
	 * @return
	 */
	public List<String> getNotBreakBlocks() {
		return NotBreakBlocks;
	}

	/**
	 * 判断方块是否收到保护
	 * 
	 * @param block
	 * @return
	 */
	public boolean isNotBreakBlock(Block block) {
		return isNotBreakBlock(block.getLevel().getFolderName(), new Vector3(block.getX(), block.getY(), block.getZ()));
	}

	/**
	 * 判断方块是否收到保护
	 * 
	 * @param level
	 * @param vector3
	 * @return
	 */
	public boolean isNotBreakBlock(String level, Vector3 vector3) {
		return NotBreakBlocks
				.contains("Le." + level + " x." + (int) vector3.x + " y." + (int) vector3.y + " z." + (int) vector3.z);
	}

	/**
	 * 得到已经参与游戏过得玩家名
	 * 
	 * @return
	 */
	public Map<String, Integer> getPlayed() {
		return Played;
	}

	/**
	 * 得到正在玩游戏的玩家名
	 * 
	 * @return
	 */
	public List<Player> getPlayingGame() {
		return PlayingGame;
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
