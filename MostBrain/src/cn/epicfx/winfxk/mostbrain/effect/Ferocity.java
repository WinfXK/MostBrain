package cn.epicfx.winfxk.mostbrain.effect;

import cn.nukkit.event.entity.EntityDamageEvent;

/**
 * 攻击力增加一半</br>
 * 钻石剑
 * 
 * @author Winfxk
 */
public class Ferocity extends EffectItem {

	@Override
	public int getID() {
		return 276;
	}

	@Override
	public void onDamage(EntityDamageEvent e) {
		e.setDamage((float) (e.getDamage() * 1.5));
		gameData.honor++;
		gameData.score += e.getDamage();
	}
}
