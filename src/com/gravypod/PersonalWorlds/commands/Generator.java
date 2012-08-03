package com.gravypod.PersonalWorlds.commands;

import org.bukkit.entity.Player;

import com.gravypod.PersonalWorlds.CommandHandler;
import com.gravypod.PersonalWorlds.PersonalWorlds;

/**
 * This deals with the "generator" argument.
 * This will list out generators.
 * 
 * @author gravypod and ElgarL
 *
 */
public class Generator {

	/**
	 * Constructor.
	 * 
	 */
	public Generator() {
		
		command((Player) CommandHandler.getSender(), CommandHandler.getArgs(), CommandHandler.getPlugin());
		
	}
	
	private static void command(Player player, String[] args, PersonalWorlds plugin) {
		
		player.sendMessage("The generators you can use are: " + plugin.joinedGenList());
		
	}
	
}
