package com.gravypod.PersonalWorlds.commands;

import org.bukkit.entity.Player;

import com.gravypod.PersonalWorlds.CommandHandler;
import com.gravypod.PersonalWorlds.PersonalWorlds;

/**
 * This deals with the "help" argument.
 * Prints out help info to a player.
 * 
 * @author gravypod
 *
 */
public class Help {

	/**
	 * Constructor.
	 * 
	 */
	public Help() {

		command((Player) CommandHandler.getSender(), CommandHandler.getArgs(), CommandHandler.getPlugin());
		
	}
	
	/**
	 * Prints out help info for a player.
	 * 
	 * @param player - Player to send messages to.
	 * @param args - Arguments of the help command; Not needed
	 * @param plugin - PersonalWorlds plugin instance
	 * 
	 */
	public static void command(Player player, String[] args, PersonalWorlds plugin) {
		
		player.sendMessage("---]Help[---");
		player.sendMessage("help - This page");
		player.sendMessage("tp - Teleport to your world");
		player.sendMessage("add/create {generator} - Create your own world.");
		player.sendMessage("generator - Lists the generators you can use");
		player.sendMessage("delete - Delete a world. Cannot be undone!");
		
	}
	
}
