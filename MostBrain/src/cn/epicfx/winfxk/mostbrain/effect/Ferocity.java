package cn.epicfx.winfxk.mostbrain.effect;

import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerItemConsumeEvent;

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
	public int getDamage() {
		return -1;
	}

	@Override
	public void onItemConsume(PlayerItemConsumeEvent e) {
	}

	@Override
	public void onDamage(EntityDamageEvent e) {
		e.setDamage((float) (e.getDamage() * 1.5));
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
