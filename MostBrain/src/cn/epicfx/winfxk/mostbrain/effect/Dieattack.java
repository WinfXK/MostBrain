package cn.epicfx.winfxk.mostbrain.effect;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerItemConsumeEvent;

/**
 * 必死一击，自己第一次打人必死</br>
 * 金苹果
 * 
 * @author Winfxk
 */
public class Dieattack extends EffectItem {
	@Override
	public int getID() {
		return 322;
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
		if (i > 0 || !(e instanceof EntityDamageByEntityEvent))
			return;
		i++;
		Entity entity = ((EntityDamageByEntityEvent) e).getDamager();
		e.setDamage(entity.getHealth());
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
