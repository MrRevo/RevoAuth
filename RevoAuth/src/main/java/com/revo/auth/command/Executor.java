package com.revo.auth.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

/*
 * Command executor class 
 * 
 * Created by Revo
 */

public final class Executor implements CommandExecutor, TabCompleter{

	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		//RAUTH
		if(cmd.getName().equalsIgnoreCase("rauth")) return new RAuth().execute(s, cmd, label, args);
		//LOGIN
		if(cmd.getName().equalsIgnoreCase("login") || cmd.getName().equalsIgnoreCase("l")) return new Login().execute(s, cmd, label, args);
		//REGISTER
		if(cmd.getName().equalsIgnoreCase("register") || cmd.getName().equalsIgnoreCase("r")) return new Register().execute(s, cmd, label, args);
		//EMAIL
		if(cmd.getName().equalsIgnoreCase("email")) return new Email().execute(s, cmd, label, args);
		//RECOVERY
		if(cmd.getName().equalsIgnoreCase("recovery")) return new Recovery().execute(s, cmd, label, args);
		//PASSWORDCHANGE
		if(cmd.getName().equalsIgnoreCase("passwordchange")) return new PasswordChange().execute(s, cmd, label, args);
		//AUTOLOGIN
		if(cmd.getName().equalsIgnoreCase("autologin")) return new AutoLogin().execute(s, cmd, label, args);
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		List<String> list = new ArrayList<>();
		//EMAIL
		if(cmd.getName().equalsIgnoreCase("email") && args.length == 1) {
			list.add("set");
			return list;
		}
		return null;
	}

}
