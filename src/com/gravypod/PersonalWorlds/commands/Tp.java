package com.gravypod.PersonalWorlds.commands;

import org.bukkit.entity.Player;

import com.gravypod.PersonalWorlds.PersonalWorlds;
import com.gravypod.PersonalWorlds.CommandHandlers.ICommand;
import com.gravypod.PersonalWorlds.utils.TeleportUtils;

/**
 * This deals with the "tp" argument. It teleports a player to a world.
 * 
 * @author gravypod
 * 
 */
public class Tp implements ICommand {
	
	@Override
	public void command(final Player player, final String[] args, final PersonalWorlds plugin) {
	
		if (args.length == 2) {
			
			TeleportUtils.tpToFriend(player, args[1]);
			
		} else {
			
			TeleportUtils.tpToMy(player);
			
		}
		
	}
	
}
