package cn.epicfx.winfxk.mostbrain;

import com.creeperface.nukkit.placeholderapi.api.PlaceholderAPI;

import cn.nukkit.plugin.Plugin;
import cn.nukkit.utils.Config;

/**
 * @author Winfxk
 */
@SuppressWarnings("deprecation")
public class LinkPAPI {
	private Activate ac;

	public LinkPAPI(Activate activate) {
		ac = activate;
		Plugin plugin = activate.getMostBrain().getServer().getPluginManager().getPlugin("PlaceholderAPI");
		if (plugin != null)
			try {
				link();
			} catch (Exception e) {
				e.printStackTrace();
				activate.getMostBrain().getLogger().warning(activate.getMessage().getMessage("无法连接PlaceholderAPI"));
			}
	}

	/**
	 * 执行连接
	 *
	 * @return
	 * @throws Exception
	 */
	public boolean link() throws Exception {
		Config config = ac.getPapiConfig();
		PlaceholderAPI papi = PlaceholderAPI.getInstance();
		papi.visitorSensitivePlaceholder(config.getString("GameCount"),
				Player -> MyPlayer.getConfig(Player.getName()).getInt("游戏局数"), 1);
		papi.visitorSensitivePlaceholder(config.getString("AttackCount"),
				Player -> MyPlayer.getConfig(Player.getName()).getInt("攻击数"), 1);
		papi.visitorSensitivePlaceholder(config.getString("Malicious"),
				Player -> MyPlayer.getConfig(Player.getName()).getInt("恶意度"), 1);
		papi.visitorSensitivePlaceholder(config.getString("Honor"),
				Player -> MyPlayer.getConfig(Player.getName()).getInt("荣耀"), 1);
		papi.visitorSensitivePlaceholder(config.getString("Malicious"),
				Player -> MyPlayer.getConfig(Player.getName()).getInt("死亡"), 1);
		papi.visitorSensitivePlaceholder(config.getString("Score"),
				Player -> MyPlayer.getConfig(Player.getName()).getInt("得分"), 1);
		papi.visitorSensitivePlaceholder(config.getString("CompScore"), Player -> MyPlayer.getCompScore(Player), 1);
		return true;
	}
}
