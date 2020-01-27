package cn.epicfx.winfxk.mostbrain.effect;

import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.level.Location;
import cn.nukkit.level.Sound;

/**
 * 被打死后三次被攻击将大概率闪避并且和敌方位移 </br>
 * 乌龟壳子
 *
 * @author Winfxk
 */
public class Frightened extends EffectItem {
	private boolean isSB = false;
	private int JJLength = 3;

	@Override
	public int getID() {
		return 469;
	}

	@Override
	public boolean isReDo() {
		return false;
	}

	@Override
	public void onBeingDamage(EntityDamageEvent e) {
		if (player.getHealth() <= e.getDamage()) {
			isSB = true;
			JJLength = 3;
			super.onBeingDamage(e);
			return;
		}
		if (!isSB || !(e instanceof EntityDamageByEntityEvent) || e.isCancelled() || Tool.getRand(1, 3) == 3
				|| JJLength-- < 0) {
			super.onBeingDamage(e);
			return;
		}
		e.setDamage(0.001f);
		Entity entity = ((EntityDamageByEntityEvent) e).getDamager();
		Location l1 = entity.getLocation();
		Location l2 = player.getLocation();
		entity.teleport(l2);
		player.teleport(l1);
		entity.getLevel().addSound(l1, Sound.RANDOM_ENDERCHESTCLOSED);
		player.getLevel().addSound(l2, Sound.RANDOM_ENDERCHESTCLOSED);
		gameData.honor++;
		gameData.score += entity.getHealth();
	}
}
