package com.revo.auth.data;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import org.bukkit.ChatColor;

import com.revo.auth.Plugin;

/*
 * FileManager class 
 * 
 * Created by Revo
 */

public final class FileManager {

	/*
	 * Data
	 */
	
	private static File config = new File(Plugin.get().getDataFolder(), "config.yml");
	private static File users = new File(Plugin.get().getDataFolder() + "/data", "users");
	private static File sessions = new File(Plugin.get().getDataFolder() + "/data", "sessions");
	private static File data = new File(Plugin.get().getDataFolder(), "data");
	private static File changelog = new File(Plugin.get().getDataFolder(), "changelog.txt");
	
	/*
	 * Check file exists
	 */
	
	public static void check() {
		if(!config.exists()) Plugin.get().saveDefaultConfig();
		if(!data.exists() && DataManager.getBaseType().equals(BaseType.YAML)) data.mkdir();
		if(!users.exists() && DataManager.getBaseType().equals(BaseType.YAML)) users.mkdir();
		if(!sessions.exists() && DataManager.getBaseType().equals(BaseType.YAML)) sessions.mkdir();
		try{
			changelog.createNewFile();
			PrintWriter writer = new PrintWriter(changelog);
			InputStream input = Plugin.get().getResource("changelog.txt");
			while(true) {
				int x = input.read();
				if(x == -1) break;
				writer.write(x);
				writer.flush();
			}
			writer.close();
		}catch(IOException e) {
			Plugin.get().getLogger().info(ChatColor.RED+"Error while creating changelog!");
		}
		
	}
	
	/*
	 * Getters
	 */

	public static File getConfig() {
		return config;
	}

	public static File getUsers() {
		return users;
	}

	public static File getSessions() {
		return sessions;
	}
	
}
