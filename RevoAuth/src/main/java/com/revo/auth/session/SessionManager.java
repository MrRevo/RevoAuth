package com.revo.auth.session;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.revo.auth.Plugin;
import com.revo.auth.data.BaseType;
import com.revo.auth.data.DataManager;
import com.revo.auth.data.FileManager;
import com.revo.auth.data.SqlBase;

/*
 * Session service
 * 
 * Created By Revo
 */

public final class SessionManager {

	/*
	 * Data
	 */

	private static List<PlayerSession> sessions = new ArrayList<PlayerSession>();

	/*
	 * Getter
	 */

	public static List<PlayerSession> getSessions() {
		return sessions;
	}

	/*
	 * Create session
	 */

	public static void createSession(Player p) {
		for (PlayerSession s : sessions)
			if (s.getPlayer().equals(p)) {
				updateSession(p);
				return;
			}
		PlayerSession s = new PlayerSession(p.getUniqueId());
		s.setLastAddress(p.getAddress().getHostName());
		s.run();
		sessions.add(s);
	}

	/*
	 * Update session current address
	 */

	public static void updateSession(Player p) {
		for (PlayerSession s : sessions) {
			if (s.getPlayer().equals(p)) {
				s.setLastAddress(p.getAddress().getHostName());
				s.refreshTime();
				if(!s.isActive()) s.run();
				if (DataManager.getBaseType().equals(BaseType.SQL)) {
					Connection c = SqlBase.connect();
					try {
						c.createStatement().executeUpdate("UPDATE sessions SET lastAddress="+s.getLastAddress()+", time="+s.getMinutesToEnd()+" WHERE uuid='"+s.getUUID().toString()+"'");
						c.close();
					} catch (SQLException e) {
						Bukkit.getLogger().info(e.getMessage());
					}
				}
			}
		}
	}

	/*
	 * Check player connection is current session
	 */

	public static boolean isCurrentSession(Player p) {
		for (PlayerSession s : sessions) {
			if (s.getPlayer().equals(p) && s.getLastAddress().equals(p.getAddress().getHostName()) && s.isActive() && s.isAutologinEnabled())
				return true;
			if(s.isAutologinEnabled() && isPremium(s.getPlayer()))
				return true;
		}
		return false;
	}
	
	/*
	 * Check player is premium player
	 */
	
	public static boolean isPremium(Player p) {
		return false;
	}

	/*
	 * Load sessions info from base
	 */

	public static void load() {
		if (DataManager.getBaseType().equals(BaseType.YAML)) {
			for (String sessionFileName : FileManager.getSessions().list()) {
				FileConfiguration sessionConfig = YamlConfiguration
						.loadConfiguration(new File(FileManager.getSessions().getPath(), sessionFileName));
				PlayerSession session = new PlayerSession(UUID.fromString(sessionConfig.getString("uuid")));
				session.setLastAddress(sessionConfig.getString("lastAddress"));
				session.setAutologin(sessionConfig.getBoolean("autologin"));
				session.setMinutesToEnd(sessionConfig.getInt("time"));
				session.run();
				sessions.add(session);
			}
		}
		if (DataManager.getBaseType().equals(BaseType.SQL)) {
			try {
				Connection c = SqlBase.connect();
				ResultSet rs = c.createStatement().executeQuery("SELECT * FROM sessions");
				while (rs.next()) {
					PlayerSession session = new PlayerSession(UUID.fromString(rs.getString(2)));
					session.setLastAddress(rs.getString(3));
					session.setAutologin(rs.getBoolean(4));
					session.setMinutesToEnd(rs.getInt(5));
					session.run();
					sessions.add(session);
				}
				c.close();
			} catch (SQLException e) {
				Bukkit.getLogger().info(e.getMessage());
			}
		}
	}

	/*
	 * Save sessions info to base
	 */

	public static void save() {
		if (DataManager.getBaseType().equals(BaseType.YAML)) {
			for (PlayerSession s : sessions) {
				File userFile = new File(FileManager.getSessions().getPath(), s.getUUID().toString() + ".yml");
				FileConfiguration sessionConfig = YamlConfiguration.loadConfiguration(userFile);
				sessionConfig.set("uuid", s.getUUID().toString());
				sessionConfig.set("lastAddress", s.getLastAddress());
				sessionConfig.set("autologin", s.isAutologinEnabled());
				sessionConfig.set("time", s.getMinutesToEnd());
				s.getThread().interrupt();
				try {
					sessionConfig.save(userFile);
				} catch (IOException e) {
					Plugin.get().getLogger().info(ChatColor.RED + "Error while saving session, contact with dev!");
				}
			}
		}
		if (DataManager.getBaseType().equals(BaseType.SQL)) {
			try {
				Connection c = SqlBase.connect();
				for (PlayerSession s : sessions) {
					Statement st = c.createStatement();
					if (st.executeQuery("SELECT uuid FROM sessions WHERE uuid='" + s.getUUID().toString()+"'").next()) {
						st.close();
						continue;
					}
					st.executeUpdate("INSERT INTO sessions VALUES (null,'"+s.getUUID().toString()+"','"+s.getLastAddress()+"',"+(s.isAutologinEnabled() == true ? 1 : 0)+","+s.getMinutesToEnd()+")");
					st.close();
					s.getThread().interrupt();
				}
				c.close();
			} catch (SQLException e) {
				Bukkit.getLogger().info(e.getMessage());
			}
		}
	}

	/*
	 * Get player session
	 */

	public static PlayerSession get(UUID uniqueId) {
		for (PlayerSession s : sessions)
			if (s.getUUID().equals(uniqueId))
				return s;
		return null;
	}

}
