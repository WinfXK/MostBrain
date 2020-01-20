package cn.epicfx.winfxk.mostbrain.effect;

import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerItemConsumeEvent;

/**
 * 第一次攻击能完全克隆对手的所有特效</br>
 * 共鸣果
 * 
 * @author Winfxk
 */
public class Theft extends EffectItem {

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
		Player player2 = (Player) entity;
		if (!handle.getGamePlayers().contains(player2))
			return;
		List<EffectItem> list = ac.getPlayers(player2.getName()).items;
		for (EffectItem item : list)
			handle.giveBuffs(player2, item);
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
