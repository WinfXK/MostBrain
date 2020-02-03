package cn.epicfx.winfxk.mostbrain.effect;

import cn.epicfx.winfxk.mostbrain.EffectItem;
import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.level.Sound;

/**
 * 燃血之法，每次攻击都会有伤害加倍效果，但是会扣除自己等量城半的学年 </br>
 * 火球
 * 
 * @author Winfxk
 */
public class Combustionblood extends EffectItem {

	@Override
	public int getID() {
		return 385;
	}

	@Override
	public void onDamage(EntityDamageEvent e) {
		if (!(e instanceof EntityDamageByEntityEvent))
			return;
		float health = player.getHealth();
		double bl = Tool.getRand(0, 5) / 10;
		float damge = e.getDamage();
		double jH = damge + (damge * bl) / 2;
		Entity entity = ((EntityDamageByEntityEvent) e).getDamager();
		jH = jH >= entity.getHealth() ? entity.getHealth() - 1 : jH;
		if (health <= jH)
			return;
		e.setDamage(Tool.objToFloat(damge + (damge * bl), e.getDamage()));
		player.setHealth(Tool.objToFloat(health - jH, health));
		gameData.honor += Tool.getRand(1, 2);
		gameData.score += jH;
		entity.getLevel().addSound(entity.getLocation(), Sound.DAMAGE_FALLBIG);
	}
}
