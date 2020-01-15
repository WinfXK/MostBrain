package cn.epicfx.winfxk.mostbrain;

import java.util.LinkedHashMap;
import java.util.List;

import cn.epicfx.winfxk.mostbrain.cmd.ACommand;
import cn.epicfx.winfxk.mostbrain.cmd.PCommand;
import cn.epicfx.winfxk.mostbrain.event.PlayerEvent;
import cn.epicfx.winfxk.mostbrain.money.EconomyAPI;
import cn.epicfx.winfxk.mostbrain.money.EconomyManage;
import cn.epicfx.winfxk.mostbrain.money.MyEconomy;
import cn.nukkit.Player;
import cn.nukkit.utils.Config;

/**
 * @author Winfxk
 */
public class Activate {
	public final static String[] FormIDs = {};
	public final static String MessageFileName = "Message.yml", ConfigFileName = "Config.yml",
			CommandFileName = "Command.yml", EconomyListConfigName = "EconomyList.yml", FormIDFileName = "FormID.yml",
			MostBrainConfigFileName = "MostBrain.yml", PlayerDataDirName = "Players";
	private MostBrain mis;
	private MyEconomy economy;
	private EconomyManage money;
	private static Activate activate;
	private LinkedHashMap<String, MyPlayer> Players;
	protected FormID FormID;
	protected Message message;
	protected ResCheck resCheck;
	protected Config config, MainMenu, CommandConfig;
	protected static final String[] loadFile = { ConfigFileName, CommandFileName };
	protected static final String[] defaultFile = { ConfigFileName, CommandFileName, MessageFileName };

	/**
	 * 插件数据的集合类
	 * 
	 * @param kis
	 */
	public Activate(MostBrain kis) {
		activate = this;
		mis = kis;
		FormID = new FormID();
		if ((resCheck = new ResCheck(this).start()) == null)
			return;
		money = new EconomyManage();
		money.addEconomyAPI(new EconomyAPI(this));
		Players = new LinkedHashMap<>();
		economy = money.getEconomy(config.getString("EconomyAPI"));
		kis.getServer().getCommandMap().register(getName(), new ACommand(this));
		kis.getServer().getCommandMap().register(getName(), new PCommand(this));
		kis.getServer().getPluginManager().registerEvents(new PlayerEvent(this), kis);
		kis.getLogger().info(message.getMessage("插件启动"));
	}

	public MyEconomy getEconomy() {
		return economy;
	}

	public void setEconomy(String EconomyName) {
		this.economy = money.getEconomy(EconomyName);
	}

	/**
	 * 获取自定义命令内容
	 * 
	 * @param string
	 * @return
	 */
	public String[] getCommands(String string) {
		List<String> list = CommandConfig.getList(string);
		String[] s = new String[list.size()];
		for (int i = 0; i < list.size(); i++)
			s[i] = list.get(i);
		return s;
	}

	public String getName() {
		return mis.getName();
	}

	public MostBrain getMostBrain() {
		return mis;
	}

	public void removePlayers(Player player) {
		removePlayers(player.getName());
	}

	public void removePlayers(String player) {
		if (Players.containsKey(player))
			Players.remove(player);
	}

	public void setPlayers(Player player, MyPlayer myPlayer) {
		setPlayers(player.getName(), myPlayer);
	}

	public void setPlayers(String player, MyPlayer myPlayer) {
		Players.put(player, myPlayer);
	}

	public MyPlayer getPlayers(String player) {
		return isPlayers(player) ? Players.get(player) : null;
	}

	public boolean isPlayers(Player player) {
		return Players.containsKey(player.getName());
	}

	public boolean isPlayers(String player) {
		return Players.containsKey(player);
	}

	public LinkedHashMap<String, MyPlayer> getPlayers() {
		return Players;
	}

	/**
	 * 返回经济支持管理器</br>
	 * Return to the economic support manager
	 * 
	 * @return
	 */
	public EconomyManage getEconomyManage() {
		return money;
	}

	public FormID getFormID() {
		return FormID;
	}

	public Config getMainMenu() {
		return MainMenu;
	}

	public Message getMessage() {
		return message;
	}

	public static Activate getActivate() {
		return activate;
	}

	/**
	 * 返回EconomyAPI货币的名称
	 * 
	 * @return
	 */
	public String getMoneyName() {
		return economy == null ? config.getString("金币") : economy.getMoneyName();
	}

	public Config getConfig() {
		return config;
	}
}
