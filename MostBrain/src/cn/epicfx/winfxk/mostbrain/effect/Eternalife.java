package cn.epicfx.winfxk.mostbrain.effect;

import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerItemConsumeEvent;

/**
 * 每隔一段时间增加一点血量</br>
 * 不死图腾
 * 
 * @author Winfxk
 */
public class Eternalife extends EffectItem {
	@Override
	public void onConsume() {
	}

	@Override
	public int getID() {
		return 450;
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
		if (player.getHealth() >= player.getMaxHealth() || i++ < 3)
			return;
		i = 0;
		float f = player.getHealth() + 1;
		player.setMaxHealth((int) (f > player.getMaxHealth() ? player.getMaxHealth() : f));
	}

}
