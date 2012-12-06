package com.gravypod.PersonalWorlds.commands;

import org.bukkit.entity.Player;

import com.gravypod.PersonalWorlds.PersonalWorlds;
import com.gravypod.PersonalWorlds.CommandHandlers.ICommand;

/**
 * Creates an world for a player
 * 
 * @author gravypod and ElgarL
 * 
 */
public class Create implements ICommand {
	
	@Override
	public void command(final Player player, final String[] args, final PersonalWorlds plugin) {
	
		new Add().command(player, args, plugin);
		
	}
	
}
