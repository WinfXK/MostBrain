package cn.epicfx.winfxk.mostbrain.effect;

import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageEvent;

/**
 * 吸血 </br>
 * 蜘蛛眼
 * 
 * @author Winfxk
 */
public class Suckblood extends EffectItem {

	@Override
	public int getID() {
		return -1;
	}

	@Override
	public int getDamage() {
		return 375;
	}

	@Override
	public void onDamage(EntityDamageEvent e) {
		Entity entity = e.getEntity();
		if (player.getHealth() >= player.getMaxHealth() || Tool.getRand(1, 5) != 1
				|| entity.getHealth() <= e.getDamage() || entity.getHealth() / 10 <= 1)
			return;
		int h = Tool.getRand(1, (int) (entity.getHealth() / 10));
		entity.setHealth(entity.getHealth() - h);
		if (h + player.getHealth() < player.getMaxHealth())
			player.setHealth(h + player.getHealth());
	}
}
