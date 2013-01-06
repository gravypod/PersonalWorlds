package com.gravypod.PersonalWorlds.utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.gravypod.PersonalWorlds.PersonalWorlds;

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
		
		TeleportUtils.teleportToWorld(world, player);
		
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
		
		TeleportUtils.teleportToWorld(world, player);
		
	}
	
	public static void teleportToWorld(final World world, final Player player) {
	
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
	public static void tpOverride(final Player player, final String worldName, PersonalWorlds plugin) {
	
		final World world;
		
		
		if (!PluginUtil.isWorldLoaded(worldName)) {
			if ((world = PluginUtil.loadWorld(worldName)) == null) {
				player.sendMessage("That is not a real world!");
				return;
			}
		} else {
			
			world = PluginUtil.matchOnlinePlayer(worldName.split("_")[0]).getWorld();
			
		}
		
		TeleportUtils.teleportToWorld(world, player);
		
	}
	
}
