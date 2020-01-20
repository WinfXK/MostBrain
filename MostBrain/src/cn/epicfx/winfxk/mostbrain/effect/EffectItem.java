package cn.epicfx.winfxk.mostbrain.effect;

import cn.epicfx.winfxk.mostbrain.Activate;
import cn.epicfx.winfxk.mostbrain.MyPlayer;
import cn.epicfx.winfxk.mostbrain.game.GameHandle;
import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.Player;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerItemConsumeEvent;
import cn.nukkit.item.enchantment.Enchantment;

/**
 * @author Winfxk
 */
public abstract class EffectItem {
	public Player player;
	public MyPlayer myPlayer;
	public Activate ac;
	private static final String SystemKey = "EffectItem";
	public int i = 0;
	public GameHandle handle;

	public void setPlayer(Player player) {
		this.player = player;
		ac = Activate.getActivate();
		handle = ac.gameHandle;
		myPlayer = ac.getPlayers(player.getName());
	}

	/**
	 * 道具的功能介绍
	 * 
	 * @return
	 */
	public String getFunction() {
		return getMeaage("Hint", player);
	}

	/**
	 * 道具消耗时调用
	 */
	public abstract void onConsume();

	/**
	 * 道具的名称
	 * 
	 * @return
	 */
	public String getName() {
		return getMeaage("Name", player);
	}

	public abstract void Wake();

	/**
	 * 对应物品的ID
	 * 
	 * @return
	 */
	public abstract int getID();

	/***
	 * 对应物品的特殊值，小于零时忽略
	 * 
	 * @return
	 */
	public abstract int getDamage();

	/**
	 * 项目消耗事件
	 * 
	 * @param e
	 */
	public abstract void onItemConsume(PlayerItemConsumeEvent e);

	/**
	 * 实体损伤事件<自己造成伤害>
	 * 
	 * @param e
	 */
	public abstract void onDamage(EntityDamageEvent e);

	/**
	 * 实体损伤事件<自己被伤害>
	 * 
	 * @param e
	 */
	public abstract void onBeingDamage(EntityDamageEvent e);

	/**
	 * 
	 * @param key
	 * @return
	 */
	public String getMeaage(String Key) {
		return getMeaage(getEffectName(), Key, null);
	}

	/**
	 * 
	 * @param key
	 * @param player
	 * @return
	 */
	public String getMeaage(String Key, Player player) {
		return getMeaage(getEffectName(), Key, player);
	}

	/**
	 * 
	 * @param Key
	 * @param Sun
	 * @return
	 */
	public String getMeaage(String Key, String Sun) {
		return getMeaage(Key, Sun, null);
	}

	/**
	 * 
	 * @param Key
	 * @param Sun
	 * @param player
	 * @return
	 */
	public String getMeaage(String Key, String Sun, Player player) {
		return player == null ? ac.getMessage().getSun(SystemKey, Key, Sun)
				: ac.getMessage().getSun(SystemKey, Key, Sun, player);
	}

	/**
	 * 获取到类名
	 * 
	 * @return
	 */
	public String getEffectName() {
		return getClass().getSimpleName();
	}

	/**
	 * 返回一个物品附魔
	 * 
	 * @return
	 */
	public Enchantment getEnchantment() {
		return Enchantment.get(Tool.getRand(0, 34));
	}
}
