package cn.epicfx.winfxk.mostbrain.effect;

import cn.nukkit.event.entity.EntityDamageEvent;

/**
 * 自己伤害减半 </br>
 * 钻石
 * 
 * @author Winfxk
 */
public class Protection extends EffectItem {

	@Override
	public int getID() {
		return 264;
	}

	@Override
	public int getDamage() {
		return -1;
	}

	@Override
	public void onBeingDamage(EntityDamageEvent e) {
		float ak = e.getDamage() / 2;
		ak = ak < 1 ? 1 : ak;
		e.setDamage(ak);
		gameData.honor++;
		gameData.score += e.getDamage() / 2;
	}
}
