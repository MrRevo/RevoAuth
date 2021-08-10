package com.revo.auth;

import java.util.Arrays;

import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.revo.auth.command.Executor;
import com.revo.auth.data.BaseType;
import com.revo.auth.data.DataManager;
import com.revo.auth.listener.PlayerInteractListener;
import com.revo.auth.listener.PlayerLoggingListener;

import net.md_5.bungee.api.ChatColor;

/* 
 * Main plugin class
 * 
 * Created by Revo
 */

public final class Plugin extends JavaPlugin{
	
	/*
	 * Data
	 */
	
	private static Plugin instance;

	/*
	 * On plugin disable
	 */

	@Override
	public void onDisable() {
		//DATA
		DataManager.save();
		//RELOAD FIX
		fixNotLoggedPlayers();
	}

	/*
	 * On plugin enable
	 */
	
	@Override
	public void onEnable() {
		//INSTANCE
		instance = this;
		//DATA
		DataManager.initBaseType();
		if(DataManager.getBaseType() == BaseType.UNDEFINED) {
			this.getLogger().info(ChatColor.RED+"Only YAML, SQL base supported! Check your config file!");
			this.getServer().getPluginManager().disablePlugin(this);
		}
		DataManager.load();
		//COMMANDS
		CommandExecutor executor = new Executor();
		this.getCommand("rauth").setExecutor(executor);
		this.getCommand("login").setAliases(Arrays.asList("l"));
		this.getCommand("login").setExecutor(executor);
		this.getCommand("register").setExecutor(executor);
		this.getCommand("register").setAliases(Arrays.asList("r"));
		this.getCommand("email").setExecutor(executor);
		this.getCommand("recovery").setExecutor(executor);
		this.getCommand("passwordchange").setExecutor(executor);
		this.getCommand("autologin").setExecutor(executor);
		//EVENTS
		this.getServer().getPluginManager().registerEvents(new PlayerInteractListener(), instance);
		this.getServer().getPluginManager().registerEvents(new PlayerLoggingListener(), instance);
	}
	
	/*
	 * Getting instance of plugin
	 */
	
	public static Plugin get() {
		if(instance == null) instance = new Plugin();
		return instance;
	}
	
	/*
	 * Fix Not Logged Players
	 */
	
	private void fixNotLoggedPlayers() {
		for(Player p : PlayerLoggingListener.notLoggedPlayerFixList)
			p.kickPlayer(ChatColor.translateAlternateColorCodes('&', DataManager.getConfig().getString("prefix")+"&cZALOGUJ SIE PONOWNIE!"));
	}

}
