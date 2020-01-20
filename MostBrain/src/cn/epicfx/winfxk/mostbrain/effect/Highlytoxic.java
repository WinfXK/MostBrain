package cn.epicfx.winfxk.mostbrain.effect;

import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.event.entity.EntityDamageEvent;
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
	public void onDamage(EntityDamageEvent e) {
		super.onDamage(e);
		Effect effect = Effect.getEffect(19);
		effect.setDuration(60);
		effect.setColor(Tool.getRand(1, 255), Tool.getRand(1, 255), Tool.getRand(1, 255));
		e.getEntity().addEffect(effect);
		gameData.honor++;
		gameData.score++;
	}
}
