package com.revo.auth.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;

/*
 * Sql base manager
 * 
 * Created By Revo
 */

public final class SqlBase {
	
	/*
	 * Data
	 */
	
	private static final String USER = DataManager.getConfig().getString("sql.user");
	private static final String PASSWORD = DataManager.getConfig().getString("sql.password");
	private static final String URL = DataManager.getConfig().getString("sql.url");
	
	/*
	 * Get connection
	 */
	
	public static Connection connect() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(URL,USER,PASSWORD);
		} catch (ClassNotFoundException e) {
			Bukkit.getLogger().info("Cannot find driver, contact with revo!");
		} catch (SQLException e) {
			Bukkit.getLogger().info("Cannot connect to your database!");
		}
		return connection;
	}

	/*
	 * Create tables if don't exists
	 */
	
	public static void checkTables() {
		try {
			Connection c = connect();
			Statement s = c.createStatement();
			if(!s.executeQuery("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'users' AND TABLE_SCHEMA = '"+URL.subSequence(URL.lastIndexOf('/') + 1, URL.length()).toString()+"' ").next()) {
				s.executeUpdate("CREATE TABLE users ("
						+ "id int NOT NULL,"
						+ "login varchar(255) NOT NULL,"
						+ "password varchar(255) NOT NULL,"
						+ "email varchar(255) NOT NULL,"
						+ "logged boolean NOT NULL"
						+ ")");
				s.executeUpdate("ALTER TABLE users ADD PRIMARY KEY (id)");
			}
			if(!s.executeQuery("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'sessions' AND TABLE_SCHEMA = '"+URL.subSequence(URL.lastIndexOf('/') + 1, URL.length()).toString()+"' ").next()) {
				s.executeUpdate("CREATE TABLE sessions ("
						+ "id int NOT NULL,"
						+ "uuid varchar(255) NOT NULL,"
						+ "lastAddress varchar(255) NOT NULL,"
						+ "autologin boolean NOT NULL,"
						+ "time int NOT NULL"
						+ ")");
				s.executeUpdate("ALTER TABLE sessions ADD PRIMARY KEY (id)");
			}
			s.close();
			c.close();
		}
		catch (SQLException e) {
			Bukkit.getLogger().info(e.getMessage());
		}
	}
	
}
