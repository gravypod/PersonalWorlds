package com.gravypod.PersonalWorlds.commands;

import org.bukkit.entity.Player;

import com.gravypod.PersonalWorlds.PersonalWorlds;
import com.gravypod.PersonalWorlds.CommandHandlers.ICommand;

/**
 * This deals with the "generator" argument. This will list out generators.
 * 
 * @author gravypod and ElgarL
 * 
 */
public class Generator implements ICommand {
	
	@Override
	public void command(final Player player, final String[] args, final PersonalWorlds plugin) {
	
		player.sendMessage(plugin.getMessage("generatorsList").replace("{GENERATORS}", plugin.joinedGenList()));
		
	}
	
}
