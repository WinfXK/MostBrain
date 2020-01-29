package cn.epicfx.winfxk.mostbrain.effect;

import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.Player;
import cn.nukkit.PlayerFood;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageEvent;

/**
 * 自己的前三次攻击将会把被攻击者的饱食度直接清空 </br>
 * 腐肉
 * 
 * @author Winfxk
 */
public class Beggar extends EffectItem {

	@Override
	public int getID() {
		return 367;
	}

	@Override
	public boolean isReDo() {
		return false;
	}

	@Override
	public void onDamage(EntityDamageEvent e) {
		if (i++ > 3) {
			remove();
			return;
		}
		Entity en = e.getEntity();
		if (en instanceof Player) {
			Player player = (Player) en;
			PlayerFood food = player.getFoodData();
			food.setLevel(food.getLevel(), 0);
			gameData.honor += Tool.getRand(1, 2);
			gameData.score += food.getLevel() + food.getFoodSaturationLevel();
		}
	}

}
