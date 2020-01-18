package cn.epicfx.winfxk.mostbrain.effect;

import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerItemConsumeEvent;

/**
 * 增加血量上限 </br>
 * 附魔金苹果
 * 
 * @author Winfxk
 */
public class Healthgod extends EffectItem {
	@Override
	public void onConsume() {
		int MaxHealth = player.getMaxHealth();
		float Health = player.getMaxHealth() / player.getHealth();
		player.setMaxHealth(MaxHealth + 10);
		player.setHealth(player.getMaxHealth() / Health);
	}

	@Override
	public int getID() {
		return 466;
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
	public void onBeingDamage(EntityDamageEvent e) {
	}

	@Override
	public void Wake() {
	}

}
