package cn.epicfx.winfxk.mostbrain.effect;

import cn.epicfx.winfxk.mostbrain.MyPlayer;
import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageEvent;

/**
 * 每次杀敌都会增加自己的血量，若血量已达上限，则每次将会增加自己的血量上限，死亡后血量上限叠加将会失效 </br>
 * 盔甲架
 *
 * @author Winfxk
 */
public class Noregretslife extends EffectItem {
	@Override
	public int getID() {
		return 425;
	}

	@Override
	public void onBeingDamage(EntityDamageEvent e) {
		if (player.getHealth() > e.getDamage())
			return;
		int is = ac.getConfig().getInt("游戏最大血量");
		player.setMaxHealth(is);
		gameData.honor -= myPlayer.items.size();
		gameData.score -= is;
	}

	@Override
	public void onDamage(EntityDamageEvent e) {
		if (e.getDamage() < e.getEntity().getHealth())
			return;
		Entity entity = e.getEntity();
		if (entity instanceof Player) {
			MyPlayer myPlayer = ac.getPlayers(entity.getName());
			if (myPlayer != null && myPlayer.GameModel && myPlayer.gameData != null) {
				myPlayer.gameData.score -= player.getMaxHealth() + e.getDamage();
				myPlayer.gameData.honor--;
			}
		}
		if (player.getHealth() >= player.getMaxHealth()) {
			player.setMaxHealth(player.getMaxHealth() + 1);
			gameData.honor++;
			gameData.score += player.getMaxHealth();
			return;
		}
		float h = player.getHealth();
		h = h + 2 > player.getMaxHealth() ? player.getMaxHealth() : h;
		player.setHealth(h);
		gameData.honor++;
		gameData.score += h;
	}
}
