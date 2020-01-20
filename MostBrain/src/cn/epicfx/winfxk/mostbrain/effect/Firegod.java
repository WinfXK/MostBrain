package cn.epicfx.winfxk.mostbrain.effect;

import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerItemConsumeEvent;

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
		if (!(e instanceof EntityDamageByEntityEvent) || Tool.getRand(0, 3) != 1)
			return;
		Entity entity = ((EntityDamageByEntityEvent) e).getDamager();
		entity.setOnFire(2);
	}

	@Override
	public void Wake() {
	}
}
