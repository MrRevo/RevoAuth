package com.revo.auth.session;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.revo.auth.data.DataManager;

/*
 * Player session
 * 
 * Created By Revo
 */

public final class PlayerSession {
	
	/*
	 * Data
	 */

	private UUID uuid;
	private String lastAddress;
	private Thread thread;
	private boolean active = false;
	private int minutesToEnd = DataManager.getConfig().getInt("session_time");
	private boolean autologin = false;
	
	/*
	 * Constructor
	 */
	
	public PlayerSession(UUID uuid) {
		super();
		this.uuid = uuid;
	}
	
	/*
	 * Run timer to end session
	 */

	public void run() {
		if(active) return;
		this.thread = new Thread(new Runnable() {
			@SuppressWarnings("static-access")
			@Override
			public void run() {
				active = true;
				while(true) {
					if(minutesToEnd<=0) break;
					if(!autologin) break;
					minutesToEnd--;
					try {
						Thread.currentThread().sleep(60000);
					} catch (InterruptedException e) {}
				}
				active = false;
			}
			
		});
		this.thread.start();
	}
	
	/*
	 * Refersh time to end
	 */
	
	public void refreshTime() {
		this.minutesToEnd = DataManager.getConfig().getInt("session_time");
	}
	
	/*
	 * Getters & Setters
	 */
	
	public Player getPlayer() {
		return Bukkit.getPlayer(uuid);
	}

	public Thread getThread() {
		return thread;
	}

	public String getLastAddress() {
		return lastAddress;
	}

	public void setLastAddress(String lastAddress) {
		this.lastAddress = lastAddress;
	}

	public boolean isActive() {
		return active;
	}

	public UUID getUUID() {
		return uuid;
	}

	public boolean isAutologinEnabled() {
		return autologin;
	}

	public void setAutologin(boolean autologin) {
		if(autologin) run();
		this.autologin = autologin;
	}

	public int getMinutesToEnd() {
		return minutesToEnd;
	}

	public void setMinutesToEnd(int minutesToEnd) {
		this.minutesToEnd = minutesToEnd;
	}
}
