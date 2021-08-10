package com.revo.auth.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.revo.auth.data.DataManager;
import com.revo.auth.listener.PlayerLoggingListener;
import com.revo.auth.session.SessionManager;
import com.revo.auth.user.User;
import com.revo.auth.user.UserManager;

/*
 * Login command 
 * 
 * Created by Revo
 */

public final class Login implements ExecutableCommand {

	public boolean execute(CommandSender s, Command cmd, String label, String[] args) {
		if (!(s instanceof Player)) {
			s.sendMessage(ChatColor.RED + "Only user can login!");
			return false;
		}
		User u = UserManager.get(s.getName());
		if (u.isLogged()) {
			s.sendMessage(ChatColor.translateAlternateColorCodes('&', DataManager.getConfig().getString("prefix")+"&cJestes juz zalogowany!"));
			return false;
		}
		if (!u.isLogged() && u.getPassword().length() == 0) {
			s.sendMessage(ChatColor.translateAlternateColorCodes('&', DataManager.getConfig().getString("prefix")+"&cZarejestruj sie! /register <haslo> <powtorz_haslo>"));
			return false;
		}
		if (args.length != 1) {
			s.sendMessage(ChatColor.translateAlternateColorCodes('&', DataManager.getConfig().getString("prefix")+"&cBlad! Wpisz: /login <twoje_haslo>"));
			return false;
		}
		if (!UserManager.login(s.getName(), args[0])) {
			s.sendMessage(ChatColor.translateAlternateColorCodes('&', DataManager.getConfig().getString("prefix")+"&cPodales bledne haslo!"));
			return false;
		}
		s.sendMessage(ChatColor.translateAlternateColorCodes('&', DataManager.getConfig().getString("prefix")+"&aZalogowano!"));
		SessionManager.createSession((Player) s);
		if(!(PlayerLoggingListener.notLoggedPlayerFixList.isEmpty()) && PlayerLoggingListener.notLoggedPlayerFixList.contains((Player) s))PlayerLoggingListener.notLoggedPlayerFixList.remove((Player) s);
		return false;
	}

}
