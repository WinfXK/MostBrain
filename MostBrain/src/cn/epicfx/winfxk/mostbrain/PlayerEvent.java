package cn.epicfx.winfxk.mostbrain;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import cn.epicfx.winfxk.mostbrain.effect.EffectItem;
import cn.epicfx.winfxk.mostbrain.game.MostConfig;
import cn.epicfx.winfxk.mostbrain.game.SettingGame;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.inventory.InventoryClickEvent;
import cn.nukkit.event.player.PlayerDeathEvent;
import cn.nukkit.event.player.PlayerDropItemEvent;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerInteractEvent.Action;
import cn.nukkit.event.player.PlayerItemConsumeEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.player.PlayerRespawnEvent;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseModal;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * @author Winfxk
 */
public class PlayerEvent implements Listener {
	private Activate ac;
	private Message msg;

	/**
	 * 监听玩家事件
	 *
	 * @param ac
	 */
	public PlayerEvent(Activate ac) {
		this.ac = ac;
		msg = ac.getMessage();
	}

	/**
	 * 表单接受事件
	 *
	 * @param e
	 */
	@EventHandler
	public void onFormResponded(PlayerFormRespondedEvent e) {
		FormResponse data = e.getResponse();
		int ID = e.getFormID();
		FormID f = ac.getFormID();
		Player player = e.getPlayer();
		if (player == null || e.wasClosed() || e.getResponse() == null
				|| (!(e.getResponse() instanceof FormResponseCustom) && !(e.getResponse() instanceof FormResponseSimple)
						&& !(e.getResponse() instanceof FormResponseModal)))
			return;
		if (f.getID(0) == ID)
			ac.makeForm.disMain(player, (FormResponseSimple) data);
		else if (f.getID(2) == ID && ((FormResponseSimple) data).getClickedButtonId() == 0)
			ac.gameHandle.QuitGame(player, false, true, false, false);
	}

