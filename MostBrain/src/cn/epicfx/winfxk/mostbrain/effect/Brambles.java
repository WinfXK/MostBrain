package cn.epicfx.winfxk.mostbrain.effect;

import cn.epicfx.winfxk.mostbrain.EffectItem;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;

/**
 * 打自己的人会受到伤害->荆棘 </br>
 * 仙人掌
 *
 * @author Winfxk
 */
public class Brambles extends EffectItem {

	@Override
	public int getID() {
		return 81;
	}

	@Override
	public void onBeingDamage(EntityDamageEvent e) {
		if (!(e instanceof EntityDamageByEntityEvent))
			return;
		Entity en = ((EntityDamageByEntityEvent) e).getDamager();
		en.setHealth(en.getHealth() - 1);
		gameData.honor++;
		gameData.score += en.getHealth();
		super.onBeingDamage(e);
	}
}
