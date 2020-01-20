package cn.epicfx.winfxk.mostbrain.effect;

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
	public void Wake() {
		if (player.getHealth() >= player.getMaxHealth() || i++ < 3)
			return;
		i = 0;
		float f = player.getHealth() + 1;
		gameData.score++;
		player.setMaxHealth((int) (f > player.getMaxHealth() ? player.getMaxHealth() : f));
	}
}
