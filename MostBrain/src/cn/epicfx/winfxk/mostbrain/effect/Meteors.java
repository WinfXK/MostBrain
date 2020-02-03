package cn.epicfx.winfxk.mostbrain.effect;

import cn.epicfx.winfxk.mostbrain.EffectItem;

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
	public boolean Affiliate() {
		return false;
	}

	@Override
	public void onConsume() {
		super.onConsume();
		handle.giveBuffs(player).giveBuffs(player);
		gameData.honor += gameData.honor / 2;
		gameData.score += gameData.score / 2;
	}
}
