package com.gravypod.PersonalWorlds.utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class TeleportUtils {
	
	/**
	 * Sends a player into his world.
	 * 
	 * @param player
	 *            who wants to go to his world
	 * 
	 */
	public static void tpToMy(final Player player) {
	
		final String playerName = player.getName();
		
		final World world;
		
		if ((world = PluginUtil.loadWorld(playerName)) == null) {
			player.sendMessage("You do not have a world!");
			return;
		}
		
		final Location spawnLocation = world.getSpawnLocation();
		
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
	public static void tpToFriend(final Player player, final String friendsName) {
	
		final Player friend = PluginUtil.matchName(friendsName);
		
		if (friend == null) {
			player.sendMessage("There is no one online with that name.");
			return;
		}
		
		final World world;
		
		if ((world = PluginUtil.loadWorld(friendsName)) == null) {
			player.sendMessage("You do not have a world!");
			return;
		}
		
		if (!friend.getWorld().equals(world)) {
			friend.sendMessage(player.getName() + " has teleported to your world!");
		}
		
		final Location spawnLocation = world.getSpawnLocation();
		
		world.getChunkAt(spawnLocation).load();
		
		player.teleport(PluginUtil.safeSpawnLoc(spawnLocation));
		
	}
	
	/**
	 * 
	 * Teleport to a users world and override the need for them to be online.
	 * 
	 * @param player
	 *            - Player using command
	 * @param worldName
	 *            - world
	 * 
	 */
	public static void tpOverride(final Player player, final String worldName) {
	
		final World world;
		
		if ((world = PluginUtil.loadWorld(worldName)) == null) {
			player.sendMessage("That is not a real world!");
			return;
		}
		
		final Location spawnLocation = world.getSpawnLocation();
		
		world.getChunkAt(spawnLocation).load();
		
		player.teleport(PluginUtil.safeSpawnLoc(spawnLocation));
		
	}
	
}
