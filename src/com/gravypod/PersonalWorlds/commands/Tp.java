package com.gravypod.PersonalWorlds.commands;

import org.bukkit.entity.Player;

import com.gravypod.PersonalWorlds.CommandHandler;
import com.gravypod.PersonalWorlds.utils.TeleportUtils;

/**
 * This deals with the "tp" argument.
 * It teleports a player to a world.
 * 
 * @author gravypod
 *
 */
public class Tp {

	/**
	 * Constructor.
	 * 
	 */
	public Tp() {

		command((Player) CommandHandler.getSender(), CommandHandler.getArgs());
		
	}
	
	private static void command(Player player, String[] args) {
		
		if (args.length == 2) {
			
			TeleportUtils.tpToFriend(player, args[1]);
			
		} else {
			
			TeleportUtils.tpToMy(player);
			
		}
		
	}
	

	
}
