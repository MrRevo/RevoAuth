package com.revo.auth.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/*
 * User container
 * 
 * Created By Revo
 */

public final class UserContainer {
	
	/*
	 * Data
	 */

	
	private static List<UserContainer> containers = new ArrayList<UserContainer>();
	private Player player;
	private Map<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();
	private ItemStack helmet;
	private ItemStack chestplate;
	private ItemStack leggings;
	private ItemStack boots;
	private ItemStack shield;
	
	
	/*
	 * Constructor
	 */
	
	public UserContainer(Player player) {
		this.player = player;
		add(this);
	}
	
	
	/*
	 * Copy invnetory
	 */
	
	public void copyToClass() {
		for(int x =0; x<=35;x++) 
			items.put(x, player.getInventory().getItem(x));
		this.helmet = player.getInventory().getHelmet();
		this.chestplate = player.getInventory().getChestplate();
		this.leggings = player.getInventory().getLeggings();
		this.boots = player.getInventory().getBoots();
		this.shield = player.getInventory().getItemInOffHand();
	}
	
	public void loadFromClass() {
		for(int x =0; x<=35;x++) {
			if(items.isEmpty()) break;
			player.getInventory().setItem(x, items.get(x));
		}
		player.getInventory().setHelmet(helmet);
		player.getInventory().setChestplate(chestplate);
		player.getInventory().setLeggings(leggings);
		player.getInventory().setBoots(boots);
		player.getInventory().setItemInOffHand(shield);
	}

	/*
	 *  Getter
	 */

	public static List<UserContainer> getContainers() {
		return containers;
	}
	public static void add(UserContainer container) {
		for(UserContainer c : containers)
			if(c.getPlayer().equals(container.getPlayer())) 
				return;
		containers.add(container);
	}
	
	public static UserContainer get(Player p) {
		for(UserContainer c : containers)
			if(c.getPlayer().equals(p)) return c;
		return null;
	}
	
	public Player getPlayer() {
		return player;
	}
	
}
