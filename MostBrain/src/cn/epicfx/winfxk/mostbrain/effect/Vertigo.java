package cn.epicfx.winfxk.mostbrain.effect;

import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.potion.Effect;

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
	public void onDamage(EntityDamageEvent e) {
		Effect effect = Effect.getEffect(9);
		effect.setDuration(280);
		effect.setColor(Tool.getRand(1, 255), Tool.getRand(1, 255), Tool.getRand(1, 255));
		e.getEntity().addEffect(effect);
		gameData.honor++;
		gameData.score += 5;
	}
}
