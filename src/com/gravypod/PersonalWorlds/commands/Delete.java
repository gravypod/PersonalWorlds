package com.gravypod.PersonalWorlds.commands;

import org.bukkit.entity.Player;

import com.gravypod.PersonalWorlds.Permissions;
import com.gravypod.PersonalWorlds.PersonalWorlds;
import com.gravypod.PersonalWorlds.CommandHandlers.ICommand;
import com.gravypod.PersonalWorlds.utils.PluginUtil;

/**
 * This deals with the argument "delete" Deletes a world of a player or a
 * provided player name.
 * 
 * @author gravypod and ElgarL
 * 
 */
public class Delete implements ICommand {
	
	@Override
	public void command(final Player player, final String[] args, final PersonalWorlds plugin) {
	
		if (args.length >= 1) {
			
			if (Permissions.DELETE_OTHER.canUse(player) && args.length == 2) {
				
				player.sendMessage("You are now deleting " + args[1] + " world!");
				
				deleteWorld(player, args[1]);
				
				player.sendMessage("Deleting done!");
				
			} else if (Permissions.DELETE.canUse(player) && args.length == 1) {
				
				player.sendMessage("You are now deleting your world!");
				
				deleteWorld(player, player.getName());
				
				player.sendMessage("Deleting done!");
				
			} else {
				
				player.sendMessage("You cannot delete that players world!");
				
			}
			
		}
		
	}
	
	/**
	 * Deletes a world that is owned by 'name'
	 * 
	 * @param name
	 *            is a player name.
	 * 
	 */
	private void deleteWorld(Player player, final String name) {
	
		PluginUtil.deleteWorld(player, name);
		
	}
	
}
