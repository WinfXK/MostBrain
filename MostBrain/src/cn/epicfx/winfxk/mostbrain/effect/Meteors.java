package cn.epicfx.winfxk.mostbrain.effect;

/**
 * 使用后随机获得两个道具 </br>
 * 绿宝石
 * 
 * @author Winfxk
 */
public class Meteors extends EffectItem {

	@Override
	public int getID() {
		return 388;
	}

	@Override
	public int getDamage() {
		return -1;
	}

	@Override
	public void onConsume() {
		super.onConsume();
		handle.giveBuffs(player).giveBuffs(player);
		gameData.honor += 2;
		gameData.score += gameData.score / 2;
	}
}
