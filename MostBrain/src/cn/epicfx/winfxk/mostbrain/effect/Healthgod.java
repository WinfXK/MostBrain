package cn.epicfx.winfxk.mostbrain.effect;

/**
 * 增加血量上限 </br>
 * 附魔金苹果
 * 
 * @author Winfxk
 */
public class Healthgod extends EffectItem {
	@Override
	public void onConsume() {
		int MaxHealth = player.getMaxHealth();
		float Health = player.getMaxHealth() / player.getHealth();
		player.setMaxHealth(MaxHealth + 10);
		player.setHealth(player.getMaxHealth() / Health);
		gameData.honor++;
		gameData.score += player.getMaxHealth();
	}

	@Override
	public boolean Affiliate() {
		return false;
	}

	@Override
	public int getID() {
		return 466;
	}

	@Override
	public int getDamage() {
		return -1;
	}
}
