package cn.epicfx.winfxk.mostbrain.effect;

import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.potion.Effect;

/**
 * 使用后自己会漂浮 </br>
 * 羽毛
 * 
 * @author Winfxk
 */
public class Flying extends EffectItem {

	@Override
	public int getID() {
		return 288;
	}

	@Override
	public int getDamage() {
		return -1;
	}

	@Override
	public void Wake() {
		if (i++ < 10)
			return;
		i = 0;
		Effect effect = Effect.getEffect(24);
		effect.setDuration(280);
		effect.setColor(Tool.getRand(1, 255), Tool.getRand(1, 255), Tool.getRand(1, 255));
		player.addEffect(effect);
		gameData.score++;
		gameData.honor--;
	}
}
