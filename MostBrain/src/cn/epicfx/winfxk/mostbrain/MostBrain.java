package cn.epicfx.winfxk.mostbrain;

import cn.nukkit.plugin.PluginBase;

/**
 * @author Winfxk
 */
public class MostBrain extends PluginBase {
	private static Activate ac;

	@Override
	public void onEnable() {
		ac = new Activate(this);
		super.onEnable();
	}

	@Override
	public void onLoad() {
		getLogger().info(getName() + " start load..");
		if (!getDataFolder().exists())
			getDataFolder().mkdirs();
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}
}
