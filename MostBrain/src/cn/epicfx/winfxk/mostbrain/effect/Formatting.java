package cn.epicfx.winfxk.mostbrain.effect;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageEvent;

/**
 * 自己的第一次攻击能清除对手的所有buff</br>
 * 爆裂共鸣果
 * 
 * @author Winfxk
 */
public class Formatting extends EffectItem {

	@Override
	public int getID() {
		return 432;
	}

	@Override
	public int getDamage() {
		return -1;
	}

	@Override
	public void onDamage(EntityDamageEvent e) {
		if (i++ > 1)
			return;
		Entity entity = e.getEntity();
		if (!(entity instanceof Player))
			return;
		Player player = (Player) entity;
		gameData.honor++;
		gameData.score += 2 * ac.gameHandle.getEffects(player);
		ac.gameHandle.clearBuffs(player);
	}
}
