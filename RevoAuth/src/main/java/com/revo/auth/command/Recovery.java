package com.revo.auth.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.revo.auth.data.DataManager;
import com.revo.auth.recovery.EmailSender;
import com.revo.auth.recovery.RecoveryManager;
import com.revo.auth.user.UserManager;

/*
 * Recovery command 
 * 
 * Created by Revo
 */

public final class Recovery implements ExecutableCommand {

	public boolean execute(CommandSender s, Command cmd, String label, String[] args) {
		if (!(s instanceof Player)) {
			s.sendMessage(ChatColor.RED + "Only user can login!");
			return false;
		}
		if (args.length == 0) {
			if (UserManager.get(s.getName()).getEmail() != null) {
				if (EmailSender.send(UserManager.get(s.getName()).getEmail(), "RAuth Recovery",
						"Aby odzyskac swoje konto wejdz na serwer i wpisz /recovery "
								+ RecoveryManager.getKey(s.getName())))
					s.sendMessage(ChatColor.translateAlternateColorCodes('&',
							DataManager.getConfig().getString("prefix")+"&aWyslano wiadomosc z kodem do odzyskania konta na twoj adres email!"));
				else
					s.sendMessage(ChatColor.translateAlternateColorCodes('&',
							DataManager.getConfig().getString("prefix")+"&cBlad wysylania wiadomosci email! Skontaktuj sie z administratorem!"));
			} else {
				s.sendMessage(ChatColor.translateAlternateColorCodes('&',
						DataManager.getConfig().getString("prefix")+"&cUstaw swoj email /email set <email>!"));
			}
		}
		if (args.length == 1) {
			if (RecoveryManager.validate(s.getName(), args[0])) {
				s.sendMessage(ChatColor.translateAlternateColorCodes('&',
						DataManager.getConfig().getString("prefix")+"&aZrestowano haslo! Nowe haslo to 1234!"));
				UserManager.get(s.getName()).setPassword(DataManager.getConfig().getString("default_password"));
			} else {
				s.sendMessage(ChatColor.translateAlternateColorCodes('&',
						DataManager.getConfig().getString("prefix")+"&cPodany przez Ciebie kod nie jest twoim kodem odzyskiwania!"));
			}
		}
		return false;
	}

}
