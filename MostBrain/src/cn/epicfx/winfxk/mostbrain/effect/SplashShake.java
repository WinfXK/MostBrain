package cn.epicfx.winfxk.mostbrain.effect;

import cn.epicfx.winfxk.mostbrain.EffectItem;
import cn.epicfx.winfxk.mostbrain.MyPlayer;
import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.level.Location;
import cn.nukkit.level.Sound;

/**
 * 在攻击时，能和目标互换位置 </br>
 * 末影之眼
 * 
 * @author Winfxk
 */
public class SplashShake extends EffectItem {

	@Override
	public int getID() {
		return 381;
	}

	@Override
	public boolean isReDo() {
		return false;
	}

	@Override
	public void onDamage(EntityDamageEvent e) {
		if (!(e instanceof EntityDamageByEntityEvent) || Tool.getRand(1, 3) != 1)
			return;
		gameData.honor++;
		Entity entity2 = e.getEntity();
		Entity entity1 = ((EntityDamageByEntityEvent) e).getDamager();
		Location l1 = entity1.getLocation();
		Location l2 = entity2.getLocation();
		double xZ = Math.abs(l1.x - l2.x) + Math.abs(l1.y - l2.y) + Math.abs(l1.z - l2.z);
		gameData.score += xZ * 2;
		if (entity2 instanceof Player) {
			MyPlayer myPlayer = ac.getPlayers(entity2.getName());
			if (myPlayer != null && myPlayer.GameModel && myPlayer.gameData != null) {
				myPlayer.gameData.score -= xZ;
				if (Tool.getRand(1, 3) == 1)
					myPlayer.gameData.honor--;
			}
		}
		entity1.teleport(l2);
		entity2.teleport(l1);
		entity1.getLevel().addSound(l1, Sound.RANDOM_ENDERCHESTCLOSED);
		entity2.getLevel().addSound(l2, Sound.RANDOM_ENDERCHESTCLOSED);
	}
}
