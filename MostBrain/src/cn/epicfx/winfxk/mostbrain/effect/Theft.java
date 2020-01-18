package cn.epicfx.winfxk.mostbrain.effect;

import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerItemConsumeEvent;

/**
 * 第一次攻击能完全克隆对手的所有特效</br>
 * 共鸣果
 * 
 * @author Winfxk
 */
public class Theft extends EffectItem {

	@Override
	public String getName() {
		return null;
	}

	@Override
	public int getID() {
		return 432;
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
	public String Function() {
		return null;
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
