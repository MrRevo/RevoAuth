package com.revo.auth.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.revo.auth.Plugin;
import com.revo.auth.data.DataManager;
import com.revo.auth.session.SessionManager;
import com.revo.auth.user.UserContainer;
import com.revo.auth.user.UserManager;

/*
 * Player logout listener 
 * 
 * Created by Revo
 */

public final class PlayerLoggingListener implements Listener {
	
	/*
	 * Data
	 */
	
	public static List<Player> notLoggedPlayerFixList = new ArrayList<Player>();
	
	/*
	 * On Join
	 */
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onJoin(PlayerJoinEvent e) {
		e.setJoinMessage(ChatColor.translateAlternateColorCodes('&', DataManager.getConfig().getString("join_message").replaceAll("@", e.getPlayer().getName())));
		if(SessionManager.isCurrentSession(e.getPlayer())) {
			SessionManager.updateSession(e.getPlayer());
			UserManager.get(e.getPlayer().getName()).setLogged(true);
			e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', DataManager.getConfig().getString("prefix")+"&aWitaj ponowanie!"));
			return;
		}
		UserManager.get(e.getPlayer().getName()).setLogged(false);
		new UserContainer(e.getPlayer()).copyToClass();
		e.getPlayer().getInventory().clear();
		e.getPlayer().setAllowFlight(true);
		e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999999, 0));
		e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 999999999, 0));
		new Thread(new Runnable() {
			@SuppressWarnings("static-access")
			@Override
			public void run() {
				while(true) {
					if(!Bukkit.getOnlinePlayers().contains(e.getPlayer())) break;
					if(UserManager.get(e.getPlayer().getName()).isLogged()) break;
					if(!notLoggedPlayerFixList.contains(e.getPlayer()))notLoggedPlayerFixList.add(e.getPlayer());
					if(!(UserManager.get(e.getPlayer().getName()).getPassword().length() > 0)) e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', DataManager.getConfig().getString("prefix")+"&cMusisz sie zarejestrowac! /register <haslo> <powtorz_haslo>"));
					else e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', DataManager.getConfig().getString("prefix")+"&cMusisz sie zalogowac! /login <haslo>"));
					try {
						Thread.currentThread().sleep(5000);
					} catch (InterruptedException ie) {}
				}
				Thread.currentThread().interrupt();
			}
		}).start();
		new BukkitRunnable() {
			@Override
			public void run() {
				 if(!UserManager.get(e.getPlayer().getName()).isLogged())e.getPlayer().kickPlayer(ChatColor.translateAlternateColorCodes('&', DataManager.getConfig().getString("prefix")+"&cMinal czas logowania!"));
			}
			
		}.runTaskLater(Plugin.get(), DataManager.getConfig().getInt("timeout") * 20);
	}
	
	/*
	 * On Logout
	 */
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onLogout(PlayerQuitEvent e) {
		e.setQuitMessage(ChatColor.translateAlternateColorCodes('&', DataManager.getConfig().getString("quit_message").replaceAll("@", e.getPlayer().getName())));
		if(!UserManager.get(e.getPlayer().getName()).isLogged()) UserContainer.get(e.getPlayer()).loadFromClass();
	}
	
}
