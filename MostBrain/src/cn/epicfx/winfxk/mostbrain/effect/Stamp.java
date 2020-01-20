package cn.epicfx.winfxk.mostbrain.effect;

import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.potion.Effect;

/**
 * 标记 ，被攻击的人会被标记</br>
 * 命名牌
 * 
 * @author Winfxk
 */
public class Stamp extends EffectItem {
	@Override
	public int getID() {
		return -1;
	}

	@Override
	public int getDamage() {
		return 421;
	}

	@Override
	public boolean isReDo() {
		return false;
	}

	@Override
	public void onDamage(EntityDamageEvent e) {
		gameData.honor++;
		gameData.score++;
		Entity en = e.getEntity();
		en.setNameTag(getText());
		Effect effect = Effect.getEffect(15);
		effect.setDuration(600);
		effect.setColor(Tool.getRand(1, 255), Tool.getRand(1, 255), Tool.getRand(1, 255));
		en.addEffect(effect);
	}
}
