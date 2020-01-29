package cn.epicfx.winfxk.mostbrain.game;

import cn.nukkit.Player;

/**
 * @author Winfxk
 */
public interface MostEvent {

	/**
	 * 玩家准备游戏时加载
	 * 
	 * @param player
	 */
	public void onReadyis(Player player);

	/**
	 * 玩家开始游戏事加载
	 * 
	 * @param player
	 */
	public void onStart(Player player);

	/**
	 * 玩家游戏内每秒调用一次
	 * 
	 * @param player
	 */
	public void Wake(Player player);

	/**
	 * 游戏结束时调用
	 * 
	 * @param player
	 */
	public void onEnd(Player player);

	/**
	 * 玩家退出游戏时调用
	 * 
	 * @param player 退出游戏的玩家
	 * @param isEnd  是否是游戏结束退出额
	 */
	public void onQuit(Player player, boolean isEnd);
}
