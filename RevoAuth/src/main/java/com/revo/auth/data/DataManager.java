package com.revo.auth.data;

import org.bukkit.configuration.file.FileConfiguration;

import com.revo.auth.Plugin;
import com.revo.auth.session.SessionManager;
import com.revo.auth.user.UserManager;

/*
 * DataManager class 
 * 
 * Created by Revo
 */

public final class DataManager {

	/*
	 * Data
	 */
	
	private static FileConfiguration config = Plugin.get().getConfig();
	private static BaseType baseType = BaseType.UNDEFINED;
	
	/*
	 * Check base configuration
	 */
	
	public static void initBaseType() {
		if(DataManager.getConfig().getString("base_type").equalsIgnoreCase("yaml")) baseType = BaseType.YAML;
		if(DataManager.getConfig().getString("base_type").equalsIgnoreCase("sql")) baseType = BaseType.SQL;
	}
	
	/*
	 * Load all data
	 */
	
	public static void load() {
		FileManager.check();
		if(baseType.equals(BaseType.SQL)) SqlBase.checkTables();
		UserManager.load();
		SessionManager.load();
	}
	
	/*
	 * Save all data
	 */
	
	public static void save() {
		FileManager.check();
		if(baseType.equals(BaseType.SQL)) SqlBase.checkTables();
		UserManager.save();
		SessionManager.save();
	}
	
	/*
	 * Getters
	 */
	
	public static FileConfiguration getConfig() {
		return config;
	}

	public static BaseType getBaseType() {
		return baseType;
	}
	
}
