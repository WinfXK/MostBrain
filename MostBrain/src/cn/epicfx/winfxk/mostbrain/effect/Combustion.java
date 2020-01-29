package cn.epicfx.winfxk.mostbrain.effect;

import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.event.entity.EntityDamageEvent;

/**
 * 被攻击的人会燃烧</br>
 * 火药
 * 
 * @author Winfxk
 */
public class Combustion extends EffectItem {

	@Override
	public int getID() {
		return 289;
	}

	@Override
	public boolean isReDo() {
		return false;
	}

	@Override
	public void onDamage(EntityDamageEvent e) {
		e.getEntity().setOnFire(Tool.getRand(3, 8));
		gameData.honor += Tool.getRand(1, 2);
		gameData.score += Tool.getRand(1, 20);
	}
}
