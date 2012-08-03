package com.gravypod.PersonalWorlds.commands;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.gravypod.PersonalWorlds.CommandHandler;
import com.gravypod.PersonalWorlds.PersonalWorlds;
import com.gravypod.PersonalWorlds.PluginUtil;

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

		command((Player) CommandHandler.getSender(), CommandHandler.getArgs(), CommandHandler.getPlugin());
		
	}
	
	private static void command(Player player, String[] args, PersonalWorlds plugin) {
		
		if (args.length == 2) {
			tpToFriend(player, args[1], plugin);
		} else {
			tpToMy(player);
		}
		
	}
	
	/**
	 * Sends a player into his world.
	 * 
	 * @param player who wants to go to his world
	 * 
	 */
	public static void tpToMy(Player player) {
		
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
	public static void tpToFriend(Player player, String friendsName, PersonalWorlds plugin) {
		
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
			friend.sendMessage(player.getName() + " has teleported to your world!");
		}
		
		Location spawnLocation = world.getSpawnLocation();
		
		world.getChunkAt(spawnLocation).load();
		
		player.teleport(PluginUtil.safeSpawnLoc(spawnLocation));
		
	}
	
}
