package cn.epicfx.winfxk.mostbrain.effect;

import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerItemConsumeEvent;

/**
 * 使用后随机获得两个道具 </br>
 * 绿宝石
 * 
 * @author Winfxk
 */
public class Meteors extends EffectItem {

	@Override
	public int getID() {
		return 388;
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
		handle.giveBuffs(player).giveBuffs(player);
	}

	@Override
	public void onBeingDamage(EntityDamageEvent e) {
	}

	@Override
	public void Wake() {
	}
}
