package cn.epicfx.winfxk.mostbrain.effect;

import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.event.entity.EntityDamageEvent;

/**
 * 轮回者，被赋予这个Buff的玩家将会一直经历血量减少后再增多的过程，</br>
 * 在血量减少时防御和攻击都会增加，在血量增多时攻击力加倍 </br>
 * 灵魂沙
 * 
 * @author Winfxk
 */
public class Rebirth extends EffectItem {
	private boolean Up = false;
	private int isUp = 0;

	@Override
	public int getID() {
		return 88;
	}

	@Override
	public void onDamage(EntityDamageEvent e) {
		double i = Tool.getRand(1, 5) / 10;
		float f = e.getDamage();
		e.setDamage(Tool.objToFloat(f + f * i, f));
		gameData.honor++;
		gameData.score += f * i;
	}

	@Override
	public void onBeingDamage(EntityDamageEvent e) {
		double i = Tool.getRand(1, 3) / 10;
		float f = e.getDamage();
		e.setDamage(Tool.objToFloat(f - f * i, f));
		gameData.score += f * i;
	}

	@Override
	public void Wake() {
		if (isUp % 2 != 0)
			return;
		isUp++;
		if (Up) {
			if (player.getHealth() + 1 >= player.getMaxHealth()) {
				player.setHealth(player.getMaxHealth());
				Up = false;
				return;
			}
			player.setHealth(player.getHealth() + 1);
			return;
		}
		if (player.getHealth() - 1 <= 1) {
			Up = true;
			player.setHealth(1);
			return;
		}
		player.setHealth(player.getHealth() - 1);
	}
}
