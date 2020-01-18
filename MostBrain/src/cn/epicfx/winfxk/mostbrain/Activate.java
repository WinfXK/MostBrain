package cn.epicfx.winfxk.mostbrain;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;

import cn.epicfx.winfxk.mostbrain.cmd.ACommand;
import cn.epicfx.winfxk.mostbrain.cmd.PCommand;
import cn.epicfx.winfxk.mostbrain.effect.Effecttor;
import cn.epicfx.winfxk.mostbrain.event.PlayerEvent;
import cn.epicfx.winfxk.mostbrain.game.GameEvent;
import cn.epicfx.winfxk.mostbrain.game.GameHandle;
import cn.epicfx.winfxk.mostbrain.game.MostConfig;
import cn.epicfx.winfxk.mostbrain.game.SettingGame;
import cn.epicfx.winfxk.mostbrain.money.EconomyAPI;
import cn.epicfx.winfxk.mostbrain.money.EconomyManage;
import cn.epicfx.winfxk.mostbrain.money.MyEconomy;
import cn.nukkit.Player;
import cn.nukkit.utils.Config;

/**
 * @author Winfxk
 */
public class Activate {
	public Player setPlayer;
	public GameEvent gameEvent;
	public GameHandle gameHandle;
	public SettingGame settingGame;
	public boolean GameError = false;
	public boolean isStartGame = false;
	public boolean SettingModel = false;
	public boolean isGameSettingUp = false;
	public final static String[] FormIDs = {};
	public final static String MessageFileName = "Message.yml", ConfigFileName = "Config.yml",
			CommandFileName = "Command.yml", EconomyListConfigName = "EconomyList.yml", FormIDFileName = "FormID.yml",
			GameConfigFileName = "MostBrain.yml", PlayerDataDirName = "Players";
	private MostBrain mis;
	private MyEconomy economy;
	private Effecttor effecttor;
	private EconomyManage money;
	private MostConfig mostConfig;
	private static Activate activate;
	private LinkedHashMap<String, MyPlayer> Players;
	protected FormID FormID;
	protected Message message;
	protected ResCheck resCheck;
	protected Config config, CommandConfig, GameConfig;
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
		Players = new LinkedHashMap<>();
		if ((resCheck = new ResCheck(this).start()) == null)
			return;
		money = new EconomyManage();
		money.addEconomyAPI(new EconomyAPI(this));
		economy = money.getEconomy(config.getString("默认货币"));
		GameConfig = new Config(new File(kis.getDataFolder(), GameConfigFileName), Config.YAML);
		isGameSettingUp = GameConfig.getBoolean("GameSettingUp");
		if (!isGameSettingUp)
			kis.getLogger().warning(message.getMessage("游戏未设置"));
		else
			reloadMostConfig();
		kis.getServer().getCommandMap().register(getName(), new ACommand(this));
		kis.getServer().getCommandMap().register(getName(), new PCommand(this));
		kis.getServer().getPluginManager().registerEvents(new PlayerEvent(this), kis);
		kis.getLogger().info(message.getMessage("插件启动"));
	}

	public Effecttor getEffecttor() {
		return effecttor;
	}

	public MostConfig reloadMostConfig() {
		mostConfig = new MostConfig(this);
		return mostConfig;
	}

	public MostConfig getMostConfig() {
		return mostConfig;
	}

	public MyEconomy getEconomy() {
		return economy;
	}

	public void setEconomy(String EconomyName) {
		if (money.supportEconomy(EconomyName))
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

	public Message getMessage() {
		return message;
	}

	public static Activate getActivate() {
		return activate;
	}

	public Config getGameConfig() {
		return GameConfig;
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

	public void setGameConfig(Config gameConfig) {
		GameConfig = gameConfig;
	}

	public void setMostConfig(MostConfig mostConfig) {
		this.mostConfig = mostConfig;
	}
}
