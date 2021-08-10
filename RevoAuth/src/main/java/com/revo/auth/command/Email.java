package com.revo.auth.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.revo.auth.Plugin;
import com.revo.auth.data.DataManager;
import com.revo.auth.user.UserManager;

/*
 * Email command
 *  
 * Created by Revo
 */

public final class Email implements ExecutableCommand {

	public boolean execute(CommandSender s, Command cmd, String label, String[] args) {
		if (!(s instanceof Player)) {
			s.sendMessage(ChatColor.RED + "Only user can login!");
			return false;
		}
		if (args.length == 0) {
			String email = UserManager.get(s.getName()).getEmail();
			s.sendMessage(ChatColor.DARK_GREEN + "]-----[ " + ChatColor.GOLD + "RevoAuth v"
					+ Plugin.get().getDescription().getVersion() + ChatColor.DARK_GREEN + " ]-----[");
			s.sendMessage(ChatColor.RED + "Twoj email: " + (email == null ? "Nie ustawiono!" : email));
			s.sendMessage(ChatColor.DARK_AQUA + "LISTA KOMEND:");
			s.sendMessage(ChatColor.DARK_AQUA + "/email set <email> - uzyj aby zmienic swoj email.");
			return false;
		}
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("set")) {
				UserManager.changeEmail(s.getName(), args[1]);
				s.sendMessage(ChatColor.translateAlternateColorCodes('&', DataManager.getConfig().getString("prefix")+"&aPomyslnie zmieniono adres email!"));
				return false;
			}
		}
		s.sendMessage(ChatColor.translateAlternateColorCodes('&', DataManager.getConfig().getString("prefix")+"&cWpisz /email set <twoj_email>"));
		return false;
	}

}
