package cn.epicfx.winfxk.mostbrain.effect;

import cn.epicfx.winfxk.mostbrain.MyPlayer;
import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.level.Sound;

/**
 * 在攻击时概率触发，若触发成功，则随机获得一个对方的Buff，若对方无Buff，</br>
 * 则本身消失一个Buff病给予被攻击的玩家，若双方均无Buff，则随机一方死亡，</br>
 * 未死亡一方获得一个随机BUff，若被攻击者未加入游戏，攻击者即刻死亡！ </br>
 * 合成肽
 * 
 * @author Winfxk
 */
public class Afterfactory extends EffectItem {

	@Override
	public int getID() {
		return 58;
	}

	@Override
	public boolean isReDo() {
		return false;
	}

	@Override
	public void onDamage(EntityDamageEvent e) {
		if (!(e instanceof EntityDamageByEntityEvent) || Tool.getRand(1, 3) != 3)
			return;
		Entity entity = ((EntityDamageByEntityEvent) e).getDamager();
		if (!(entity instanceof Player))
			return;
		Player player2 = (Player) entity;
		MyPlayer myPlayer = ac.getPlayers(entity.getName());
		if (myPlayer == null || !myPlayer.GameModel || myPlayer.items == null) {
			player.sendMessage(getText());
			player.getLevel().addSound(player.getLocation(), Sound.MOB_ZOMBIE_DEATH);
			player.kill();
			gameData.honor--;
			gameData.score -= player2.getHealth() + player.getHealth();
			if (myPlayer.gameData != null) {
				myPlayer.gameData.honor++;
				myPlayer.gameData.score += player2.getHealth() + player.getHealth();
			}
			e.setDamage(0);
			return;
		}
		if (myPlayer.items.size() <= 0 && this.myPlayer.items.size() <= 1) {
			if (Tool.getRand(1, 2) == 1) {
				player.sendMessage(getText());
				player.kill();
				gameData.honor--;
				gameData.score -= player2.getHealth() + player.getHealth();
				if (myPlayer.gameData != null) {
					myPlayer.gameData.honor++;
					myPlayer.gameData.score += player2.getHealth() + player.getHealth();
				}
				player.getLevel().addSound(player.getLocation(), Sound.MOB_ZOMBIE_DEATH);
				player2.getInventory().addItem(ac.gameHandle.getItem());
				return;
			} else {
				player2.sendMessage(getText());
				player2.kill();
				if (myPlayer.gameData != null) {
					myPlayer.gameData.honor--;
					myPlayer.gameData.score -= player2.getHealth() + player.getHealth();
				}
				gameData.honor++;
				gameData.score += player2.getHealth();
				player2.getLevel().addSound(player2.getLocation(), Sound.MOB_ZOMBIE_DEATH);
				player.getInventory().addItem(ac.gameHandle.getItem());
				return;
			}
		}
		if (myPlayer.items.size() <= 0 && this.myPlayer.items.size() > 1) {
			int Ik = Tool.getRand(0, this.myPlayer.items.size() - 1);
			player2.getInventory().addItem(ac.getEffecttor().getItem(this.myPlayer.items.get(Ik)));
			this.myPlayer.items.remove(Ik);
			ac.setPlayers(this.myPlayer.getPlayer(), this.myPlayer);
			player.sendMessage(getText());
			gameData.honor--;
			gameData.score -= player2.getHealth() + myPlayer.items.size();
			player.getLevel().addSound(player.getLocation(), Sound.MOB_VILLAGER_DEATH);
			return;
		}
		gameData.honor++;
		gameData.score += player2.getHealth();
		player2.sendMessage(getText());
		player2.getLevel().addSound(player2.getLocation(), Sound.MOB_VILLAGER_DEATH);
		player.getInventory()
				.addItem(ac.getEffecttor().getItem(myPlayer.items.get(Tool.getRand(0, myPlayer.items.size() - 1))));
	}
}