	/**
	 * 背包点击事件
	 *
	 * @param e
	 */
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (!ac.getMostBrain().isEnabled())
			return;
		Player player = e.getPlayer();
		if (ac.SettingModel)
			if (player.getName().equals(ac.setPlayer.getName())) {
				Item item = e.getSourceItem();
				CompoundTag tag = item.getCustomBlockData() == null ? new CompoundTag() : item.getCustomBlockData();
				if (tag.getBoolean(ac.getMostBrain().getName())) {
					e.setCancelled();
					return;
				}
			}
	}

	@EventHandler
	public void onItemConsume(PlayerItemConsumeEvent e) {
		if (ac.isStartGame)
			EffectItem.receiveItemConsume(e);
	}

	/**
	 * 玩家丢东西事件
	 *
	 * @param e
	 */
	@EventHandler
	public void onDropItem(PlayerDropItemEvent e) {
		if (!ac.getMostBrain().isEnabled())
			return;
		Player player = e.getPlayer();
		if (ac.SettingModel && player.getName().equals(ac.setPlayer.getName())) {
			e.setCancelled();
			return;
		}
	}

	/**
	 * 监听玩家进服事件
	 *
	 * @param e
	 */
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if (!ac.getMostBrain().isEnabled())
			return;
		Player player = e.getPlayer();
		MyPlayer myPlayer;
		ac.setPlayers(player, myPlayer = new MyPlayer(player));
		String string = msg.getSun("Event", "PlayerJoin", "Tip", player);
		if (string != null && !string.isEmpty())
			if (!player.isOp() && ac.isGameSettingUp)
				player.sendMessage(string);
		string = msg.getSun("Event", "PlayerJoin", "OpTip", player);
		if (string != null && !string.isEmpty())
			if (player.isOp() && !ac.isGameSettingUp)
				player.sendMessage(string);
		MostConfig mc = ac.getMostConfig();
		if (ac.isGameSettingUp && mc != null) {
			if (myPlayer.config.get("Inventory") != null)
				myPlayer.loadInventory();
			double x = player.getX(), y = player.getY(), z = player.getZ();
			if (x > mc.MinX && x < mc.MaxX && y > mc.MinY && y < mc.MaxY && z > mc.MinZ && z < mc.MaxZ
					&& player.getLevel().getFolderName().equals(mc.StartLevel)) {
				Level level = Server.getInstance().getLevelByName(mc.Level);
				Vector3 v = mc.getStart();
				if (level != null)
					new RespawnThread(player, new Location(v.x, v.y, v.z, level)).start();
			}
		}
		if (ac.config.getBoolean("物品防御")) {
			Map<Integer, Item> map = player.getInventory().getContents();
			Map<Integer, Item> map1 = new HashMap<>();
			if (map != null) {
				for (Integer i : map.keySet()) {
					Item item = map.get(i);
					if (item == null || item.getId() == 0)
						continue;
					CompoundTag nbt = item.getNamedTag();
					if (nbt == null)
						continue;
					if (nbt.getString(ac.getMostBrain().getName()) == null)
						map1.put(i, item);
				}
				player.getInventory().setContents(map);
			}
		}
	}

	/**
	 * 监听都比重生事件
	 *
	 * @param e
	 */
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		if (!ac.getMostBrain().isEnabled())
			return;
		Player player = e.getPlayer();
		MyPlayer myPlayer = ac.getPlayers(player.getName());
		if (myPlayer != null && ac.isStartGame) {
			int Mh = ac.config.getInt("游戏最大血量");
			int h = ac.config.getInt("游戏血量");
			if (myPlayer.ReadyModel) {
				if (player.getHealth() <= 0 || player.getMaxHealth() <= 0) {
					player.setMaxHealth(Mh);
					player.setHealth(h);
				}
				Vector3 v = ac.getMostConfig().getStart();
				Level level = Server.getInstance().getLevelByName(ac.getMostConfig().Level);
				if (level != null)
					new RespawnThread(player, new Location(v.x, v.y, v.z, level)).start();

			}
			if (myPlayer.GameModel) {
				myPlayer.RespawnTime = Instant.now();
				ac.setPlayers(player.getName(), myPlayer);
				if (player.getHealth() <= 0 || player.getMaxHealth() <= 0) {
					player.setMaxHealth(Mh);
					player.setHealth(h);
				}
				Vector3 v = ac.getMostConfig().getSpawn();
				Level level = Server.getInstance().getLevelByName(ac.getMostConfig().StartLevel);
				if (level != null)
					new RespawnThread(player, new Location(v.x, v.y, v.z, level)).start();
			}
		}
		if (player.getHealth() <= 0 || player.getMaxHealth() <= 0) {
			player.setMaxHealth(20);
			player.setHealth(20);
		}
	}

	/**
	 * 玩家传送事件延迟
	 *
	 * @author Winfxk
	 */
	private class RespawnThread extends Thread {
		private Player player;
		private Location l;

		public RespawnThread(Player player, Location l) {
			this.l = l;
			this.player = player;
		}

		@Override
		public void run() {
			try {
				sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			player.teleport(l);
		}
	}

	/**
	 * 监听玩家退服事件
	 *
	 * @param e
	 */
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if (!ac.getMostBrain().isEnabled())
			return;
		Player player = e.getPlayer();
		MyPlayer myPlayer = ac.getPlayers(player.getName());
		if (ac.SettingModel && player.getName().equals(ac.setPlayer.getName())) {
			ac.setPlayer = null;
			ac.SettingModel = false;
			player.getInventory().setItem(0, ac.settingGame.sbItem);
			if (ac.settingGame.start != null)
				ac.settingGame.start.getLevel().setBlock(ac.settingGame.start.getLocation(), ac.settingGame.start);
			player.setGamemode(ac.settingGame.gameMode);
			ac.getMostBrain().getLogger().warning(ac.settingGame.getMessage("管理员退出"));
			ac.settingGame = null;
		} else if (ac.gameEvent != null && ac.isStartGame)
			if (myPlayer != null && (myPlayer.ReadyModel || myPlayer.GameModel))
				ac.gameHandle.QuitGame(player, true, true, false, true);
		if (ac.isPlayers(player))
			ac.removePlayers(player);
	}

	/**
	 * 监听玩家死亡事件
	 *
	 * @param e
	 */
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		if (!ac.getMostBrain().isEnabled())
			return;
		Player player = e.getEntity();
		if (!player.isPlayer())
			return;
		MyPlayer myPlayer = ac.getPlayers(player.getName());
		if (myPlayer != null && myPlayer.GameModel)
			myPlayer.addDeath();
	}

	/**
	 * 监听玩家付香商骇事件
	 *
	 * @param e
	 */
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (!ac.getMostBrain().isEnabled())
			return;
		Entity entity = e.getEntity();
		if (!(entity instanceof Player))
			return;
		Player player = (Player) e.getEntity();
		if (ac.SettingModel && player.getName().equals(ac.setPlayer.getName())) {
			e.setCancelled();
			return;
		}
		if (ac.isStartGame)
			EffectItem.receiveDamage(e);
	}

	/**
	 * 监听玩家点击交互事件
	 *
	 * @param e
	 */
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if (!ac.getMostBrain().isEnabled())
			return;
		Player player = e.getPlayer();
		Block block = e.getBlock();
		Item item = e.getItem();
		Action action = e.getAction();
		if (action == null || item == null || block == null || action.equals(Action.PHYSICAL))
			return;
		if (ac.SettingModel) {
			if (player.getName().equals(ac.setPlayer.getName())) {
				CompoundTag tag = item.getCustomBlockData() == null ? new CompoundTag() : item.getCustomBlockData();
				if (tag.getBoolean(ac.getMostBrain().getName())) {
					(ac.settingGame == null ? ac.settingGame = new SettingGame(ac, player) : ac.settingGame).Click(e);
					e.setCancelled();
					return;
				}
			}
			if (ac.settingGame.start != null && block.getLocation().equals(ac.settingGame.start.getLocation())
					|| ac.settingGame.getStartSign() != null
					&& block.getLocation().equals(ac.settingGame.getStartSign().getLocation())) {
				e.setCancelled();
				return;
			}
		}
		if (ac.isGameSettingUp)
			if (ac.getMostConfig() != null) {
				MostConfig config = ac.getMostConfig();
				Location location = block.getLocation();
				if (location.level.getFolderName().equals(config.Level))
					if (location.x == config.Start.get("X") && location.y == config.Start.get("Y")
					&& location.z == config.Start.get("Z"))
						ac.gameEvent.Start(e);
				EffectItem.receiveItemConsume(new PlayerItemConsumeEvent(player, e.getItem()));
				if (ac.getMostConfig().isNotBreakBlock(block))
					e.setCancelled();
			}
	}

	/**
	 * 方块放置事件
	 *
	 * @param e
	 */
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if (!ac.getMostBrain().isEnabled())
			return;
		Player player = e.getPlayer();
		Block block = e.getBlock();
		if (ac.SettingModel) {
			if (player.getName().equals(ac.setPlayer.getName())) {
				Item item = e.getItem();
				CompoundTag tag = item.getCustomBlockData() == null ? new CompoundTag() : item.getCustomBlockData();
				if (tag.getBoolean(ac.getMostBrain().getName())) {
					e.setCancelled();
					return;
				}
			}
			if ((ac.settingGame.start != null && block.getLocation().equals(ac.settingGame.start.getLocation())
					|| ac.settingGame.getStartSign() != null
					&& block.getLocation().equals(ac.settingGame.getStartSign().getLocation()))
					&& !player.getName().equals(ac.setPlayer.getName())) {
				e.setCancelled();
				return;
			}
		}
		if (ac.isGameSettingUp)
			if (ac.getMostConfig() != null && ac.getMostConfig().isNotBreakBlock(block)) {
				e.setCancelled();
				return;
			}
	}

	/**
	 * 玩家破坏方块事件
	 *
	 * @param e
	 */
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if (!ac.getMostBrain().isEnabled())
			return;
		Player player = e.getPlayer();
		Block block = e.getBlock();
		if (ac.SettingModel) {
			if (player.getName().equals(ac.setPlayer.getName())) {
				Item item = e.getItem();
				CompoundTag tag = item.getCustomBlockData() == null ? new CompoundTag() : item.getCustomBlockData();
				if (tag.getBoolean(ac.getMostBrain().getName())) {
					(ac.settingGame == null ? ac.settingGame = new SettingGame(ac, player) : ac.settingGame).Click(e);
					e.setCancelled();
					return;
				}
			}
			if ((ac.settingGame.start != null && block.getLocation().equals(ac.settingGame.start.getLocation())
					|| ac.settingGame.getStartSign() != null
					&& block.getLocation().equals(ac.settingGame.getStartSign().getLocation()))
					&& !player.getName().equals(ac.setPlayer.getName())) {
				e.setCancelled();
				return;
			}
		}
		if (ac.isGameSettingUp)
			if (ac.getMostConfig() != null) {
				MostConfig config = ac.getMostConfig();
				Location location = block.getLocation();
				if (location.level.getFolderName().equals(config.Level))
					if (location.x == config.Start.get("X") && location.y == config.Start.get("Y")
					&& location.z == config.Start.get("Z"))
						ac.gameEvent.Start(e);
				if (ac.getMostConfig().isNotBreakBlock(block))
					e.setCancelled();
				return;
			}
	}
}
