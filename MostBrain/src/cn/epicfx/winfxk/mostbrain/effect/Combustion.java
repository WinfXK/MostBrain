package cn.epicfx.winfxk.mostbrain.effect;

import cn.nukkit.event.entity.EntityDamageEvent;

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
	public boolean isReDo() {
		return false;
	}

	@Override
	public int getDamage() {
		return -1;
	}

	@Override
	public void onDamage(EntityDamageEvent e) {
		e.getEntity().setOnFire(3);
		gameData.honor++;
		gameData.score++;
	}
}
