package cn.epicfx.winfxk.mostbrain.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.epicfx.winfxk.mostbrain.Activate;
import cn.epicfx.winfxk.mostbrain.MyPlayer;
import cn.epicfx.winfxk.mostbrain.effect.EffectItem;
import cn.epicfx.winfxk.mostbrain.tool.Tool;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.math.Vector3;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class GameHandle {
	private Activate ac;
	private boolean ReadyisModel = false;
	private GameMainThread gameMainThread;
	private ReadyiingThread readyiingThread;
	private int GameMinCount;
	/**
	 * 已等待时间
	 */
	private int ReadyisTime;
	/**
	 * 剩余等待时间
	 */
	private int ReadyTime;
	private List<Player> gamePlayers;
	private boolean StartGame = false;
	/**
	 * 重生点
	 */
	private Location location;
	private MostConfig mostConfig;
	/**
	 * 重生点所在的世界
	 */
	private Level SpawnLevel;
	/**
	 * 玩家在准备状态时木牌显示的内容
	 */
	private String[] ReadySign;

	public GameHandle(Activate ac) {
		this.ac = ac;
		mostConfig = ac.getMostConfig();
		SpawnLevel = Server.getInstance().getLevelByName(mostConfig.StartLevel);
		location = new Location(mostConfig.Spawn.get("X"), mostConfig.Spawn.get("Y"), mostConfig.Spawn.get("Z"),
				SpawnLevel);
		GameMinCount = ac.getConfig().getInt("游戏最小人数");
		GameMinCount = GameMinCount < 2 ? 2 : GameMinCount;
		gamePlayers = new ArrayList<>();
		ReadyTime = ac.getConfig().getInt("等待时间");
		ReadySign = getMessageList("加入准备");
	}

	public void addPlayer(Player player) {
		if (gamePlayers.contains(player))
			return;
		if (StartGame)
			return;
		if (!ReadyisModel) {
			ReadyisModel = true;
			readyiingThread = new ReadyiingThread();
			readyiingThread.start();
		}
		gamePlayers.add(player);
		MyPlayer myPlayer = ac.getPlayers(player.getName());
		myPlayer.saveInventory();
		player.getInventory().clearAll();
		String[] list = getMessageList("加入准备");
		for (int i = 0; i < list.length; i++)
			list[i] = ac.getMessage().getText(list[i],
					new String[] { "{Player}", "{Money}", "{Count}", "{MinCount}", "{IsCount}" },
					new Object[] { player.getName(), myPlayer.getMoney() + gamePlayers.size(), GameMinCount,
							gamePlayers.size() >= GameMinCount ? " "
									: Tool.getRandColor() + gamePlayers.size() + Tool.getRandColor() + "/"
											+ Tool.getRandColor() + GameMinCount });
		player.sendTitle(list[0], list.length > 1 ? list[1] : null);
	}

	/**
	 * 游戏准备线程
	 * 
	 * @author Winfxk
	 */
	private class ReadyiingThread extends Thread {
		@Override
		public void run() {
			try {
				while (ReadyisModel) {
					if (gamePlayers.size() > GameMinCount)
						for (Player player : gamePlayers)
							player.sendMessage(ac.getMessage().getSon("Game", "即将开始",
									new String[] { "{Player}", "{Money}", "{ReadyTime}" }, new Object[] {
											player.getName(), MyPlayer.getMoney(player.getName()), ReadyTime-- }));
					else if (ReadyisTime > 120)
						for (Player player : gamePlayers)
							player.sendMessage(ac.getMessage().getSon("Game", "人数不足",
									new String[] { "{Player}", "{Money}", "{ReadyTime}" }, new Object[] {
											player.getName(), MyPlayer.getMoney(player.getName()), ReadyisTime++ }));
					sleep(1000);
					String[] list = getMessageList("ReadyisGameSign");
					for (int i = 0; i < list.length; i++)
						list[i] = ac.getMessage().getText(list[i],
								new String[] { "{Count}", "{MinCount}", "{IsCount}" },
								new Object[] { gamePlayers.size(), GameMinCount,
										gamePlayers.size() >= GameMinCount ? " "
												: Tool.getRandColor() + gamePlayers.size() + Tool.getRandColor() + "/"
														+ Tool.getRandColor() + GameMinCount });
					Tool.setSign(mostConfig.Level, mostConfig.Start.get("X"), mostConfig.Start.get("Y"),
							mostConfig.Start.get("Z"), list);
					if (ReadyTime <= 0) {
						ReadyisModel = false;
						StartGame = true;
						gameMainThread = new GameMainThread();
						gameMainThread.start();
						readyiingThread = null;
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				getMessage("游戏出现错误");
			}
			super.run();
		}
	}

	/**
	 * 游戏主线程
	 * 
	 * @author Winfxk
	 */
	private class GameMainThread extends Thread {
		@Override
		public void run() {
			for (Player player : gamePlayers) {
				player.teleport(location);
				player.sendTitle(getMessage("开始游戏", player));
			}
			for (int i = 0; i < gamePlayers.size() * 5; i++)
				location.level.dropItem(
						new Vector3(Tool.getRand((int) mostConfig.MinX + 1, (int) mostConfig.MaxX - 1),
								Tool.getRand((int) mostConfig.MinX + 1, (int) mostConfig.MaxX - 1),
								Tool.getRand((int) mostConfig.MinX + 1, (int) mostConfig.MaxX - 1)),
						getItem(), null, true, 3);
			while (StartGame) {

			}
			super.run();
		}

		public Item getItem() {
			return null;
		}
	}

	public String getMessage(String Key) {
		return getMessage(Key, null);
	}

	public String getMessage(String Key, Player player) {
		return player == null ? ac.getMessage().getSon("Game", Key) : ac.getMessage().getSon("Game", Key, player);
	}

	/**
	 * 随机给予玩家一个Buff
	 * 
	 * @param player
	 * @param effectItem
	 */
	public void giveBuffs(Player player) {
		EffectItem[] items = ac.getEffecttor().getList();
		EffectItem item = items[Tool.getRand(0, items.length - 1)];
		player.getInventory().addItem(ac.getEffecttor().getItem(item));
	}

	/**
	 * 随机给予玩家一个Buff
	 * 
	 * @param player
	 * @param effectItem
	 */
	public void giveBuffs(Player player, EffectItem effectItem) {
		if (!gamePlayers.contains(player) || effectItem == null)
			return;
		player.getInventory().addItem(ac.getEffecttor().getItem(effectItem));
	}

	/**
	 * 清空一个玩家的Buff
	 * 
	 * @param player
	 */
	public void clearBuffs(Player player) {
		if (!gamePlayers.contains(player))
			return;
		MyPlayer myPlayer = ac.getPlayers(player.getName());
		myPlayer.items = new ArrayList<>();
		ac.setPlayers(player, myPlayer);
	}

	/**
	 * 删除一个玩家的Buff
	 * 
	 * @param player
	 * @param Name
	 */
	public void delBuffs(Player player, String Name) {
		if (!gamePlayers.contains(player))
			return;
		MyPlayer myPlayer = ac.getPlayers(player.getName());
		for (int i = 0; i < myPlayer.items.size(); i++)
			if (myPlayer.items.get(i).getName().equals(Name))
				myPlayer.items.remove(i);
		ac.setPlayers(player, myPlayer);
	}

	/**
	 * 获取玩家的所有Buff
	 * 
	 * @param player
	 * @return
	 */
	public List<EffectItem> getBuffs(Player player) {
		if (!gamePlayers.contains(player))
			return null;
		MyPlayer myPlayer = ac.getPlayers(player.getName());
		return myPlayer.items;
	}

	public String[] getMessageList(String Key) {
		Object obj = ac.getMessage().getConfig().get("Game");
		Map<String, Object> map = obj == null || !(obj instanceof Map) ? new HashMap<>()
				: (HashMap<String, Object>) obj;
		obj = map.get(Key);
		List<String> list = obj == null || !(obj instanceof List) ? new ArrayList<>() : (ArrayList<String>) obj;
		String[] strings = new String[list.size()];
		for (int i = 0; i < list.size(); i++)
			strings[i] = list.get(i);
		return strings;
	}

}
