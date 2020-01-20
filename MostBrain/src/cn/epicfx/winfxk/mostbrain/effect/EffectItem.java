package cn.epicfx.winfxk.mostbrain.effect;

import java.util.ArrayList;

import cn.epicfx.winfxk.mostbrain.Activate;
import cn.epicfx.winfxk.mostbrain.MyPlayer;
import cn.epicfx.winfxk.mostbrain.game.GameData;
import cn.epicfx.winfxk.mostbrain.game.GameHandle;
import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerItemConsumeEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * @author Winfxk
 */
public abstract class EffectItem {
	public Activate ac = Activate.getActivate();
	public Player player;
	public MyPlayer myPlayer;
	public GameHandle handle;
	public GameData gameData;
	public int i = 0;
	private static final String SystemKey = "EffectItem";

	/**
	 * 分配玩家对象
	 * 
	 * @param player
	 */
	public void setPlayer(Player player) {
		this.player = player;
		ac = Activate.getActivate();
		handle = ac.gameHandle;
		myPlayer = ac.getPlayers(player.getName());
		gameData = new GameData();
	}

	/**
	 * 是否加入玩家特效列表
	 * 
	 * @return
	 */
	public boolean Affiliate() {
		return true;
	}

	/**
	 * 是否允许重复
	 * 
	 * @return
	 */
	public boolean isReDo() {
		return true;
	}

	/**
	 * 分配玩家物品使用处理事件
	 * 
	 * @param e
	 */
	public static void receiveItemConsume(PlayerItemConsumeEvent e) {
		Player player = e.getPlayer();
		MyPlayer myPlayer = Activate.getActivate().getPlayers(player.getName());
		if (myPlayer == null)
			return;
		boolean isOK = false;
		if (myPlayer.GameModel || myPlayer.ReadyModel) {
			Item item2 = e.getItem();
			CompoundTag nbt = item2.getNamedTag();
			nbt = nbt == null ? new CompoundTag() : nbt;
			if (nbt.getString(Activate.getActivate().getMostBrain().getName()) != null) {
				for (EffectItem item3 : Activate.getActivate().getEffecttor().getList()) {
					if (item2.getId() == item3.getID()
							&& (item3.getDamage() < 0 || item3.getDamage() == item2.getDamage())) {
						try {
							EffectItem item4 = item3.getClass().newInstance();
							myPlayer.items = myPlayer.items == null ? new ArrayList<>() : myPlayer.items;
							item4.setPlayer(player);
							if (item4.Affiliate())
								if (!item4.isReDo()) {
									boolean isOKb = false;
									for (EffectItem item5 : myPlayer.items)
										if (isOKb = item5.getName().equals(item4.getName()))
											break;
									if (!isOKb)
										myPlayer.items.add(item4);
								} else
									myPlayer.items.add(item4);
							item4.onConsume();
							Activate.getActivate().setPlayers(player, myPlayer);
							if (item2.getCount() <= 1)
								item2 = new Item(0, 0);
							else
								item2.setCount(item2.getCount() - 1);
							player.getInventory().setItemInHand(item2);
							isOK = true;
							player.sendMessage(Activate.getActivate().getMessage().getSon("Game", "使用道具",
									new String[] { "{Player}", "{Money}", "{Buff}" }, new Object[] { player.getName(),
											MyPlayer.getMoney(player.getName()), item4.getName() }));
							e.setCancelled();
							break;
						} catch (InstantiationException | IllegalAccessException e1) {
							e1.printStackTrace();
						}
					}
				}
				return;
			}
			if (!isOK && myPlayer.items.size() > 0)
				for (EffectItem item : myPlayer.items)
					item.onItemConsume(e);
		}
	}

	/**
	 * 发回处理
	 * 
	 * @param e
	 */
	public static void receiveDamage(EntityDamageEvent e) {
		Entity entity = e.getEntity();
		MyPlayer myPlayer;
		if (entity instanceof Player) {
			myPlayer = Activate.getActivate().getPlayers(entity.getName());
			if (myPlayer.GameModel || myPlayer.ReadyModel)
				if (myPlayer.items.size() > 0)
					for (EffectItem item : myPlayer.items)
						item.allotDamageEvent(e);
		}
		if (e instanceof EntityDamageByEntityEvent) {
			entity = ((EntityDamageByEntityEvent) e).getDamager();
			if (entity instanceof Player) {
				myPlayer = Activate.getActivate().getPlayers(entity.getName());
				if (myPlayer.GameModel || myPlayer.ReadyModel)
					if (myPlayer.items.size() > 0)
						for (EffectItem item : myPlayer.items)
							item.allotDamageEvent(e);
			}
		}
	}

	/**
	 * 分配伤害事件
	 * 
	 * @param e
	 */
	public void allotDamageEvent(EntityDamageEvent e) {
		Entity entity = ((EntityDamageByEntityEvent) e).getDamager();
		if (e instanceof EntityDamageByEntityEvent && isPlayer(entity)) {
			Item item = ((Player) entity).getInventory().getItemInHand();
			CompoundTag nbt = item.getNamedTag();
			nbt = nbt == null ? new CompoundTag() : nbt;
			if (nbt.getString(ac.getMostBrain().getName()) != null) {
				int ak = nbt.getInt("Ak");
				e.setDamage(ak >= 0 ? e.getDamage() : ak);
			}
			onDamage(e);
		}
		if (isPlayer(e.getEntity()))
			onBeingDamage(e);
	}

	/**
	 * 是否是游戏内的玩家触发的事件
	 * 
	 * @param entity
	 * @return
	 */
	private boolean isPlayer(Entity entity) {
		if (!(entity instanceof Player))
			return false;
		if (!ac.isPlayers(entity.getName()))
			return false;
		MyPlayer myPlayer = ac.getPlayers(entity.getName());
		if (!myPlayer.GameModel && !myPlayer.ReadyModel)
			return false;
		return myPlayer.getPlayer().getName().equals(player.getName());
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
	public void onConsume() {
		gameData.honor++;
		gameData.score += 5;
	}

	/**
	 * 道具的名称
	 * 
	 * @return
	 */
	public String getName() {
		return getMeaage("Name", player);
	}

	/**
	 * 返回附属文本
	 * 
	 * @return
	 */
	public String getText() {
		return getMeaage("Text", player);
	}

	public void Wake() {
		gameData.score++;
	}

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
	public int getDamage() {
		return -1;
	}

	/**
	 * 项目消耗事件
	 * 
	 * @param e
	 */
	public void onItemConsume(PlayerItemConsumeEvent e) {
		gameData.score++;
	}

	/**
	 * 实体损伤事件<自己造成伤害>
	 * 
	 * @param e
	 */
	public void onDamage(EntityDamageEvent e) {
		gameData.honor++;
		gameData.score += e.getDamage() / 2;
	}

	/**
	 * 实体损伤事件<自己被伤害>
	 * 
	 * @param e
	 */
	public void onBeingDamage(EntityDamageEvent e) {
		gameData.honor -= 2;
		gameData.score -= e.getDamage() * 2;
	}

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
