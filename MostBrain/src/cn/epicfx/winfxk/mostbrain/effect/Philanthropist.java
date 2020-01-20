package cn.epicfx.winfxk.mostbrain.effect;

import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;

/**
 * 攻击自己的人会随机回血 </br>
 * 海晶碎片
 * 
 * @author Winfxk
 */
public class Philanthropist extends EffectItem {

	@Override
	public int getID() {
		return 409;
	}

	@Override
	public int getDamage() {
		return -1;
	}

	@Override
	public void onBeingDamage(EntityDamageEvent e) {
		if (!(e instanceof EntityDamageByEntityEvent))
			return;
		Entity entity = ((EntityDamageByEntityEvent) e).getDamager();
		float h = entity.getHealth();
		int mh = entity.getMaxHealth();
		if (h >= mh)
			return;
		h += (h / 5) <= 1 ? mh : Tool.getRand(1, (int) (h / 5));
		entity.setHealth(h > mh ? mh : h);
		gameData.honor -= 5;
		gameData.score -= h;
	}
}
