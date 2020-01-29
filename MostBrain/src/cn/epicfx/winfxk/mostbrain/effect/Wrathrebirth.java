package cn.epicfx.winfxk.mostbrain.effect;

import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.event.entity.EntityDamageEvent;

/**
 * 死亡复活后的前三次攻击将会获得攻击加成 </br>
 * 甜菜根
 *
 * @author Winfxk
 */
public class Wrathrebirth extends EffectItem {
	private boolean isSB = false;
	private int SB = 3;

	@Override
	public int getID() {
		return 457;
	}

	@Override
	public void onDamage(EntityDamageEvent e) {
		if (!isSB || e.isCancelled() || SB-- < 0)
			return;
		float d = e.getDamage();
		e.setDamage(d * (Tool.getRand(5, 9) / 10));
		gameData.honor++;
		gameData.score += d + e.getDamage();
	}

	@Override
	public void onBeingDamage(EntityDamageEvent e) {
		if (player.getHealth() > e.getDamage() || e.isCancelled())
			return;
		isSB = true;
		SB = 3;
	}
}
