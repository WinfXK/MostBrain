package cn.epicfx.winfxk.mostbrain.effect;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerItemConsumeEvent;

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
	public void onItemConsume(PlayerItemConsumeEvent e) {
	}

	@Override
	public void onDamage(EntityDamageEvent e) {
		if (i++ > 1)
			return;
		Entity entity = e.getEntity();
		if (!(entity instanceof Player))
			return;
		Player player = (Player) entity;
		ac.gameHandle.clearBuffs(player);
	}

	@Override
	public void onConsume() {
	}

	@Override
	public void onBeingDamage(EntityDamageEvent e) {
	}

	@Override
	public void Wake() {
	}
}
