package com.gravypod.PersonalWorlds.CommandHandlers;

import org.apache.commons.lang.WordUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gravypod.PersonalWorlds.PersonalWorlds;
import com.gravypod.PersonalWorlds.commands.Help;

/**
 * This is the command handler for PersonalWorlds
 * 
 * @author gravypod
 * 
 */
public class CommandHandler implements CommandExecutor {
	
	private static PersonalWorlds plugin;
	
	public CommandHandler(final PersonalWorlds plugin) {
	
		CommandHandler.plugin = plugin;
		
	}
	
	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String cmd, final String[] args) {
	
		final Player player;
		
		if (sender instanceof Player) {
			player = (Player) sender;
		} else {
			sender.sendMessage("You are not in game! You cannot use that command!");
			return true;
		}
		
		// This is a permissions check to see if the player has permissions for
		// that argument
		if (args.length >= 1) {
			if (!player.hasPermission(CommandHandler.plugin.getPluginName() + "." + args[0])) {
				player.sendMessage("You do not have permissions for that command!");
				return true;
			}
		} else {
			new Help().command(player, args, CommandHandler.plugin);
			return true;
		}
		
		final String argument = WordUtils.capitalize(args[0].toLowerCase());
		final String className = "com.gravypod.PersonalWorlds.commands." + argument;
		
		if (CommandHandler.plugin.getCommands().contains(className + ".class")) {
			
			try {
				
				CommandHandler.plugin.getServer().getScheduler().scheduleSyncDelayedTask(CommandHandler.plugin, new Runnable() {
					
					@Override
					public void run() {
					
						try {
							
							((ICommand) Class.forName(className).newInstance()).command(player, args, CommandHandler.plugin);
							
						} catch (final Exception e) {
						}
						
					}
					
				}, 1);
				
				return true;
				
			} catch (final Exception e) {
				e.printStackTrace();
			}
			
		}
		
		new Help().command(player, args, CommandHandler.plugin);
		return true;
		
	}
	
}
