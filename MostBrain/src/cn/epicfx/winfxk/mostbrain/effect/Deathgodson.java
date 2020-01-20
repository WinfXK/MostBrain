package cn.epicfx.winfxk.mostbrain.effect;

import cn.nukkit.event.entity.EntityDamageEvent;

/**
 * 阻挡一次必死攻击 </br>
 * 下界之星
 * 
 * @author Winfxk
 */
public class Deathgodson extends EffectItem {
	@Override
	public int getID() {
		return 399;
	}

	@Override
	public int getDamage() {
		return -1;
	}

	@Override
	public void onBeingDamage(EntityDamageEvent e) {
		if (i++ < 2 && ((int) (player.getHealth() - e.getDamage())) <= 0) {
			e.setCancelled();
			player.setHealth(player.getMaxHealth() / 2);
			gameData.honor++;
			gameData.score *= 2;
		}
	}
}
