package com.gravypod.PersonalWorlds.commands;

import org.bukkit.entity.Player;

import com.gravypod.PersonalWorlds.CommandHandler;
import com.gravypod.PersonalWorlds.PersonalWorlds;
import com.gravypod.PersonalWorlds.PluginUtil;

/**
 * This deals with the argument "delete"
 * Deletes a world of a player or a provided player name.
 * 
 * @author gravypod and ElgarL
 *
 */
public class Delete {

	/**
	 * Constructor.
	 * 
	 */
	public Delete() {

		command((Player) CommandHandler.getSender(), CommandHandler.getArgs(), CommandHandler.getPlugin());
		
	}

	private void command(Player player, String[] args, PersonalWorlds plugin) {

		if (args.length == 2) {
			
			if (player.hasPermission(plugin.getPluginName() + ".delete.other")) {
				
				deleteWorld(args[1]);
				
			} else if (player.getName().equalsIgnoreCase(args[1])) {
				
				deleteWorld(args[1]);
				
			} else {
				
				player.sendMessage("You cannot delete that players world!");
				
			}
			
		} 
		
	}
	
	/**
	 * Deletes a world that is owned by 'name'
	 * 
	 * @param name is a player name.
	 * 
	 */
	private void deleteWorld(String name) {
		
		PluginUtil.deleteWorld(name);
		
	}
	
}
