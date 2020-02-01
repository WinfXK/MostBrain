package cn.epicfx.winfxk.mostbrain;

import java.io.File;
import java.io.FilenameFilter;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.epicfx.winfxk.mostbrain.cmd.ACommand;
import cn.epicfx.winfxk.mostbrain.cmd.PCommand;
import cn.epicfx.winfxk.mostbrain.effect.Effecttor;
import cn.epicfx.winfxk.mostbrain.game.GameEvent;
import cn.epicfx.winfxk.mostbrain.game.GameHandle;
import cn.epicfx.winfxk.mostbrain.game.GameThread;
import cn.epicfx.winfxk.mostbrain.game.MostConfig;
import cn.epicfx.winfxk.mostbrain.game.MostEvent;
import cn.epicfx.winfxk.mostbrain.game.SettingGame;
import cn.epicfx.winfxk.mostbrain.money.EconomyAPI;
import cn.epicfx.winfxk.mostbrain.money.EconomyManage;
import cn.epicfx.winfxk.mostbrain.money.MyEconomy;
import cn.epicfx.winfxk.mostbrain.tool.Format.MyData;
import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.Player;
import cn.nukkit.utils.Config;

/**
 * @author Winfxk
 */
public class Activate implements FilenameFilter {
	public Player setPlayer;
	public MakeForm makeForm;
	public ResCheck resCheck;
	public GameEvent gameEvent;
	public GameHandle gameHandle;
	public SettingGame settingGame;
	public boolean GameError = false;
	public boolean isStartGame = false;
	public boolean SettingModel = false;
	public boolean isGameSettingUp = false;
	public List<String> langs = new ArrayList<>();
	public final static String[] FormIDs = { /* 0 */"主页", /* 1 */"副页", /* 2 */"提示" };
	public final static String MessageFileName = "Message.yml", ConfigFileName = "Config.yml",
			CommandFileName = "Command.yml", EconomyListConfigName = "EconomyList.yml", FormIDFileName = "FormID.yml",
			GameConfigFileName = "MostBrain.yml", PlayerDataDirName = "Players", LanguageDirName = "language";
	private MostBrain mis;
	private MyEconomy economy;
	private Effecttor effecttor;
	private EconomyManage money;
	private MostConfig mostConfig;
	private GameThread UpdateThread;
	private static Activate activate;
	private LinkedHashMap<String, MyPlayer> Players;
	protected FormID FormID;
	protected Message message;
	protected List<MostEvent> mostEvents;
	protected Config config, CommandConfig, GameConfig;
	protected static final String[] loadFile = { ConfigFileName, CommandFileName };
	protected static final String[] defaultFile = { ConfigFileName, CommandFileName, MessageFileName };

	/**
	 * 插件数据的集合类
	 *
	 * @param kis
	 */
	public Activate(MostBrain kis) {
		mostEvents = new ArrayList<>();
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
			reloadMostConfig(true);
		makeForm = new MakeForm(this);
		(UpdateThread = new GameThread(this, 0)).start();
		kis.getServer().getCommandMap().register(getName(), new ACommand(this));
		kis.getServer().getCommandMap().register(getName(), new PCommand(this));
		kis.getServer().getPluginManager().registerEvents(new PlayerEvent(this), kis);
		kis.getLogger().info(message.getMessage("插件启动", new String[] { "{loadTime}" },
				new Object[] { ((float) Duration.between(mis.loadTime, Instant.now()).toMillis()) + "ms" }));
	}

