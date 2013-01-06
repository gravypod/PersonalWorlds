package com.gravypod.PersonalWorlds.commands;

import org.bukkit.entity.Player;

import com.gravypod.PersonalWorlds.Permissions;
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
	
		if (args.length != 2) {
			player.sendMessage(plugin.getMessage("wrongArguments"));
			return;
		}
		
		if (Permissions.OVERRIDE.canUse(player)) {
			
			TeleportUtils.tpOverride(player, args[1], plugin);
			return;
			
		}
		
		TeleportUtils.tpToFriend(player, args[1]);
		
	}
	
}
