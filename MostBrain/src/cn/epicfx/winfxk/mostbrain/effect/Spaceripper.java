package cn.epicfx.winfxk.mostbrain.effect;

import cn.epicfx.winfxk.mostbrain.EffectItem;
import cn.epicfx.winfxk.mostbrain.game.MostConfig;
import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.level.Location;
import cn.nukkit.level.Sound;

/**
 * 空间撕裂者，攻击会使别人随机传送到游戏区域内的一个位置 </br>
 * 金锭
 *
 * @author Winfxk
 */
public class Spaceripper extends EffectItem {
	private MostConfig mc;

	@Override
	public void setPlayer(Player player) {
		mc = ac.getMostConfig();
		super.setPlayer(player);
	}

	@Override
	public int getID() {
		return 15;
	}

	@Override
	public void onDamage(EntityDamageEvent e) {
		if (Tool.getRand(1, 10) > 3)
			return;
		Entity entity = e.getEntity();
		Location l1 = entity.getLocation();
		Location l = new Location(Tool.getRand(mc.MinX + 1, mc.MaxX - 1), Tool.getRand(mc.MinY + 1, mc.MaxY - 1),
				Tool.getRand(mc.MinZ + 1, mc.MaxZ - 1));
		entity.getLevel().addSound(l1, Sound.MOB_ENDERMEN_STARE);
		entity.teleport(l);
		double s = Math.sqrt(entity.getHealth());
		s = e.getDamage() + (s > 1 ? Tool.getRand(1, s) : s);
		e.setDamage(Tool.objToFloat(s, e.getDamage() + 1));
		gameData.score += Math.abs(l1.x - l.x) + Math.abs(l1.y - l.y) + Math.abs(l1.y - l.y);
		gameData.honor++;
	}
}
