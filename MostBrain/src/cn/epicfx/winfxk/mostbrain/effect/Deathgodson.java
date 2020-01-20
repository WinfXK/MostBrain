package cn.epicfx.winfxk.mostbrain.effect;

import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerItemConsumeEvent;

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
	public void onItemConsume(PlayerItemConsumeEvent e) {
	}

	@Override
	public void onDamage(EntityDamageEvent e) {
	}

	@Override
	public void onConsume() {
	}

	@Override
	public void onBeingDamage(EntityDamageEvent e) {
		if (i++ < 2 && ((int) (player.getHealth() - e.getDamage())) <= 0) {
			e.setCancelled();
			player.setHealth(player.getMaxHealth() / 2);
		}
	}

	@Override
	public void Wake() {
	}
}