	/**
	 * 获取服务器Top榜
	 *
	 * @return
	 */
	public String getTop() {
		Map<String, Long> map = new HashMap<>();
		File file = new File(mis.getDataFolder(), Activate.PlayerDataDirName);
		File[] files = file.listFiles(this);
		if (files.length <= 0)
			return "";
		String name, rc, rb, re, s, a = "";
		boolean isF = false;
		int wz;
		long df = 0;
		List<String> pList;
		int score, honor, death, malicious;
		for (File file2 : files) {
			config = new Config(file2, Config.YAML);
			name = config.getString("name");
			if (name == null || name.isEmpty()) {
				name = file2.getName();
				wz = name.lastIndexOf(".");
				if (wz <= 0)
					continue;
				name = name.substring(0, wz);
			}
			score = config.getInt("得分");
			honor = config.getInt("荣耀");
			death = config.getInt("死亡");
			malicious = config.getInt("恶意度");
			if (score > 0) {
				score = Tool.ObjectToInt(Math.sqrt(score));
				df = (honor > 0) ? score * Tool.ObjectToInt(Math.sqrt(honor)) : score - (honor * 2);
				if (df > 0) {
					if (death > 0)
						df /= death;
					if (malicious > 0)
						df /= Math.pow(malicious, 2);
					df += config.getInt("游戏局数") + config.getInt("攻击数");
				} else if (df < 0)
					df -= (death + malicious);
			} else
				df += (Math.abs(score) + Math.abs(honor) + Math.abs(death) + Math.abs(malicious)) * -1;
			map.put(name, df);
		}
		map = Tool.sortByValueDescending(map);
		pList = new ArrayList<>(map.keySet());
		rc = Tool.getRandColor();
		rb = Tool.getRandColor();
		MyData<String, String> myData = new MyData<>();
		re = Tool.getRandColor();
		String[] sss, ss = { "{Top}", "{Score}", "{Player}", "{isRandColor}", " {isRandColor2}", "{isRandColor3}" };
		for (int i = 0; i < 10 && i < map.size(); i++) {
			s = message.getSun("Command", "PlayerCommand", "Top10", ss,
					new Object[] { i + 1, map.get(pList.get(i)), pList.get(i), rc, re, rb });
			if (s.contains("{Format}")) {
				isF = true;
				sss = s.split("\\{Format\\}");
				myData.put(sss[0], sss[1]);
				continue;
			}
			a += (a.isEmpty() ? "" : "\n") + s;
		}
		if (isF)
			return myData.getStrng();
		return a;
	}

	/**
	 * 获取服务器Top死亡榜
	 *
	 * @return
	 */
	public String getTopD() {
		Map<String, Long> map = new HashMap<>();
		File file = new File(mis.getDataFolder(), Activate.PlayerDataDirName);
		File[] files = file.listFiles(this);
		if (files.length <= 0)
			return "";
		String name, rc, rb, re, s, a = "";
		boolean isF = false;
		int wz;
		List<String> pList;
		for (File file2 : files) {
			config = new Config(file2, Config.YAML);
			name = config.getString("name");
			if (name == null || name.isEmpty()) {
				name = file2.getName();
				wz = name.lastIndexOf(".");
				if (wz <= 0)
					continue;
				name = name.substring(0, wz);
			}
			map.put(name, Tool.objToLong(config.getInt("死亡")));
		}
		map = Tool.sortByValueAscending(map);
		pList = new ArrayList<>(map.keySet());
		rc = Tool.getRandColor();
		rb = Tool.getRandColor();
		MyData<String, String> myData = new MyData<>();
		re = Tool.getRandColor();
		String[] sss, ss = { "{Top}", "{Score}", "{Player}", "{isRandColor}", " {isRandColor2}", "{isRandColor3}" };
		for (int i = 0; i < 10 && i < map.size(); i++) {
			s = message.getSun("Command", "PlayerCommand", "Top10", ss,
					new Object[] { i + 1, map.get(pList.get(i)), pList.get(i), rc, re, rb });
			if (s.contains("{Format}")) {
				isF = true;
				sss = s.split("\\{Format\\}");
				myData.put(sss[0], sss[1]);
				continue;
			}
			a += (a.isEmpty() ? "" : "\n") + s;
		}
		if (isF)
			return myData.getStrng();
		return a;
	}

