package com.gravypod.PersonalWorlds.Listener;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.MetadataValue;

import com.gravypod.PersonalWorlds.PersonalWorlds;
import com.gravypod.PersonalWorlds.PluginUtil;

/**
 * Handler for player events.
 * 
 * @author gravypod
 *
 */
public class PlayerListener implements Listener {

	PersonalWorlds plugin;

	public PlayerListener(PersonalWorlds plugin) {

		this.plugin = plugin;

	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void playerListener(PlayerMoveEvent event) {
		
		Player p = event.getPlayer();
			
		World playerWorld = event.getPlayer().getWorld();
		
		// Checks if the player name == a world name; If so, it /must/ be a player world
		
		List<MetadataValue> m = playerWorld.getMetadata(plugin.getPluginName());
		
		if(m.isEmpty())
			return;
		
		if (m.get(0).asBoolean()) {
			
			Location loc = event.getTo();
			
			if (PluginUtil.borderTest(loc)) {
				
				p.teleport(PluginUtil.safeSpawnLoc(loc));
				
				p.sendMessage("You have reached the end of that players world!");
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void prePlayerLogin(PlayerPreLoginEvent event) {

		PluginUtil.loadWorld(event.getName());
		
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void playerLogin(PlayerLoginEvent event) {

		Player player = event.getPlayer();
		
		if (!player.getWorld().hasMetadata(plugin.getPluginName())) {
			plugin.getServer().unloadWorld(PluginUtil.worldName(player.getName()), true);
		}
		
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void playerLogoutEvent(PlayerQuitEvent event) {
		
		final String playerName = event.getPlayer().getName();
		
		PluginUtil.kickPlayersOut(playerName, false, "The worlds owner has left the game! You have been removed.");;
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
			
				plugin.getServer().unloadWorld(PluginUtil.worldName(playerName), true);
			
			}
		});
		
	}
	
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void playerChangeWorldListener(PlayerChangedWorldEvent event) {
		
		World worldFrom = event.getFrom();
		
		if (worldFrom.getPlayers().size() <= 1) {
			if (PluginUtil.isPlayerWorld(worldFrom.getName())) {
				if (plugin.getServer().unloadWorld(worldFrom, true)) {
					System.out.println("World Saved");
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void playerTpEvent(PlayerTeleportEvent event) {
		
		Player player = event.getPlayer();
		
		if (PluginUtil.isPlayerWorld(player.getName())) {
			if (PluginUtil.borderTest(event.getTo())) {
				player.sendMessage("You cannot tp out of your world!");
				event.setCancelled(true);
			}
		}
		
	}
	}
