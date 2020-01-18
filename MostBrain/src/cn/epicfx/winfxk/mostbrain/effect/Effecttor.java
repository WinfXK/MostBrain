package cn.epicfx.winfxk.mostbrain.effect;

import cn.epicfx.winfxk.mostbrain.Activate;
import cn.epicfx.winfxk.mostbrain.game.GameHandle;
import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * @author Winfxk
 */
public class Effecttor {

	private EffectItem[] list;

	public EffectItem[] getList() {
		return list;
	}

	public Effecttor(GameHandle gameHandle) {
		load();
	}

	public Item getItem(EffectItem item) {
		Item item2 = new Item(item.getID(), item.getDamage() < 0 ? 0 : item.getDamage(), 1);
		item2.setCustomName(Tool.getRandColor() + item.getName());
		item2.setLore(item.Function());
		item2.addEnchantment(item.getEnchantment());
		CompoundTag nbt = item2.getNamedTag();
		nbt.putString(Activate.getActivate().getMostBrain().getName(), Activate.getActivate().getMostBrain().getName());
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
}
