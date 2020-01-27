package cn.epicfx.winfxk.mostbrain.effect;

import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.event.entity.EntityDamageEvent;

/**
 * 被攻击时会呐喊 </br>
 * 兔子腿
 *
 * @author Winfxk
 */
public class AQspirit extends EffectItem {
	private String[] Ts;

	@Override
	public int getID() {
		return 415;
	}

	@Override
	public void Wake() {
	}

	@Override
	public void onDamage(EntityDamageEvent e) {
	}

	@Override
	public void onBeingDamage(EntityDamageEvent e) {
		player.setNameTag(Ts[Tool.getRand(0, Ts.length - 1)]);
	}

	@Override
	public boolean isReDo() {
		return false;
	}

	@Override
	public void onConsume() {
		super.onConsume();
		String s = getText();
		if (s == null || s.isEmpty())
			Ts = new String[] { this.getName(), this.getClass().getSimpleName() };
		else
			Ts = s.split("|");
	}
}