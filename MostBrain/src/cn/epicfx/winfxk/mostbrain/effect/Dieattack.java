package cn.epicfx.winfxk.mostbrain.effect;

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
		if (i++ > 1)
			return;
		e.setDamage(e.getEntity().getHealth());
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