	/**
	 * 获取服务器Top得分榜
	 *
	 * @return
	 */
	public String getTopS() {
		Map<String, Long> map = new HashMap<>();
		File file = new File(mis.getDataFolder(), Activate.PlayerDataDirName);
		File[] files = file.listFiles(this);
		if (files.length <= 0)
			return "";
		String name, rc, rb, re, s, a = "";
		boolean isF = false;
		int wz;
		List<String> pList;
		for (File file2 : files) {
			config = new Config(file2, Config.YAML);
			name = config.getString("name");
			if (name == null || name.isEmpty()) {
				name = file2.getName();
				wz = name.lastIndexOf(".");
				if (wz <= 0)
					continue;
				name = name.substring(0, wz);
			}
			map.put(name, Tool.objToLong(config.getInt("得分")));
		}
		map = Tool.sortByValueDescending(map);
		pList = new ArrayList<>(map.keySet());
		rc = Tool.getRandColor();
		rb = Tool.getRandColor();
		MyData<String, String> myData = new MyData<>();
		re = Tool.getRandColor();
		String[] sss, ss = { "{Top}", "{Score}", "{Player}", "{isRandColor}", " {isRandColor2}", "{isRandColor3}" };
		for (int i = 0; i < 10 && i < map.size(); i++) {
			s = message.getSun("Command", "PlayerCommand", "Top10", ss,
					new Object[] { i + 1, map.get(pList.get(i)), pList.get(i), rc, re, rb });
			if (s.contains("{Format}")) {
				isF = true;
				sss = s.split("\\{Format\\}");
				myData.put(sss[0], sss[1]);
				continue;
			}
			a += (a.isEmpty() ? "" : "\n") + s;
		}
		if (isF)
			return myData.getStrng();
		return a;
	}

	/**
	 * 获取服务器Top荣耀榜
	 *
	 * @return
	 */
	public String getTopH() {
		Map<String, Long> map = new HashMap<>();
		File file = new File(mis.getDataFolder(), Activate.PlayerDataDirName);
		File[] files = file.listFiles(this);
		if (files.length <= 0)
			return "";
		String name, rc, rb, re, s, a = "";
		boolean isF = false;
		int wz;
		List<String> pList;
		for (File file2 : files) {
			config = new Config(file2, Config.YAML);
			name = config.getString("name");
			if (name == null || name.isEmpty()) {
				name = file2.getName();
				wz = name.lastIndexOf(".");
				if (wz <= 0)
					continue;
				name = name.substring(0, wz);
			}
			map.put(name, Tool.objToLong(config.getInt("荣耀")));
		}
		map = Tool.sortByValueDescending(map);
		pList = new ArrayList<>(map.keySet());
		rc = Tool.getRandColor();
		rb = Tool.getRandColor();
		MyData<String, String> myData = new MyData<>();
		re = Tool.getRandColor();
		String[] sss, ss = { "{Top}", "{Score}", "{Player}", "{isRandColor}", " {isRandColor2}", "{isRandColor3}" };
		for (int i = 0; i < 10 && i < map.size(); i++) {
			s = message.getSun("Command", "PlayerCommand", "Top10", ss,
					new Object[] { i + 1, map.get(pList.get(i)), pList.get(i), rc, re, rb });
			if (s.contains("{Format}")) {
				isF = true;
				sss = s.split("\\{Format\\}");
				myData.put(sss[0], sss[1]);
				continue;
			}
			a += (a.isEmpty() ? "" : "\n") + s;
		}
		if (isF)
			return myData.getStrng();
		return a;
	}

	/**
	 * 得到插件检测更新线程
	 *
	 * @return
	 */
	public GameThread getUpdateThread() {
		return UpdateThread;
	}

	/**
	 * 得到当前的游戏事件
	 *
	 * @return
	 */
	public List<MostEvent> getMostEvents() {
		return new ArrayList<>(mostEvents);
	}

	/**
	 * 添加游戏事件
	 *
	 * @param event
	 * @return
	 */
	public Activate addGameEvent(MostEvent event) {
		if (!mostEvents.contains(event))
			mostEvents.add(event);
		return this;
	}

