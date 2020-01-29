package cn.epicfx.winfxk.mostbrain.effect;

import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.level.Level;
import cn.nukkit.level.Sound;
import cn.nukkit.level.particle.ExplodeParticle;

/**
 * 爆裂 被攻击的人将会爆炸 </br>
 * 发酵蛛眼
 * 
 * @author Winfxk
 */
public class Burst extends EffectItem {

	@Override
	public int getID() {
		return 376;
	}

	@Override
	public boolean isReDo() {
		return false;
	}

	@Override
	public void onDamage(EntityDamageEvent e) {
		gameData.honor += Tool.getRand(1, 2);
		Entity entity = e.getEntity();
		Level level = entity.getLevel();
		level.addParticle(new ExplodeParticle(entity.getLocation()));
		level.addSound(entity, Sound.RANDOM_EXPLODE);
		int as = (int) entity.getHealth() / 10;
		as = as <= 1 ? 1 : as;
		float aa = entity.getHealth();
		entity.setHealth(entity.getHealth() - Tool.getRand(1, as));
		gameData.score += aa - entity.getHealth();
	}
}
