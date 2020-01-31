package cn.epicfx.winfxk.mostbrain.effect;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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
	 * 在玩家特效中删除本特效
	 */
	public void remove() {
		MyPlayer myPlayer = ac.getPlayers(player.getName());
		myPlayer.items.remove(this);
		ac.setPlayers(player, myPlayer);
	}

	/**
	 * 返回物品的最大堆叠数量
	 *
	 * @return
	 */
	public int MaxStack() {
		return 1;
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
				for (EffectItem item3 : Activate.getActivate().getEffecttor().getList())
					if (item2.getId() == item3.getID()
					&& (item3.getDamage() < 0 || item3.getDamage() == item2.getDamage()))
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
		boolean KillB = false;
		MyPlayer myPlayer;
		List<EffectItem> items;
		if (entity instanceof Player) {
			myPlayer = Activate.getActivate().getPlayers(entity.getName());
			if (myPlayer != null && (myPlayer.GameModel || myPlayer.ReadyModel))
				if (myPlayer.items != null && myPlayer.items.size() >= 0) {
					if (e instanceof EntityDamageByEntityEvent) {
						entity = ((EntityDamageByEntityEvent) e).getDamager();
						if (entity instanceof Player) {
							MyPlayer myPlayer2 = Activate.getActivate().getPlayers(entity.getName());
							if (myPlayer2 != null && (myPlayer2.GameModel || myPlayer2.ReadyModel))
								if (myPlayer2.items != null && myPlayer2.items.size() >= 0)
									if (Duration.between(myPlayer2.RespawnTime, Instant.now()).toMillis() <= Activate
									.getActivate().getConfig().getInt("无敌时间")) {
										e.setCancelled();
										return;
									}
						}
					}
					e.setDamage(e.getDamage() * Activate.getActivate().getConfig().getInt("倍率加成"));
					KillB = true;
					items = new ArrayList<>(myPlayer.items);
					for (EffectItem item : items) {
						if (e.isCancelled())
							break;
						item.allotDamageEvent(e);
					}
				}
		}
		if (e instanceof EntityDamageByEntityEvent) {
			entity = ((EntityDamageByEntityEvent) e).getDamager();
			if (entity instanceof Player) {
				myPlayer = Activate.getActivate().getPlayers(entity.getName());
				if (myPlayer != null && (myPlayer.GameModel || myPlayer.ReadyModel))
					if (myPlayer.items != null && myPlayer.items.size() >= 0) {
						if (Duration.between(myPlayer.RespawnTime, Instant.now()).toMillis() <= Activate.getActivate()
								.getConfig().getInt("无敌时间")) {
							e.setCancelled();
							return;
						}
						if (!KillB) {
							e.setDamage(e.getDamage() * Activate.getActivate().getConfig().getInt("倍率加成"));
							KillB = true;
						}
						items = new ArrayList<>(myPlayer.items);
						for (EffectItem item : items) {
							if (e.isCancelled())
								break;
							item.allotDamageEvent(e);
						}
					}
			}
		}
	}

	/**
	 * 分配伤害事件
	 *
	 * @param e
	 */
	public void allotDamageEvent(EntityDamageEvent e) {
		if (e instanceof EntityDamageByEntityEvent) {
			Entity entity = ((EntityDamageByEntityEvent) e).getDamager();
			if (isPlayer(entity)) {
				Item item = ((Player) entity).getInventory().getItemInHand();
				CompoundTag nbt = item.getNamedTag();
				nbt = nbt == null ? new CompoundTag() : nbt;
				if (nbt.getString(ac.getMostBrain().getName()) != null) {
					int ak = nbt.getInt("Ak");
					e.setDamage(ak <= 0 ? e.getDamage() : ak);
				}
				if (!e.isCancelled())
					onDamage(e);
			}
		}
		if (isPlayer(e.getEntity()) && !e.isCancelled())
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
		String s = getMeaage("Hint", player);
		if (s.length() > 15)
			s = getString("", s);
		return s;
	}

	public String getString(String Max, String s) {
		if (s.length() > 15) {
			String string = s.substring(0, s.length() <= 15 ? s.length() - 1 : 14);
			String ss = "xxx1&^%$" + s;
			String sss = "xxx1&^%$" + string;
			s = ss.replace(sss, "");
			Max += (Max.isEmpty() ? "" : "\n") + string;
			return getString(Max, s);
		}
		return Max += (Max.isEmpty() ? "" : "\n") + s;
	}

	/**
	 * 道具消耗时调用
	 */
	public void onConsume() {
		gameData.honor++;
		gameData.score += 5 + Tool.getRand(1, getID()) * 2;
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
		gameData.score += Tool.getRand(1, ac.gameHandle.getGamePlayers().size());
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
		gameData.score += e.getItem().getId();
	}

	/**
	 * 实体损伤事件<自己造成伤害>
	 *
	 * @param e
	 */
	public void onDamage(EntityDamageEvent e) {
		gameData.honor++;
		Entity entity = e.getEntity();
		if (entity.getHealth() <= e.getDamage()) {
			gameData.honor += 2;
			gameData.score += e.getDamage() * ac.gameHandle.getGamePlayers().size() + entity.getHealth();
		} else
			gameData.score += e.getDamage() + ac.gameHandle.getGamePlayers().size();
	}

	/**
	 * 实体损伤事件<自己被伤害>
	 *
	 * @param e
	 */
	public void onBeingDamage(EntityDamageEvent e) {
		if (Tool.getRand(1, 3) == 1)
			gameData.honor--;
		if (player.getHealth() <= e.getDamage()) {
			gameData.score -= e.getDamage() + player.getMaxHealth();
			return;
		}
		if (Tool.getRand(1, 2) == 1)
			gameData.score -= e.getDamage();
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
