package com.revo.auth.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;

import com.revo.auth.user.User;
import com.revo.auth.user.UserManager;

/*
 * Player interact listener 
 * 
 * Created by Revo
 */

public class PlayerInteractListener implements Listener {

	/*
	 * onChatInteract
	 */

	@EventHandler(priority = EventPriority.HIGH)
	public void onChatInteract(AsyncPlayerChatEvent e) {
		if (!this.checkLogged(e))
			e.setCancelled(true);
	}

	/*
	 * onCommandType
	 */

	@EventHandler(priority = EventPriority.HIGH)
	public void onCommandType(PlayerCommandPreprocessEvent e) {
		if (e.getMessage().startsWith("/recovery") || e.getMessage().startsWith("/recovery".toUpperCase())
				|| e.getMessage().startsWith("/l") || e.getMessage().startsWith("/l".toUpperCase())
				|| e.getMessage().startsWith("/r") || e.getMessage().startsWith("/r".toUpperCase()))
			return;
		if (!this.checkLogged(e))
			e.setCancelled(true);
	}

	/*
	 * onMove
	 */

	@EventHandler(priority = EventPriority.HIGH)
	public void onMove(PlayerMoveEvent e) {
		if (!this.checkLogged(e))
			e.setCancelled(true);
	}
	
	/*
	 * onPlayerTeleport
	 */
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onTeleport(PlayerTeleportEvent e) {
		if (!this.checkLogged(e))
			e.setCancelled(true);
	}
	
	/*
	 * onPlayerPortalInteract
	 */
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerPortalInteract(PlayerPortalEvent e) {
		if (!this.checkLogged(e))
			e.setCancelled(true);
	}

	/*
	 * onDropitem
	 */

	@EventHandler(priority = EventPriority.HIGH)
	public void onDropItem(PlayerDropItemEvent e) {
		if (!this.checkLogged(e))
			e.setCancelled(true);
	}

	/*
	 * onPickupitem
	 */

	@EventHandler(priority = EventPriority.HIGH)
	public void onPickupItem(EntityPickupItemEvent e) {
		if (!this.checkLogged(e))
			e.setCancelled(true);
	}

	/*
	 * onInteract
	 */

	@EventHandler(priority = EventPriority.HIGH)
	public void onInteract(PlayerInteractEvent e) {
		if (!this.checkLogged(e))
			e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onInteract(PlayerInteractEntityEvent e) {
		if (!this.checkLogged(e))
			e.setCancelled(true);
	}
	

	/*
	 * onDamage
	 */

	@EventHandler(priority = EventPriority.HIGH)
	public void onDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			if (!this.checkLogged(e))
				e.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onDamage(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			if (!UserManager.get(e.getDamager().getName()).isLogged())
				e.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onDamage(EntityShootBowEvent e) {
		if (e.getEntity() instanceof Player) {
			if (!this.checkLogged(e))
				e.setCancelled(true);
		}
	}

	/*
	 * inventoryAccess
	 */

	@EventHandler(priority = EventPriority.HIGH)
	public void onInventoryAccess(InventoryOpenEvent e) {
		if (!UserManager.get(e.getPlayer().getName()).isLogged())
			e.setCancelled(true);
	}
	
	/*
	 * Vehicle Destroy
	 */

	@EventHandler(priority = EventPriority.HIGH)
	public void onVehicleDestroy(VehicleDestroyEvent e) {
		if (!UserManager.get(e.getAttacker().getName()).isLogged())
			e.setCancelled(true);
	}

	/*
	 * Check players is logged!
	 */

	private boolean checkLogged(EntityEvent e) {
		User u = UserManager.get(e.getEntity().getName());
		if (!u.isLogged())
			return false;
		return true;
	}

	private boolean checkLogged(PlayerEvent e) {
		User u = UserManager.get(e.getPlayer().getName());
		if (!u.isLogged())
			return false;
		return true;
	}
	
}
