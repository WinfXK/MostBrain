package cn.epicfx.winfxk.mostbrain.game;

import cn.epicfx.winfxk.mostbrain.Activate;
import cn.epicfx.winfxk.mostbrain.MyPlayer;
import cn.nukkit.Player;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerQuitEvent;

/**
 * @author Winfxk
 */
public class GameEvent {
	private Activate ac;

	public GameEvent(Activate ac) {
		this.ac = ac;
	}

	/**
	 * 玩家在游戏内退出游戏事件
	 * 
	 * @param e
	 */
	public void QuitGame(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		MyPlayer myPlayer = ac.getPlayers(player.getName());
		if (myPlayer != null && (myPlayer.GameModel || myPlayer.ReadyModel))
			ac.gameHandle.QuitGame(player, true, true, false);
	}

	/**
	 * 点击开始木牌
	 * 
	 * @param e
	 */
	public void Start(BlockBreakEvent e) {
		Start(new PlayerInteractEvent(e.getPlayer(), e.getItem(), e.getBlock(), e.getFace()));
	}

	/**
	 * 点击开始木牌
	 * 
	 * @param e
	 */
	public void Start(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		if (ac.gameHandle.StartGame()) {
			player.sendMessage(getMessage("游戏已开始", player));
			e.setCancelled();
			return;
		}
		if (ac.gameHandle.getGamePlayers().contains(player)) {
			player.sendMessage(getMessage("已加入", player));
			e.setCancelled();
			return;
		}
		ac.gameHandle.addPlayer(player);
		e.setCancelled();
	}

	public String getMessage(String Key) {
		return ac.getMessage().getSon("Game", Key);
	}

	public String getMessage(String Key, Player player) {
		return ac.getMessage().getSon("Game", Key, player);
	}
}
