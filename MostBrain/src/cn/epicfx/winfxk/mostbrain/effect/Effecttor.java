package cn.epicfx.winfxk.mostbrain.effect;

import cn.epicfx.winfxk.mostbrain.Activate;
import cn.epicfx.winfxk.mostbrain.MyPlayer;
import cn.epicfx.winfxk.mostbrain.game.GameHandle;
import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * @author Winfxk
 */
public class Effecttor {

	private EffectItem[] list;
	private Activate ac;

	public EffectItem[] getList() {
		return list;
	}

	public Effecttor(GameHandle gameHandle) {
		load();
		ac = Activate.getActivate();
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
		return item2;
	}

	protected void load() {
		list = new EffectItem[] { new Accumulation(), new Beggar(), new Brambles(), new Combustion(), new Deathgodson(),
				new Dieattack(), new Dodging(), new Eternalife(), new Expedite(), new Ferocity(), new Firegod(),
				new Flying(), new Formatting(), new Healthgod(), new Highlytoxic(), new Meteors(), new Philanthropist(),
				new Protection(), new Satiate(), new Slowness(), new Theft(), new Vertigo() };
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
