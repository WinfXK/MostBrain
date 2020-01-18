package cn.epicfx.winfxk.mostbrain.effect;

import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerItemConsumeEvent;

/**
 * 眩晕 被自己攻击的人会眩晕 </br>
 * 睡莲
 * 
 * @author Winfxk
 */
public class Vertigo extends EffectItem {

	@Override
	public int getID() {
		return 111;
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
	public String getName() {
		return null;
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
