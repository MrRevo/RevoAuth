package com.revo.auth.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.revo.auth.data.DataManager;
import com.revo.auth.session.PlayerSession;
import com.revo.auth.session.SessionManager;

/*
 * Auto login command
 * 
 * Created By Revo
 */

public final class AutoLogin implements ExecutableCommand {

	@Override
	public boolean execute(CommandSender s, Command cmd, String label, String[] args) {
		if (!(s instanceof Player)) {
			s.sendMessage(ChatColor.RED + "Only user can login!");
			return false;
		}
		if (args.length == 0) {
			PlayerSession session = SessionManager.get(((Player) s).getUniqueId());
			if(session.isAutologinEnabled()) {
				session.setAutologin(false);
				s.sendMessage(ChatColor.translateAlternateColorCodes('&', DataManager.getConfig().getString("prefix")+"&cWylaczono autologowanie!"));
			}else {
				session.setAutologin(true);
				s.sendMessage(ChatColor.translateAlternateColorCodes('&', DataManager.getConfig().getString("prefix")+"&aWlaczono autologowanie!"));
			}
			return false;
		}
		s.sendMessage(ChatColor.translateAlternateColorCodes('&', DataManager.getConfig().getString("prefix")+"&cWpisz /autlogin"));
		return false;
	}

}
