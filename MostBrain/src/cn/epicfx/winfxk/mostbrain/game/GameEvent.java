package cn.epicfx.winfxk.mostbrain.game;

import cn.epicfx.winfxk.mostbrain.Activate;
import cn.nukkit.Player;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerInteractEvent;

/**
 * @author Winfxk
 */
public class GameEvent {
	private Activate ac;

	public GameEvent(Activate ac) {
		this.ac = ac;
	}

	/**
	 * 点击开始木牌
	 * 
	 * @param e
	 */
	public void Start(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		if (ac.isStartGame) {
			player.sendMessage(getMessage("游戏已开始", player));
			e.setCancelled();
			return;
		}

	}

	/**
	 * 在游戏中发生攻击事件
	 * 
	 * @param e
	 */
	public void onDamage(EntityDamageEvent e) {

	}

	public String getMessage(String Key) {
		return ac.getMessage().getSon("Game", Key);
	}

	public String getMessage(String Key, Player player) {
		return ac.getMessage().getSon("Game", Key, player);
	}
}
