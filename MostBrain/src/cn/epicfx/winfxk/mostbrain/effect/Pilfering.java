package cn.epicfx.winfxk.mostbrain.effect;

import java.util.ArrayList;
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
		List<Player> items = new ArrayList<>(ac.gameHandle.getGamePlayers());
		items.remove(player);
		Player player2 = ac.gameHandle.getGamePlayers().get(Tool.getRand(0, items.size() - 1));
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
}
