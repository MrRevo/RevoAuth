package com.revo.auth.user;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import com.google.common.hash.Hashing;
import com.revo.auth.Plugin;
import com.revo.auth.data.BaseType;
import com.revo.auth.data.DataManager;
import com.revo.auth.data.FileManager;
import com.revo.auth.data.SqlBase;

/*
 * UserManager class 
 * 
 * Created by Revo
 */

public final class UserManager {

	/*
	 * Data
	 */

	private static List<User> users = new ArrayList<User>();

	/*
	 * Change email
	 */

	public static boolean changeEmail(String login, String email) {
		for (User u : users)
			if (u.getLogin().equalsIgnoreCase(login)) {
				u.setEmail(email);
				if (DataManager.getBaseType().equals(BaseType.SQL)) {
					Connection c = SqlBase.connect();
					try {
						c.createStatement().executeUpdate("UPDATE users SET email="+u.getEmail()+" WHERE login='"+u.getLogin()+"'");
						c.close();
					} catch (SQLException e) {
						Bukkit.getLogger().info(e.getMessage());
					}
				}
				return true;
			}
		return false;
	}

	/*
	 * Change password
	 */

	public static boolean changePassword(String login, String password) {
		for (User u : users)
			if (u.getLogin().equalsIgnoreCase(login)) {
				u.setPassword(hash(password));
				if (DataManager.getBaseType().equals(BaseType.SQL)) {
					Connection c = SqlBase.connect();
					try {
						c.createStatement().executeUpdate("UPDATE users SET password="+u.getPassword()+" WHERE login='"+u.getLogin()+"'");
						c.close();
					} catch (SQLException e) {
						Bukkit.getLogger().info(e.getMessage());
					}
				}
				return true;
			}
		return false;
	}

	/*
	 * Login user
	 */

	public static boolean login(String login, String password) {
		for (User u : users)
			if (u.getLogin().equalsIgnoreCase(login) && u.getPassword().equals(hash(password))) {
				u.setLogged(true);
				UserContainer.get(Bukkit.getPlayer(u.getLogin())).loadFromClass();
				Player p = Bukkit.getPlayer(u.getLogin());
				if (!p.getGameMode().equals(GameMode.CREATIVE))
					p.setAllowFlight(false);
				for (PotionEffectType t : PotionEffectType.values())
					Bukkit.getPlayer(u.getLogin()).removePotionEffect(t);
				return true;
			}
		return false;
	}

	/*
	 * Register user
	 */

	public static boolean register(String login, String password) {
		for (User u : users)
			if (u.getLogin().equalsIgnoreCase(login) && u.getPassword().length() > 0)
				return false;
		User u = new User(login);
		u.setPassword(hash(password));
		users.add(u);
		return true;
	}

	/*
	 * Get user by name
	 */

	public static User get(String login) {
		for (User u : users)
			if (u.getLogin().equalsIgnoreCase(login)) {
				return u;
			}
		return new User(login);
	}

	/*
	 * Load users from base
	 */

	public static void load() {
		if (DataManager.getBaseType().equals(BaseType.YAML)) {
			for (String userFileName : FileManager.getUsers().list()) {
				FileConfiguration userConfig = YamlConfiguration
						.loadConfiguration(new File(FileManager.getUsers().getPath(), userFileName));
				User u = new User(userConfig.getString("login"));
				u.setEmail(userConfig.getString("email"));
				u.setPassword(userConfig.getString("password"));
				u.setLogged(userConfig.getBoolean("logged"));
				users.add(u);
			}
		}
		if (DataManager.getBaseType().equals(BaseType.SQL)) {
			try {
				Connection c = SqlBase.connect();
				ResultSet rs = c.createStatement().executeQuery("SELECT * FROM users");
				while (rs.next()) {
					User u = new User(rs.getString(2));
					u.setPassword(rs.getString(3));
					u.setEmail(rs.getString(4));
					u.setLogged(rs.getBoolean(5));
					users.add(u);
				}
				c.close();
			} catch (SQLException e) {
				Bukkit.getLogger().info(e.getMessage());
			}
		}
	}

	/*
	 * Save users to base
	 */

	public static void save() {
		if (DataManager.getBaseType().equals(BaseType.YAML)) {
			for (User u : users) {
				File userFile = new File(FileManager.getUsers().getPath(), u.getLogin() + ".yml");
				FileConfiguration userConfig = YamlConfiguration.loadConfiguration(userFile);
				userConfig.set("login", u.getLogin());
				userConfig.set("password", u.getPassword());
				userConfig.set("email", u.getEmail());
				userConfig.set("logged", u.isLogged());
				try {
					userConfig.save(userFile);
				} catch (IOException e) {
					Plugin.get().getLogger().info(ChatColor.RED + "Error while saving user, contact with dev!");
				}
			}
		}
		if (DataManager.getBaseType().equals(BaseType.SQL)) {
			try {
				Connection c = SqlBase.connect();
				for (User u : users) {
					Statement s = c.createStatement();
					if (s.executeQuery("SELECT login FROM users WHERE login='" + u.getLogin()+"'").next()) {
						s.executeUpdate("UPDATE users SET logged="+(u.isLogged() == true ? 1 : 0)+" WHERE login='"+u.getLogin()+"'");
						s.close();
						continue;
					}
					s.executeUpdate("INSERT INTO users VALUES (null,"+u.getLogin()+","+u.getPassword()+","+u.getEmail()+","+(u.isLogged() == true ? 1 : 0)+")");
					s.close();
				}
				c.close();
			} catch (SQLException e) {
				Bukkit.getLogger().info(e.getMessage());
			}
		}
	}

	/*
	 * Get all users
	 */

	public static List<User> getUsers() {
		return users;
	}

	/*
	 * Hash a string
	 */

	private static String hash(String s) {
		return Hashing.sha256().hashString(s, StandardCharsets.UTF_8).toString();
	}

}
