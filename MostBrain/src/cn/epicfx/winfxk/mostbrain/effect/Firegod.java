package cn.epicfx.winfxk.mostbrain.effect;

import cn.epicfx.winfxk.mostbrain.EffectItem;
import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;

/**
 * 攻击自己的人有概率会被燃烧 </br>
 * 烈焰棒
 * 
 * @author Winfxk
 */
public class Firegod extends EffectItem {

	@Override
	public int getID() {
		return 369;
	}

	@Override
	public void onBeingDamage(EntityDamageEvent e) {
		if (!(e instanceof EntityDamageByEntityEvent) || Tool.getRand(0, 3) != 1)
			return;
		Entity entity = ((EntityDamageByEntityEvent) e).getDamager();
		entity.setOnFire(5);
		gameData.honor += Tool.getRand(1, 2);
		gameData.score += entity.getHealth();
	}
}
