package cn.epicfx.winfxk.mostbrain.effect;

import cn.epicfx.winfxk.mostbrain.EffectItem;
import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.event.entity.EntityDamageEvent;

/**
 * 阻挡一次必死攻击 </br>
 * 下界之星
 * 
 * @author Winfxk
 */
public class Deathgodson extends EffectItem {
	@Override
	public int getID() {
		return 399;
	}

	@Override
	public void onBeingDamage(EntityDamageEvent e) {
		if (i++ < 2 && ((int) (player.getHealth() - e.getDamage())) <= 0) {
			e.setCancelled();
			player.setHealth(player.getMaxHealth() / 2);
			gameData.honor += Tool.getRand(1, 10);
			gameData.score *= 2;
		}
	}
}
