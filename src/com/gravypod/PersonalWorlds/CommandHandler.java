package com.gravypod.PersonalWorlds;

import org.apache.commons.lang.WordUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gravypod.PersonalWorlds.commands.Help;


/**
 * This is the command handler for PersonalWorlds
 * 
 * @author gravypod
 *
 */
public class CommandHandler implements CommandExecutor {

	private static PersonalWorlds plugin;
	
	private static CommandSender sender;
	private static Player player;
	private static Command command;
	private static String cmd;
	private static String[] args;
	
	public CommandHandler(PersonalWorlds plugin) {
		
		CommandHandler.plugin = plugin;
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args) {
		Player player = null;
		
		if (sender instanceof Player) {
			player = (Player) sender;
		} else {
			sender.sendMessage("You are not in game! You cannot use that command!");
			return true;
		}
		
		// This is a permissions check to see if the player has permissions for that argument
		if (!player.hasPermission(plugin.getPluginName() + "." + args[0])) {
			player.sendMessage("You do not have permissions for that command!");
			return true;
		}
		
		String argument = WordUtils.capitalize(args[0].toLowerCase());
		final String className = "com.gravypod.PersonalWorlds.commands." + argument;
		
		

		if (plugin.getCommands().contains(className + ".class")) {
			
			try {
				
				CommandHandler.sender = sender;
				CommandHandler.player = player;				
				CommandHandler.command = command;
				CommandHandler.cmd = cmd;
				CommandHandler.args = args;
				
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

					@Override
					public void run() {
						
						try {
							
							Class.forName(className).newInstance();
							
						} catch (Exception e) {
						}
						
					}
					
				}, 1);
				
				return true;
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		Help.command(player, args, plugin);
		return true;
		
	}

	/**
	 * Gets the static plugin instance of PersonalWorlds
	 * 
	 * @return - an instance
	 * 
	 */
	public static PersonalWorlds getPlugin() {
		return plugin;
	}

	/**
	 * Gets the sender of the current command.
	 * 
	 * @return CommandSender
	 * 
	 */
	public static CommandSender getSender() {
		return sender;
	}

	/**
	 * Gets the player that sent a command.
	 * 
	 * @return - Player
	 * 
	 */
	public static Player getPlayer() {
		return player;
	}

	/**
	 * Gets the command we will be processing 
	 * 
	 * @return - Command
	 * 
	 */
	public static Command getCommand() {
		return command;
	}

	/**
	 * Get the command we will be processing
	 * 
	 * @return - String
	 * 
	 */
	public static String getCmd() {
		return cmd;
	}

	/**
	 * Gets the arguments of the command we will be processing
	 * 
	 * @return - String[]
	 */
	public static String[] getArgs() {
		return args;
	}
	
}
