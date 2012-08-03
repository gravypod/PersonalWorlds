package com.gravypod.PersonalWorlds.commands;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import com.gravypod.PersonalWorlds.CommandHandler;
import com.gravypod.PersonalWorlds.PersonalWorlds;
import com.gravypod.PersonalWorlds.WorldGenerator;

/**
 * This class deals with the argument "add" and "create".
 * Creates an world for a player.
 * 
 * @author gravypod and ElgarL
 *
 */
public class Add {

	/**
	 * Constructor.
	 * 
	 */
	public Add() {
		
		command((Player) CommandHandler.getSender(), CommandHandler.getArgs(), CommandHandler.getPlugin());
		
	}
	
	private static void command(Player player, String[] args, PersonalWorlds plugin) {
		
		if (args.length == 2) {
			
			new WorldGenerator(plugin, player, args[1]);
		
			player.setMetadata(plugin.getPluginName(), new FixedMetadataValue(plugin, true));
			
		} else {
			
			player.sendMessage("invalid generator. choose one of the following generators:" + plugin.joinedGenList());
			
		}
		
	}
	
	
}
