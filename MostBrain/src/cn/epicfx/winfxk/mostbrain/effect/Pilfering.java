package cn.epicfx.winfxk.mostbrain.effect;

import java.util.List;

import cn.epicfx.winfxk.mostbrain.MyPlayer;
import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.Player;
import cn.nukkit.event.entity.EntityDamageEvent;

/**
 * 顺手牵羊</br>
 * 在死亡的时候会随机带走一个玩家 </br>
 * 栓绳
 *
 * @author Winfxk
 */
public class Pilfering extends EffectItem {
	private int M;
	private String[] K = { "{ByPlayer}", "{ByMoney}" };

	@Override
	public int getID() {
		return 420;
	}

	@Override
	public boolean isReDo() {
		return false;
	}

	@Override
	public void onBeingDamage(EntityDamageEvent e) {
		if (player.getHealth() > e.getDamage())
			return;
		gameData.honor++;
		if (ac.gameHandle.getGamePlayers().size() <= 1) {
			e.setDamage(0.01f);
			gameData.score += player.getMaxHealth() - player.getHealth();
			player.setHealth(player.getMaxHealth());
			return;
		}
		M = ac.getConfig().getInt("递归深度");
		Player player2 = getSB(ac.gameHandle.getGamePlayers(), 0);
		if (player2.equals(player) || player2.getName().equals(player.getName())) {
			e.setCancelled();
			gameData.score += player.getMaxHealth() + player.getHealth();
			player.setHealth(player.getMaxHealth());
			return;
		}
		gameData.score += player2.getHealth();
		player2.sendMessage(ac.getMessage().getText(getText(), K,
				new Object[] { player2.getName(), MyPlayer.getMoney(player2.getName()) }));
	}

	/**
	 * 随机或区域一个不是自己的逗比玩家
	 *
	 * @param players
	 * @param length  已经循环了的深度
	 * @return
	 */
	public Player getSB(List<Player> players, int length) {
		Player player2 = players.get(Tool.getRand(0, players.size() - 1));
		if (player2.getName().equals(player2.getName()))
			if (M <= length)
				return player2;
			else
				player2 = getSB(players, length++);
		return player2;
	}
}
