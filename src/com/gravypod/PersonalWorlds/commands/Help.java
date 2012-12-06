package com.gravypod.PersonalWorlds.commands;

import org.bukkit.entity.Player;

import com.gravypod.PersonalWorlds.PersonalWorlds;
import com.gravypod.PersonalWorlds.CommandHandlers.ICommand;

/**
 * This deals with the "help" argument. Prints out help info to a player.
 * 
 * @author gravypod
 * 
 */
public class Help implements ICommand {
	
	/**
	 * Prints out help info for a player.
	 * 
	 * @param player
	 *            - Player to send messages to.
	 * @param args
	 *            - Arguments of the help command; Not needed
	 * @param plugin
	 *            - PersonalWorlds plugin instance
	 * 
	 */
	@Override
	public void command(final Player player, final String[] args, final PersonalWorlds plugin) {
	
		player.sendMessage("---]Help[---");
		player.sendMessage("help - This page");
		player.sendMessage("tp - Teleport to your world");
		player.sendMessage("add/create {generator} - Create your own world.");
		player.sendMessage("generator - Lists the generators you can use");
		player.sendMessage("delete - Delete a world. Cannot be undone!");
		
	}
	
}