	/**
	 * 得到Buff管理器
	 *
	 * @return
	 */
	public Effecttor getEffecttor() {
		return effecttor;
	}

	/**
	 * 重载所有游戏配置
	 *
	 * @param isMsg
	 * @return
	 */
	public MostConfig reloadMostConfig(boolean isMsg) {
		mostConfig = new MostConfig(this);
		gameEvent = new GameEvent(this);
		gameHandle = new GameHandle(this);
		effecttor = new Effecttor(gameHandle, isMsg);
		return mostConfig;
	}

	/**
	 * 得到游戏配置
	 *
	 * @return
	 */
	public MostConfig getMostConfig() {
		return mostConfig;
	}

	/**
	 * 得到默认经济插件
	 *
	 * @return
	 */
	public MyEconomy getEconomy() {
		return economy;
	}

	/**
	 * 设置默认经济插件
	 *
	 * @param EconomyName
	 */
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
		List<Object> list = CommandConfig.getList(string);
		String[] s = new String[list.size()];
		for (int i = 0; i < list.size(); i++)
			s[i] = Tool.objToString(list.get(i));
		return s;
	}

	/**
	 * 得到插件名称
	 *
	 * @return
	 */
	public String getName() {
		return mis.getName();
	}

	/**
	 * 得到插件主类
	 *
	 * @return
	 */
	public MostBrain getMostBrain() {
		return mis;
	}

	/**
	 * 删除玩家数据
	 *
	 * @param player
	 */
	public void removePlayers(Player player) {
		removePlayers(player.getName());
	}

	/**
	 * 删除玩家数据
	 *
	 * @param player
	 */
	public void removePlayers(String player) {
		if (Players.containsKey(player))
			Players.remove(player);
	}

	/**
	 * 设置玩家数据
	 *
	 * @param player
	 * @return
	 */
	public void setPlayers(Player player, MyPlayer myPlayer) {
		setPlayers(player.getName(), myPlayer);
	}

	/**
	 * 设置玩家数据
	 *
	 * @param player
	 * @return
	 */
	public void setPlayers(String player, MyPlayer myPlayer) {
		Players.put(player, myPlayer);
	}

	/**
	 * 设置玩家数据
	 *
	 * @param player
	 * @return
	 */
	public MyPlayer getPlayers(String player) {
		return isPlayers(player) ? Players.get(player) : null;
	}

	/**
	 * 玩家数据是否存在
	 *
	 * @param player
	 * @return
	 */
	public boolean isPlayers(Player player) {
		return Players.containsKey(player.getName());
	}

	/**
	 * 玩家数据是否存在
	 *
	 * @param player
	 * @return
	 */
	public boolean isPlayers(String player) {
		return Players.containsKey(player);
	}

	/**
	 * 得到玩家数据
	 *
	 * @return
	 */
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

	/**
	 * 得到ID类
	 *
	 * @return
	 */
	public FormID getFormID() {
		return FormID;
	}

	/**
	 * 得到语言类
	 *
	 * @return
	 */
	public Message getMessage() {
		return message;
	}

	/**
	 * 对外接口
	 *
	 * @return
	 */
	public static Activate getActivate() {
		return activate;
	}

	/**
	 * 得到游戏配置文件对象
	 *
	 * @return
	 */
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

	/**
	 * 得到MostBrain主配置文件
	 *
	 * @return
	 */
	public Config getConfig() {
		return config;
	}

	/**
	 * 设置游戏配置文件
	 *
	 * @param gameConfig
	 */
	public void setGameConfig(Config gameConfig) {
		GameConfig = gameConfig;
	}

	/**
	 * 设置游戏配置
	 *
	 * @param mostConfig
	 */
	public void setMostConfig(MostConfig mostConfig) {
		this.mostConfig = mostConfig;
	}

	/**
	 * 文件列表过滤器
	 */
	@Override
	public boolean accept(File arg0, String arg1) {
		return new File(arg0, arg1).isFile();
	}
}
