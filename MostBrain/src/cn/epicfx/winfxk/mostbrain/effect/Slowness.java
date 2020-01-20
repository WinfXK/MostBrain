package cn.epicfx.winfxk.mostbrain.effect;

import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.potion.Effect;

/**
 * 缓慢Buff </br>
 * 蜘蛛丝
 * 
 * @author Winfxk
 */
public class Slowness extends EffectItem {

	@Override
	public int getID() {
		return 30;
	}

	@Override
	public int getDamage() {
		return -1;
	}

	@Override
	public void onDamage(EntityDamageEvent e) {
		Effect effect = Effect.getEffect(2);
		effect.setDuration(280);
		effect.setColor(Tool.getRand(1, 255), Tool.getRand(1, 255), Tool.getRand(1, 255));
		e.getEntity().addEffect(effect);
		gameData.honor++;
		gameData.score++;
	}
}
