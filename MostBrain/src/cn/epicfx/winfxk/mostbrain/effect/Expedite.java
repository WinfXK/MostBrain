package cn.epicfx.winfxk.mostbrain.effect;

import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerItemConsumeEvent;
import cn.nukkit.potion.Effect;

/**
 * 给自己加速</br>
 * 红石粉
 * 
 * @author Winfxk
 */
public class Expedite extends EffectItem {

	@Override
	public int getID() {
		return 55;
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
	public void onConsume() {
	}

	@Override
	public void onBeingDamage(EntityDamageEvent e) {
	}

	@Override
	public void Wake() {
		if (i++ < 10)
			return;
		Effect effect = Effect.getEffect(1);
		effect.setDuration(280);
		effect.setColor(Tool.getRand(1, 255), Tool.getRand(1, 255), Tool.getRand(1, 255));
		player.addEffect(effect);
	}
}
