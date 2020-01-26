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
import cn.nukkit.command.CommandSender;
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
	 * 增加附属掉落间隔
	 */
	private int Timesleep;
	/**
	 * 管理员是否终止了游戏
	 */
	protected boolean AdminStopGame = false;
	/**
	 * 终止游戏的玩家对象
	 */
	protected CommandSender sender;

	public GameHandle(Activate ac) {
		this.ac = ac;
		mostConfig = ac.getMostConfig();
		SpawnLevel = Server.getInstance().getLevelByName(mostConfig.StartLevel);
		location = new Location(mostConfig.Spawn.get("X"), mostConfig.Spawn.get("Y"), mostConfig.Spawn.get("Z"),
				SpawnLevel);
		GameMinCount = ac.getConfig().getInt("游戏最小人数");
		GameMinCount = GameMinCount <= 0 ? 2 : GameMinCount;
		gamePlayers = new ArrayList<>();
		ReadyTime = 0;
		Timesleep = ac.getConfig().getInt("掉落间隔");
	}

	public boolean ReadyisModel() {
		return ReadyisModel;
	}

	public boolean StartGame() {
		return StartGame;
	}

	public GameHandle setAdminStopGame(boolean adminStopGame) {
		AdminStopGame = adminStopGame;
		return this;
	}

	public GameHandle setCommandSender(CommandSender sender) {
		this.sender = sender;
		return this;
	}

	public List<Player> getGamePlayers() {
		return new ArrayList<>(gamePlayers);
	}

	/**
	 * 增加参与游戏的玩家数量
	 * 
	 * @param player
	 */
	public void addPlayer(Player player) {
		if (gamePlayers.contains(player))
			return;
		if (StartGame)
			return;
		MyPlayer myPlayer = ac.getPlayers(player.getName());
		if (myPlayer.getMoney() < ac.getConfig().getDouble("游戏费用")) {
			player.sendMessage(
					ac.getMessage().getSon("Game", "结束惩罚", new String[] { "{Player}", "{Money}", "{MyMoney}" },
							new Object[] { player.getName(), ac.getConfig().getDouble("游戏费用"), myPlayer.getMoney() }));
			return;
		}
		player.sendMessage(
				ac.getMessage().getSon("Game", "扣除费用", new String[] { "{Player}", "{Money}", "{MyMoney}" },
						new Object[] { player.getName(), ac.getConfig().getDouble("游戏费用"), myPlayer.getMoney() }));
		ac.getEconomy().reduceMoney(player, ac.getConfig().getDouble("游戏费用"));
		if (!ReadyisModel) {
			ReadyisModel = true;
			readyiingThread = new ReadyiingThread();
			readyiingThread.start();
		}
		ac.isStartGame = true;
		gamePlayers.add(player);
		myPlayer.saveInventory().saveGameMode().saveHealth();
		player.getInventory().clearAll();
		myPlayer.ReadyModel = true;
		String[] list = getMessageList("加入准备");
		int Max = ac.getConfig().getInt("游戏最大血量");
		int h = ac.getConfig().getInt("游戏血量");
		h = h > Max ? Max : h;
		player.setMaxHealth(Max);
		player.setHealth(h);
		player.setGamemode(0);
		player.removeAllEffects();
		myPlayer.items = new ArrayList<>();
		myPlayer.gameData = new GameData();
		ac.setPlayers(player, myPlayer);
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
				ReadyTime = ac.getConfig().getInt("等待时间");
				while (ReadyisModel) {
					if (AdminStopGame) {
						String[] Key2 = { "{Player}", "{Money}", "{Admin}", "{Count}" };
						ac.getMostBrain().getLogger().info(ac.getMessage().getSon("Game", "管理员终止游戏", Key2,
								new Object[] { "", "", sender.getName(), gamePlayers.size() }));
						for (Player player : gamePlayers)
							player.sendMessage(ac.getMessage().getSon("Game", "管理员终止游戏", Key2,
									new Object[] { player.getName(), MyPlayer.getMoney(player.getName()),
											sender.getName(), gamePlayers.size() }));
						break;
					}
					if (gamePlayers.size() <= 0) {
						ac.getMostBrain().getLogger().info(getMessage("游戏终止"));
						break;
					}
					if (gamePlayers.size() < 1) {
						reload();
						break;
					}
					sleep(1000);
					if (gamePlayers.size() >= GameMinCount)
						ReadyTime--;
					for (Player player : gamePlayers)
						if (gamePlayers.size() >= GameMinCount
								&& ((ReadyTime >= 5 && ReadyTime % 5 == 0) || ReadyTime <= 3)) {
							player.sendMessage(ac.getMessage().getSon("Game", "即将开始",
									new String[] { "{Player}", "{Money}", "{ReadyTime}" },
									new Object[] { player.getName(), MyPlayer.getMoney(player.getName()), ReadyTime }));
						} else if (ReadyisTime > 120)
							player.sendMessage(ac.getMessage().getSon("Game", "人数不足",
									new String[] { "{Player}", "{Money}", "{ReadyTime}" }, new Object[] {
											player.getName(), MyPlayer.getMoney(player.getName()), ReadyisTime }));
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
						for (Player player : gamePlayers) {
							MyPlayer myPlayer = ac.getPlayers(player.getName());
							myPlayer.ReadyModel = false;
							myPlayer.GameModel = true;
							ac.setPlayers(player, myPlayer);
						}
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
	 * 游戏设置复位
	 */
	public void reload() {
		Map<String, Object> GameMessage = (Map<String, Object>) ac.getMessage().getConfig().get("Game");
		List<?> list = (List<?>) GameMessage.get("NotStartSign");
		Level signLevel = Server.getInstance().getLevelByName(mostConfig.Level);
		if (signLevel != null)
			Tool.setSign(signLevel.getBlock(mostConfig.getStart()),
					ac.getMessage().getText(Tool.objToString(list.get(0), "")),
					ac.getMessage().getText(Tool.objToString(list.get(1), "")),
					ac.getMessage().getText(Tool.objToString(list.get(2), "")),
					ac.getMessage().getText(Tool.objToString(list.get(3), "")));
		ac.isStartGame = false;
		StartGame = false;
		ReadyisModel = false;
	}

	/**
	 * 游戏时间到 所有玩家退出游戏
	 */
	public void QuitGame() {
		for (Player player : gamePlayers)
			try {
				QuitGame(player, false, false, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		gamePlayers = new ArrayList<>();
		reload();
	}

	/**
	 * 玩家退出游戏的处理
	 * 
	 * @param player
	 */
	public void QuitGame(Player player, boolean isQuitServer, boolean isRemove, boolean isMsg) {
		if (!ac.isStartGame)
			return;
		MyPlayer myPlayer = ac.getPlayers(player.getName());
		if (myPlayer.GameModel || myPlayer.ReadyModel) {
			int honor = 0, score = 0;
			for (EffectItem item : myPlayer.items) {
				honor += item.gameData.honor;
				score += item.gameData.score;
			}
			if (isQuitServer) {
				honor = -10;
				score = 0;
			}
			honor += myPlayer.gameData.honor;
			score += myPlayer.gameData.score;
			if (isRemove)
				gamePlayers.remove(player);
			player.removeAllEffects();
			myPlayer.addHonor(honor).addScore(score).loadInventory().loadXYZ().loadGameMode();
			myPlayer.items = new ArrayList<>();
			myPlayer.GameModel = false;
			myPlayer.ReadyModel = false;
			myPlayer.gameData = null;
			ac.setPlayers(player, myPlayer);
			if (!AdminStopGame) {
				int bs = ac.getConfig().getInt("给予倍率");
				boolean sb = ac.getConfig().getBoolean("给予惩罚");
				if (!isQuitServer) {
					if (score >= bs) {
						ac.getEconomy().addMoney(player, (double) score / bs);
						player.sendMessage(ac.getMessage().getSon("Game", "结束奖励",
								new String[] { "{Player}", "{Money}", "{MyMoney}" },
								new Object[] { player.getName(), (double) score / bs, myPlayer.getMoney() }));
					} else if (score > 0 && score / bs < 0 && sb) {
						ac.getEconomy().reduceMoney(player, (double) score / bs);
						player.sendMessage(ac.getMessage().getSon("Game", "结束惩罚",
								new String[] { "{Player}", "{Money}", "{MyMoney}" },
								new Object[] { player.getName(), (double) score / bs, myPlayer.getMoney() }));
					}
				} else {
					double sbsbsbs = score <= ac.getConfig().getDouble("游戏费用") ? ac.getConfig().getDouble("游戏费用")
							: score;
					ac.getEconomy().reduceMoney(player, sbsbsbs);
					player.sendMessage(
							ac.getMessage().getSon("Game", "退出惩罚", new String[] { "{Player}", "{Money}", "{MyMoney}" },
									new Object[] { player.getName(), sbsbsbs, myPlayer.getMoney() }));
				}
			}
			if (isMsg)
				player.sendMessage(ac.getMessage().getSon("Game", "游戏结束",
						new String[] { "{Player}", "{Money}", "{Score}", "{Honor}" },
						new Object[] { player.getName(), MyPlayer.getMoney(player.getName()), score, honor }));
		}
	}

	/**
	 * 游戏主线程
	 * 
	 * @author Winfxk
	 */
	private class GameMainThread extends Thread {
		private int GameTime;

		public GameMainThread() {
			GameTime = ac.getConfig().getInt("游戏时间");
		}

		@Override
		public void run() {
			int BjBl = ac.getConfig().getInt("补给倍率");
			BjBl = BjBl <= 0 ? 1 : BjBl;
			new BuffThread().start();
			MyPlayer myPlayer;
			String[] Key = { "{Count}" };
			Object[] D;
			for (Player player : gamePlayers) {
				if (player == null)
					continue;
				myPlayer = ac.getPlayers(player.getName());
				if (myPlayer == null)
					continue;
				myPlayer.GameModel = true;
				myPlayer.ReadyModel = false;
				myPlayer.saveXYZ();
				player.getInventory().addItem(ac.getEffecttor().getAK());
				player.teleport(location);
				ac.setPlayers(player, myPlayer);
				player.sendTitle(getMessage("开始游戏", player));
			}
			Timesleep = ac.getConfig().getInt("掉落间隔");
			for (int i = 0; i < gamePlayers.size(); i++)
				location.level.dropItem(new Vector3(Tool.getRand((int) mostConfig.MinX + 1, (int) mostConfig.MaxX - 1),
						Tool.getRand((int) mostConfig.MinY + 1, (int) mostConfig.MaxY - 1), mostConfig.MinZ + 2),
						getItem(), null, true, 3);
			List<?> list;
			Map<String, Object> GameMessage;
			Level signLevel;
			try {
				while (StartGame && GameTime >= 0) {
					if (AdminStopGame) {
						String[] Key2 = { "{Player}", "{Money}", "{Admin}", "{Count}" };
						ac.getMostBrain().getLogger().info(ac.getMessage().getSon("Game", "管理员终止游戏", Key2,
								new Object[] { "", "", sender.getName(), gamePlayers.size() }));
						for (Player player : gamePlayers)
							player.sendMessage(ac.getMessage().getSon("Game", "管理员终止游戏", Key2,
									new Object[] { player.getName(), MyPlayer.getMoney(player.getName()),
											sender.getName(), gamePlayers.size() }));
						break;
					}
					if (gamePlayers.size() <= 0) {
						ac.getMostBrain().getLogger().info(getMessage("游戏终止"));
						break;
					}
					GameMessage = (Map<String, Object>) ac.getMessage().getConfig().get("Game");
					list = (List<?>) GameMessage.get("StartGameSign");
					signLevel = Server.getInstance().getLevelByName(mostConfig.Level);
					if (signLevel != null) {
						D = new Object[] { gamePlayers.size() };
						Tool.setSign(signLevel.getBlock(mostConfig.getStart()),
								ac.getMessage().getText(Tool.objToString(list.get(0), ""), Key, D),
								ac.getMessage().getText(Tool.objToString(list.get(1), ""), Key, D),
								ac.getMessage().getText(Tool.objToString(list.get(2), ""), Key, D),
								ac.getMessage().getText(Tool.objToString(list.get(3), ""), Key, D));
					}
					Timesleep--;
					for (Player player : gamePlayers) {
						if (Timesleep < 5 && Timesleep > 0) {
							player.sendMessage(ac.getMessage().getSon("Game", "即将发送补给",
									new String[] { "{Player}", "{Money}", "{SleepTime}" },
									new Object[] { player.getName(), MyPlayer.getMoney(player.getName()), Timesleep }));
						}
						if (Timesleep == 0) {
							player.sendMessage(ac.getMessage().getSon("Game", "正在发送补给", player));
							for (int sb = 0; sb < BjBl; sb++)
								location.level.dropItem(
										new Vector3(Tool.getRand((int) mostConfig.MinX + 1, (int) mostConfig.MaxX - 1),
												Tool.getRand((int) mostConfig.MinY + 1, (int) mostConfig.MaxY - 1),
												mostConfig.MinZ + 2),
										getItem(), null, true, 3);
						}
						if ((GameTime > 3 && GameTime < 10) || GameTime == 25 || GameTime == 30)
							player.sendTitle(ac.getMessage().getSon("Game", "游戏即将结束",
									new String[] { "{Player}", "{Money}", "{GameTime}" },
									new Object[] { player.getName(), MyPlayer.getMoney(player.getName()), GameTime }));
						if (GameTime <= 3)
							player.sendTitle(Tool.getRandColor() + GameTime);
					}
					Timesleep = Timesleep <= 0 ? ac.getConfig().getInt("掉落间隔") : Timesleep;
					GameTime--;
					sleep(1000);
					if (GameTime < 0)
						break;
				}
				QuitGame();
			} catch (

			InterruptedException e) {
				e.printStackTrace();
			}
			super.run();
		}

		public Item getItem() {
			List<EffectItem> effectItems = ac.getEffecttor().getList();
			EffectItem effectItem = effectItems.get(Tool.getRand(0, effectItems.size() - 1));
			Item item = ac.getEffecttor().getItem(effectItem);
			item.setCount(Tool.getRand(1, 3));
			return item;
		}

		private class BuffThread extends Thread {
			@Override
			public void run() {
				try {
					MyPlayer myPlayer;
					while (StartGame) {
						for (Player player : gamePlayers) {
							myPlayer = ac.getPlayers(player.getName());
							if (myPlayer != null && myPlayer.items != null)
								for (EffectItem item : myPlayer.items)
									if (item != null)
										item.Wake();
						}
						sleep(1000);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				super.run();
			}
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
	public GameHandle giveBuffs(Player player) {
		if (!gamePlayers.contains(player))
			return this;
		List<EffectItem> items = ac.getEffecttor().getList();
		player.getInventory().addItem(ac.getEffecttor().getItem(items.get(Tool.getRand(0, items.size() - 1))));
		return this;
	}

	/**
	 * 给予玩家一个Buff
	 * 
	 * @param player
	 * @param effectItem
	 */
	public GameHandle giveBuffs(Player player, EffectItem effectItem) {
		if (!gamePlayers.contains(player) || effectItem == null)
			return this;
		player.getInventory().addItem(ac.getEffecttor().getItem(effectItem));
		return this;
	}

	/**
	 * 返回玩家持有的效果数量
	 * 
	 * @param player
	 * @return
	 */
	public int getEffects(Player player) {
		MyPlayer myPlayer = ac.getPlayers(player.getName());
		if (myPlayer.ReadyModel || myPlayer.GameModel)
			return myPlayer.items.size();
		return 0;
	}

	/**
	 * 清空一个玩家的Buff
	 * 
	 * @param player
	 */
	public GameHandle clearBuffs(Player player) {
		if (!gamePlayers.contains(player))
			return this;
		MyPlayer myPlayer = ac.getPlayers(player.getName());
		myPlayer.items = new ArrayList<>();
		ac.setPlayers(player, myPlayer);
		return this;
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
