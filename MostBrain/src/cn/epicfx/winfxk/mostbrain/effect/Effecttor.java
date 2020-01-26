package cn.epicfx.winfxk.mostbrain.effect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.epicfx.winfxk.mostbrain.Activate;
import cn.epicfx.winfxk.mostbrain.MyPlayer;
import cn.epicfx.winfxk.mostbrain.game.GameHandle;
import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * @author Winfxk
 */
public class Effecttor {
	private List<EffectItem> list;
	private Activate ac;
	private List<EffectItem> usEffectItems = new ArrayList<>();
	public static final int[] Ak = { 268, 272, 276, 283, 267, 258, 271, 275, 279, 286 };
	public static final int[] AkEn = { 9, 12, 13, 29 };
	private static final EffectItem[] defEffect = { new Accumulation(), new Beggar(), new Brambles(), new Combustion(),
			new Deathgodson(), new Dieattack(), new Dodging(), new Eternalife(), new Expedite(), new Ferocity(),
			new Firegod(), new Flying(), new Formatting(), new Healthgod(), new Highlytoxic(), new Meteors(),
			new Philanthropist(), new Protection(), new Suckblood(), new Burst(), new Satiate(), new Slowness(),
			new Theft(), new Vertigo(), new Stamp() };

	public List<EffectItem> getList() {
		list = list == null ? Arrays.asList(defEffect) : list;
		return list;
	}

	/**
	 * 添加新的特效
	 * 
	 * @param item
	 * @return
	 */
	public Effecttor makeEffecttor(EffectItem item) {
		if (list.contains(item) || usEffectItems.contains(item))
			return this;
		usEffectItems.add(item);
		return this;
	}

	public Effecttor(GameHandle gameHandle) {
		load();
		ac = Activate.getActivate();
	}

	/**
	 * 给玩家随机一个武器
	 * 
	 * @return
	 */
	public Item getAK() {
		Item item = new Item(Ak[Tool.getRand(0, Ak.length - 1)]);
		CompoundTag nbt = item.getNamedTag();
		nbt = nbt == null ? new CompoundTag() : nbt;
		Enchantment en = Enchantment.get(AkEn[Tool.getRand(0, AkEn.length - 1)]);
		en.setLevel(en.getMaxLevel() > 1 ? Tool.getRand(1, en.getMaxLevel()) : en.getMaxLevel());
		item.addEnchantment(en);
		item.addEnchantment(Enchantment.getEnchantment(28));
		item.addEnchantment();
		nbt.putString(ac.getMostBrain().getName(), ac.getMostBrain().getName());
		int aks = Tool.getRand(1, 5);
		int abs = item.getAttackDamage();
		abs = abs <= 0 ? 1 : abs;
		nbt.putInt("Ak", aks * abs);
		item.setCompoundTag(nbt);
		item.setLore(ac.getMessage().getSon("Game", "武器介绍", new String[] { "{Rate}", "{Damage}", "{MaxDamage}" },
				new Object[] { aks, abs, aks * abs }));
		return item;
	}

	public Item getItem(EffectItem item) {
		Item item2 = new Item(item.getID(), item.getDamage() < 0 ? 0 : item.getDamage(), 1);
		item2.setCustomName(Tool.getRandColor() + item.getName());
		item2.setLore(item.getFunction());
		item2.addEnchantment(item.getEnchantment());
		CompoundTag nbt = item2.getNamedTag();
		nbt = nbt == null ? new CompoundTag() : nbt;
		nbt.putString(ac.getMostBrain().getName(), ac.getMostBrain().getName());
		nbt.putString("Name", item.getName());
		item2.setCompoundTag(nbt);
		item2.setCount(Tool.getRand(1, item.MaxStack()));
		return item2;
	}

	protected void load() {
		list = Arrays.asList(defEffect);
		for (EffectItem item : usEffectItems)
			list.add(item);
	}

	/**
	 * 当玩家使用了一个物品，判断是否是游戏物品
	 * 
	 * @param player
	 * @param item
	 * @return
	 */
	public Effecttor putBuff(Player player, Item item) {
		if (!ac.gameHandle.getGamePlayers().contains(player))
			return this;
		CompoundTag nbt = item.getNamedTag();
		if (nbt == null || !nbt.getString(ac.getMostBrain().getName()).equals(ac.getMostBrain().getName()))
			return this;
		for (EffectItem effectItem : list)
			if (effectItem.getID() == item.getId()
					&& (effectItem.getDamage() < 0 || effectItem.getDamage() == item.getDamage()))
				putBuff(player, effectItem);
		return this;
	}

	/**
	 * 给玩家创建Buff
	 * 
	 * @param player
	 * @param item
	 * @return
	 */
	public Effecttor putBuff(Player player, EffectItem item) {
		if (!ac.gameHandle.getGamePlayers().contains(player))
			return this;
		try {
			EffectItem item2 = item.getClass().newInstance();
			item2.setPlayer(player);
			MyPlayer myPlayer = ac.getPlayers(player.getName());
			myPlayer.items.add(item2);
			ac.setPlayers(player, myPlayer);
			item2.onConsume();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
}
