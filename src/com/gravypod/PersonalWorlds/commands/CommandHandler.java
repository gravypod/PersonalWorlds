package com.gravypod.PersonalWorlds.commands;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import com.gravypod.PersonalWorlds.PersonalWorlds;
import com.gravypod.PersonalWorlds.PluginUtil;
import com.gravypod.PersonalWorlds.WorldGenerator;

/**
 * This is the command handler for PersonalWorlds
 * 
 * @author gravypod
 *
 */
public class CommandHandler implements CommandExecutor {

	PersonalWorlds plugin;
	
	private enum commands {
		help, tp, add, create, generator, delete
	}
	
	public CommandHandler(PersonalWorlds plugin) {
		
		this.plugin = plugin;
		
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
		
		if (args.length == 0) {
			help(sender);
			return true;
		}
		
		// This is a permissions check to see if the player has permissions for that argument
		if (!player.hasPermission(plugin.getPluginName() + "." + args[0])) {
			player.sendMessage("You do not have permissions for that command!");
			return true;
		}
		
		try {
			switch (commands.valueOf(args[0].toLowerCase())) {
			
			case help:
				help(sender);
				return true;
			case tp:
				if (args.length == 2) {
					tpToFriend(player, args[1]);
				} else {
					tpToMy(player);
				}
				return true;
			case add:
			case create:
				if (args.length == 2) {
					add(player, args[1]);
				} else {
					player.sendMessage("The generators you can use are:" + plugin.joinedGenList());
				}
				return true;
			case generator:
				player.sendMessage("The generators you can use are:" + plugin.joinedGenList());
				return true;
			case delete:
				if (args.length == 2) {
					if (player.hasPermission(plugin.getPluginName() + "." + args[0] + "other"))
						deleteWorld(args[1]);
					else if (player.getName().equalsIgnoreCase(args[1]))
						deleteWorld(args[1]);
					else 
						player.sendMessage("You cannot delete that players world!");
				} else {
					help(sender);
				}
				return true;
			default:
				help(sender);
				return true;
			}
		} catch (Exception e) {
			help(sender);
			return true;
		}
	}

	/**
	 * Sends help messages.
	 * 
	 * @param sender that you want the help messages to be sent to.
	 * 
	 */
	public void help(CommandSender sender) {
		
		sender.sendMessage("---]Help[---");
		sender.sendMessage("help - This page");
		sender.sendMessage("tp - Teleport to your world");
		sender.sendMessage("add/create {generator} - Create your own world.");
		sender.sendMessage("generator - Lists the generators you can use");
		sender.sendMessage("delete - Delete a world. Cannot be undone!");
		
	}
	
	/**
	 * Adds a world for a player.
	 * 
	 * @param player that the world is being made for.
	 * @param args are the type of generator we want to use.
	 * 
	 */
	public void add(Player player, String args) {
			
		new WorldGenerator(plugin, player, args);
			
		player.setMetadata(plugin.getPluginName(), new FixedMetadataValue(this.plugin, true));
		
	}
	
	/**
	 * Sends a player into his world.
	 * 
	 * @param player who wants to go to his world
	 * 
	 */
	public void tpToMy(Player player) {
		
		String playerName = player.getName();
		
		World world;
		
		if ((world = PluginUtil.loadWorld(playerName)) == null) {
			player.sendMessage("You do not have a world!");
			return;
		}
			
		Location spawnLocation = world.getSpawnLocation();
			
		world.loadChunk(spawnLocation.getChunk());
		
		player.teleport(PluginUtil.safeSpawnLoc(spawnLocation));
			
	}
	
	/**
	 * Send a player into another players world.
	 * 
	 * @param player
	 * @param friendsName
	 * 
	 */
	public void tpToFriend(Player player, String friendsName) {
		
		Player friend = plugin.getServer().getPlayer(friendsName);
		
		if (friend == null) {
			player.sendMessage("There is no one online with that name.");
			return;
		}
		
		World world;
		
		if ((world = PluginUtil.loadWorld(friendsName)) == null) {
			player.sendMessage("You do not have a world!");
			return;
		}
		
		if (!friend.getWorld().equals(world)) {
			friend.sendMessage(player.getName() + " has gone to your world!");
		}
		
		Location spawnLocation = world.getSpawnLocation();
		
		world.getChunkAt(spawnLocation).load();
		
		player.teleport(PluginUtil.safeSpawnLoc(spawnLocation));
		
	}
	
	/**
	 * Deletes a world that is owned by 'name'
	 * 
	 * @param name is a player name.
	 * 
	 */
	public void deleteWorld(String name) {
		
		PluginUtil.deleteWorld(name);
		
	}
	
}
