package cn.epicfx.winfxk.mostbrain.effect;

import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageEvent;

/**
 * 第一次攻击能完全克隆对手的所有特效</br>
 * 共鸣果
 * 
 * @author Winfxk
 */
public class Theft extends EffectItem {

	@Override
	public int getID() {
		return 432;
	}

	@Override
	public boolean isReDo() {
		return false;
	}

	@Override
	public void onDamage(EntityDamageEvent e) {
		if (i++ > 1) {
			remove();
			return;
		}
		Entity entity = e.getEntity();
		if (!(entity instanceof Player))
			return;
		Player player2 = (Player) entity;
		if (!handle.getGamePlayers().contains(player2))
			return;
		List<EffectItem> list = ac.getPlayers(player2.getName()).items;
		for (EffectItem item : list)
			handle.giveBuffs(player, item);
		gameData.honor++;
		gameData.score += list.size() * 2;
	}
}
