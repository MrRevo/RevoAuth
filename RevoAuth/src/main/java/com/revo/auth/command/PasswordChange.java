package com.revo.auth.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.revo.auth.data.DataManager;
import com.revo.auth.user.User;
import com.revo.auth.user.UserManager;

public final class PasswordChange implements ExecutableCommand {

	@Override
	public boolean execute(CommandSender s, Command cmd, String label, String[] args) {
		if (!(s instanceof Player)) {
			s.sendMessage(ChatColor.RED + "Only user can login!");
			return false;
		}
		User u = UserManager.get(s.getName());
		if (!u.isLogged()) {
			s.sendMessage(ChatColor.translateAlternateColorCodes('&', DataManager.getConfig().getString("prefix")+"&cNie jestes zalogowany!"));
			return false;
		}
		if(args.length == 2) {
			if(!args[0].equals(args[1])) {
				s.sendMessage(ChatColor.translateAlternateColorCodes('&', DataManager.getConfig().getString("prefix")+"&cBlad! "+args[0]+" i "+args[1] + " sa rozne!"));
				return false;
			}
			UserManager.changePassword(s.getName(), args[0]);
			s.sendMessage(ChatColor.translateAlternateColorCodes('&', DataManager.getConfig().getString("prefix")+"&aPomyslnie zmieniono haslo na "+args[0]));
			return false;
		}
		s.sendMessage(ChatColor.translateAlternateColorCodes('&', DataManager.getConfig().getString("prefix")+"&cBlad! Wpisz: /passwordchange <haslo> <powtorz_haslo>"));
		return false;
	}

}
