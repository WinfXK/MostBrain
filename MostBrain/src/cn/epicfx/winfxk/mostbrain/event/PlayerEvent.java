package cn.epicfx.winfxk.mostbrain.event;

import cn.epicfx.winfxk.mostbrain.Activate;
import cn.epicfx.winfxk.mostbrain.Message;
import cn.epicfx.winfxk.mostbrain.MyPlayer;
import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.inventory.InventoryClickEvent;
import cn.nukkit.event.inventory.InventoryMoveItemEvent;
import cn.nukkit.event.player.PlayerDeathEvent;
import cn.nukkit.event.player.PlayerDropItemEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.player.PlayerRespawnEvent;
import cn.nukkit.item.Item;
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

	@EventHandler
	public void onInventoryMoveItem(InventoryMoveItemEvent e) {
		if (!ac.getMostBrain().isEnabled())
			return;
		if (ac.SettingModel) {
			Item item = e.getItem();
			if (item.getId() == 290 && item.getDamage() == 0)
				if ((item.getCustomBlockData() == null ? new CompoundTag() : item.getCustomBlockData())
						.getBoolean(ac.getMostBrain().getName()))
					e.setCancelled();
			return;
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (!ac.getMostBrain().isEnabled())
			return;
		Player player = e.getPlayer();
		if (ac.SettingModel && player.getName().equals(ac.setPlayer.getName())) {
			e.setCancelled();
			return;
		}
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
		if (string == null || string.isEmpty())
			return;
		player.sendMessage(string);
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
		if (ac.isPlayers(player)) {
			MyPlayer myPlayer = ac.getPlayers(player.getName());
			myPlayer.config.save();
			ac.setPlayers(player, new MyPlayer(player));
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
		if (ac.SettingModel && player.getName().equals(ac.setPlayer.getName())) {
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
		if (ac.SettingModel && player.getName().equals(ac.setPlayer.getName())) {
			e.setCancelled();
			return;
		}
	}

}
