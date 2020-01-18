package cn.epicfx.winfxk.mostbrain.effect;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerItemConsumeEvent;

/**
 * 自己的前三次攻击直接将被攻击者血量减半</br>
 * 剪刀
 * 
 * @author Winfxk
 */
public class Accumulation extends EffectItem {
	@Override
	public int getID() {
		return 359;
	}

	@Override
	public int getDamage() {
		return -1;
	}

	@Override
	public void onItemConsume(PlayerItemConsumeEvent e) {
	}

	@Override
	public void onConsume() {
	}

	@Override
	public void onDamage(EntityDamageEvent e) {
		if (i >= 3 || !(e instanceof EntityDamageByEntityEvent))
			return;
		Entity entity = ((EntityDamageByEntityEvent) e).getDamager();
		i++;
		entity.setHealth(entity.getHealth() / 2);
	}

	@Override
	public void onBeingDamage(EntityDamageEvent e) {
	}

	@Override
	public void Wake() {
	}

}
