package cn.epicfx.winfxk.mostbrain.effect;

import cn.epicfx.winfxk.mostbrain.EffectItem;
import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.potion.Effect;

/**
 * 给自己加速</br>
 * 红石粉
 * 
 * @author Winfxk
 */
public class Expedite extends EffectItem {

	@Override
	public int getID() {
		return 55;
	}

	@Override
	public boolean isReDo() {
		return false;
	}

	@Override
	public void onConsume() {
		super.onConsume();
		i = 10;
	}

	@Override
	public void Wake() {
		super.Wake();
		if (i++ < 10)
			return;
		i = 0;
		Effect effect = Effect.getEffect(1);
		effect.setDuration(280);
		gameData.score += Tool.getRand(1, getID());
		effect.setColor(Tool.getRand(1, 255), Tool.getRand(1, 255), Tool.getRand(1, 255));
		player.addEffect(effect);
	}
}
