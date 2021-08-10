package com.revo.auth.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/*
 * ExecutableCommand interface 
 * 
 * Created by Revo
 */

public interface ExecutableCommand {

	//Execute command
	public boolean execute(CommandSender s, Command cmd, String label, String[] args);
	
}
