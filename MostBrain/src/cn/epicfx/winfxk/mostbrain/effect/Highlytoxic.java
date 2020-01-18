package cn.epicfx.winfxk.mostbrain.effect;

import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerItemConsumeEvent;
import cn.nukkit.potion.Effect;

/**
 * 被攻击的人会中毒</br>
 * 毒马铃薯
 * 
 * @author Winfxk
 */
public class Highlytoxic extends EffectItem {

	@Override
	public int getID() {
		return 394;
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
		if (!(e instanceof EntityDamageByEntityEvent))
			return;
		Entity entity = ((EntityDamageByEntityEvent) e).getDamager();
		Effect effect = Effect.getEffect(19);
		effect.setDuration(60);
		effect.setColor(Tool.getRand(1, 255), Tool.getRand(1, 255), Tool.getRand(1, 255));
		entity.addEffect(effect);
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
