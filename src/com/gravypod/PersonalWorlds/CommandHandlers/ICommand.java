package com.gravypod.PersonalWorlds.CommandHandlers;

import org.bukkit.entity.Player;

import com.gravypod.PersonalWorlds.PersonalWorlds;

public interface ICommand {
	
	/**
	 * Run the needed task for a command.
	 * 
	 * @param player
	 *            - player that sent the command
	 * @param args
	 *            - arguments of the command
	 * @param plugin
	 *            - plugin instance
	 */
	void command(final Player player, final String[] args, final PersonalWorlds plugin);
	
}
