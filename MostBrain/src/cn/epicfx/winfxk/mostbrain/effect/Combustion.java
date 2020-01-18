package cn.epicfx.winfxk.mostbrain.effect;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerItemConsumeEvent;

/**
 * 被攻击的人会燃烧</br>
 * 火药
 * 
 * @author Winfxk
 */
public class Combustion extends EffectItem {

	@Override
	public int getID() {
		return 289;
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
		if (!(e instanceof EntityDamageEvent))
			return;
		Entity en = ((EntityDamageByEntityEvent) e).getDamager();
		en.setOnFire(3);
	}

	@Override
	public void onConsume() {
	}

	@Override
	public void onBeingDamage(EntityDamageEvent e) {
	}

	@Override
	public void Wake() {
	}
}
