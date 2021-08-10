package com.revo.auth.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.revo.auth.Plugin;

/*
 * RAuth command 
 * 
 * Created by Revo
 */

public final class RAuth implements ExecutableCommand{

	public boolean execute(CommandSender s, Command cmd, String label, String[] args) {
		s.sendMessage(ChatColor.DARK_GREEN+"]-----[ "+ChatColor.GOLD+"RevoAuth v" + Plugin.get().getDescription().getVersion() + ChatColor.DARK_GREEN+ " ]-----[");
		s.sendMessage(ChatColor.DARK_AQUA+"/login <password> - uzyj aby sie zalogowac.");
		s.sendMessage(ChatColor.DARK_AQUA+"/register <password> <password_repeat> - uzyj aby sie zarejestrowac.");
		s.sendMessage(ChatColor.DARK_AQUA+"/email - uzyj aby zarzadzac swoim email'em.");
		s.sendMessage(ChatColor.DARK_AQUA+"/recovery - uzyj aby odzyskac haslo przy pomocy email.");
		s.sendMessage(ChatColor.DARK_AQUA+"/passwordchange - uzyj aby zmienic swoje haslo.");
		s.sendMessage(ChatColor.DARK_AQUA+"/autologin - wylacz/wlacz auto logowanie na serwer.");
		s.sendMessage(ChatColor.RED+"Developed by Revo ;*");
		return false;
	}

}
