package cn.epicfx.winfxk.mostbrain.effect;

import cn.epicfx.winfxk.mostbrain.EffectItem;
import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.level.Sound;

/**
 * 自己的前三次攻击直接将被攻击者血量减半</br>
 * 剪刀
 *
 * @author Winfxk
 */
public class Accumulation extends EffectItem {
	@Override
	public int getID() {
		return 359;
	}

	@Override
	public void onDamage(EntityDamageEvent e) {
		if (i++ > 3)
			return;
		Entity entity = e.getEntity();
		entity.setHealth(entity.getHealth() / 2);
		entity.getLevel().addSound(entity.getLocation(), Sound.DAMAGE_FALLBIG);
		gameData.honor += Tool.getRand(1, 2);
		gameData.score += entity.getHealth() * 2;
	}
}
