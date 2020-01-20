package cn.epicfx.winfxk.mostbrain.effect;

import cn.nukkit.PlayerFood;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerItemConsumeEvent;

/**
 * 自己不会饥饿</br>
 * 小麦
 * 
 * @author Winfxk
 */
public class Satiate extends EffectItem {
	private PlayerFood food;

	@Override
	public int getID() {
		return 59;
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
		food = player.getFoodData();
	}

	@Override
	public void onBeingDamage(EntityDamageEvent e) {
	}

	@Override
	public void Wake() {
		food.setLevel(food.getLevel() < 1 ? 1 : food.getLevel(),
				food.getFoodSaturationLevel() < 1 ? 1 : food.getFoodSaturationLevel());
	}
}
