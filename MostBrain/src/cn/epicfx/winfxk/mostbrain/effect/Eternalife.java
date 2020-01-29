package cn.epicfx.winfxk.mostbrain.effect;

import cn.epicfx.winfxk.mostbrain.tool.Tool;

/**
 * 每隔一段时间增加一点血量</br>
 * 不死图腾
 * 
 * @author Winfxk
 */
public class Eternalife extends EffectItem {
	@Override
	public int getID() {
		return 450;
	}

	@Override
	public void Wake() {
		if (player.getHealth() >= player.getMaxHealth() || i++ < 3)
			return;
		i = 0;
		if (Tool.getRand(1, 20) == 1)
			gameData.honor++;
		float f = player.getHealth() + 1;
		gameData.score += player.getHealth();
		player.setMaxHealth((int) (f > player.getMaxHealth() ? player.getMaxHealth() : f));
	}
}
