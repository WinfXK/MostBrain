package cn.epicfx.winfxk.mostbrain.effect;

import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.event.entity.EntityDamageEvent;

/**
 * 自己被攻击时有概率闪避攻击</br>
 * 末影珍珠
 * 
 * @author Winfxk
 */
public class Dodging extends EffectItem {
	public static final int MinGing = 0, MaxGing = 5;

	@Override
	public int getID() {
		return 368;
	}

	@Override
	public void onBeingDamage(EntityDamageEvent e) {
		if (Tool.getRand(MinGing, MaxGing) != 1 || e.isCancelled())
			return;
		gameData.honor += Tool.getRand(1, 2);
		gameData.score += e.getDamage();
		e.setCancelled();
	}
}
