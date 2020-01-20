package cn.epicfx.winfxk.mostbrain;

import cn.epicfx.winfxk.mostbrain.effect.EffectItem;
import cn.epicfx.winfxk.mostbrain.game.MostConfig;
import cn.epicfx.winfxk.mostbrain.game.SettingGame;
import cn.nukkit.Player;
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
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerInteractEvent.Action;
import cn.nukkit.event.player.PlayerItemConsumeEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.player.PlayerRespawnEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
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
		ac.setPlayers(player, new MyPlayer(player));
		String string = msg.getSun("Event", "PlayerJoin", "Tip", player);
		if (string != null && !string.isEmpty())
			if (!player.isOp() && ac.isGameSettingUp)
				player.sendMessage(string);
		string = msg.getSun("Event", "PlayerJoin", "OpTip", player);
		if (string != null && !string.isEmpty())
			if (player.isOp() && !ac.isGameSettingUp)
				player.sendMessage(string);
		MyPlayer myPlayer = ac.getPlayers(player.getName());
		myPlayer.setInventory();
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
		if (ac.isPlayers(player)) {
			if (ac.SettingModel == false
					|| !(ac.SettingModel == true && player.getName().equals(ac.setPlayer.getName()))
					|| !(myPlayer.GameModel || myPlayer.ReadyModel)) {
				myPlayer.config.save();
				ac.setPlayers(player, new MyPlayer(player));
			}
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
			ac.gameEvent.QuitGame(e);
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
	public void on(BlockPlaceEvent e) {
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
