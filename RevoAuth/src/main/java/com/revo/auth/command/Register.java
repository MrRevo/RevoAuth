package com.revo.auth.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.revo.auth.data.DataManager;
import com.revo.auth.user.UserManager;

/*
 * Register command 
 * 
 * Created by Revo
 */

public final class Register implements ExecutableCommand {

	public boolean execute(CommandSender s, Command cmd, String label, String[] args) {
		if (!(s instanceof Player)) {
			s.sendMessage(ChatColor.RED + "Only user can login!");
			return false;
		}
		if (args.length != 2) {
			s.sendMessage(ChatColor.translateAlternateColorCodes('&',
					DataManager.getConfig().getString("prefix")+"&cBlad! Wpisz: /register <twoje_haslo> <twoje_haslo>"));
			return false;
		}
		if (!args[0].equals(args[1])) {
			s.sendMessage(ChatColor.translateAlternateColorCodes('&',
					DataManager.getConfig().getString("prefix")+"&cTwoja hasla sie nie zgadzaja!"));
			return false;
		}
		if (UserManager.register(s.getName(), args[0])) {
			s.sendMessage(ChatColor.translateAlternateColorCodes('&',
					DataManager.getConfig().getString("prefix")+"&aZarejestrowano! Teraz sie zaloguj!"));
			return false;
		}
		s.sendMessage(ChatColor.translateAlternateColorCodes('&',
				DataManager.getConfig().getString("prefix")+"&cJestes juz zarejestrowany!"));
		return false;
	}

}
