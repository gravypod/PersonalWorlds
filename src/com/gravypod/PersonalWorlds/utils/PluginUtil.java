package com.gravypod.PersonalWorlds.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import com.gravypod.PersonalWorlds.PersonalWorlds;

/**
 * 
 * From src in <a href='http://palmergames.com/downloads/antix/AntiX.jar'>AntiX</a>
 * @author <a href='https://github.com/ElgarL'>ElgarL</a>
 *
 */
public class PluginUtil {

	static PersonalWorlds plugin;
	static Server server;
	static int borderSize;
	
	/**
	 * Initiation of the static class.
	 * 
	 * @param init
	 * 
	 */
	public static void init(PersonalWorlds init) {
		
		plugin = init;
		server = plugin.getServer();
		borderSize = plugin.getBorderSize();
	}
	
	/**
	 * Match a player with a player name you give
	 * 
	 * @param name
	 * @return String of the Players name online or off-line
	 * 
	 */
	public static String matchPlayer(String name) {

		List<Player> players = server.matchPlayer(name);

		switch (players.size()) {
		
		case 0:
			return matchOfflinePlayer(name);
		case 1:
			return ((Player) players.get(0)).getName();
			
		default:
			return null;
		}
		
	}
	
	/**
	 * Matches an String to an off-line player 
	 * 
	 * @param name
	 * @return player name or null if there is no player
	 * 
	 */
	public static String matchOfflinePlayer(String name) {
		
		for (OfflinePlayer player : server.getOfflinePlayers())
			if (player.getName().equals(name))
				return player.getName();
		
		return null;
	}
	
	/**
	 * Get the chunk the player is in.
	 * 
	 * @param name must match a player, no error checking. Use {@code=matchPlayer}
	 * @return Chunk
	 * 
	 */
	public static Chunk getPlayerChunk(String name) {
		
		return server.getPlayer(name).getLocation().getChunk();
		
	}
	
	/**
	 * Gets a Array of online players.
	 * 
	 * @return Array of player objects
	 * 
	 */
	public static Player[] getOnlinePlayers() {

		return server.getOnlinePlayers();
	}
	
	/**
	 * Returns a String with path formating.
	 * 
	 * @param name
	 * @return String containing personal world path and name
	 * 
	 */
	public static String worldName(String name) {
		
		return plugin.getPluginName() + System.getProperty("file.separator") + name;
		
	}
	
	/**
	 * Finds if a world name is a player owned world.
	 * 
	 * @param playerName
	 * @return True if this is a player world.
	 * 
	 */
	public static Boolean isPlayerWorld(String playerName) {
		
		return playerName.startsWith(plugin.getPluginName() + System.getProperty("file.separator"));
		
	}

	/**
	 * Makes a safe spawn location.
	 * 
	 * @param spawnLocation
	 * @return Modified location so you are not buried.
	 * 
	 */
	public static Location safeSpawnLoc(Location spawnLocation) {
		
		Location blockLocation = spawnLocation;
		
		//find the first non air block below us
		while (blockLocation.getBlock().getType() != Material.AIR) {
		    blockLocation.setY(blockLocation.getY() + 1);
		}
		
		// set to 1 block up so we are not sunk in the ground
		blockLocation.setY(blockLocation.getY() + 1);
		
		return blockLocation;
		
	}
	
	/**
	 * Tests if the player has a personal world.
	 * 
	 * @param playerName
	 * @return True if they own a world
	 * 
	 */
	public static boolean hasWorld(String playerName) {
		
		File worldFile = new File(worldName(playerName));
		
		if (worldFile.exists()) {
			if (worldFile.isDirectory()) {
				return true;
			}
		}
		
		return false;
		
	}
	
	/**
	 * Checks if a world is loaded
	 * 
	 * @param playerName
	 * @return True if the world is loaded
	 * 
	 */
	public static boolean isWorldLoaded(String playerName) {
		
		return plugin.getServer().getWorld(worldName(playerName)) != null;

	}
	
	/**
	 * Loads a world from a player name.
	 * 
	 * @param playerName
	 * @return World object that has been loaded
	 * 
	 */
	public static World loadWorld(String playerName) {
		
		if (PluginUtil.hasWorld(playerName)) {
			
			World world;
			
			String worldName = PluginUtil.worldName(playerName);
			
			if (PluginUtil.isWorldLoaded(worldName)) {
				
				world = plugin.getServer().getWorld(worldName);
				
			} else {
				
				Environment worldEnv = null;
				for (Environment env : Environment.values()) {
					//new File(worldName + System.getProperty("file.separator") + "is" + genName)
					File checkFile = new File(worldName + System.getProperty("file.separator") + "is" + env.name());
					if (checkFile.exists()) {
						worldEnv = env;
						//continue;
					}
				}

				WorldCreator worldCreator = new WorldCreator(worldName);
				
				if (worldEnv != null) {
					worldCreator.environment(worldEnv);
				}
				
				world = plugin.getServer().createWorld(worldCreator);
				
				world.setMetadata(plugin.getPluginName(), new FixedMetadataValue(plugin, true));
				
			} 
			
			world.setKeepSpawnInMemory(false);
			return world;
			
		}
		
		return null;
		
	}
	
	/**
	 * Checks if the the player is outside of the personal world border.
	 * 
	 * @param destination
	 * @return True if they are out of bounds
	 * 
	 */
	public static boolean borderTest(Location destination) {
		
		int x = destination.getBlockX();
		int z = destination.getBlockZ();
		
		Location spawnLoc = destination.getWorld().getSpawnLocation();
		
		int x1 = spawnLoc.getBlockX();
		int z1 = spawnLoc.getBlockZ();
		
		boolean hasChanged = false;
		
		if (x >= x1 + borderSize) {
			destination.setX(x - 5);
			hasChanged = true;
		} else if (x <= x1 - borderSize) {
			destination.setX(x + 5);
			hasChanged = true;
		}
			
		if (z >= z1 + borderSize) {
			destination.setZ(z - 5);
			hasChanged = true;
		} else if (z <= z1 - borderSize) {
			destination.setZ(z + 5);
			hasChanged = true;
		}
		
		return hasChanged;
		
	}
	
	/**
	 * Delete a world that is matched to a player name.
	 * This delayed one tick after the world is unloaded to fully delete.
	 * 
	 * @param player
	 * 
	 */
	public static void deleteWorld(String player) {
		
		String playerName = matchPlayer(player);
		
		
		if (PluginUtil.hasWorld(playerName)) {
		
			final String worldName = worldName(playerName);
					
			World world = plugin.getServer().getWorld(worldName);
			
			if (world == null) {
				
				return;
			}
			
			PluginUtil.kickPlayersOut(playerName, true, "This world is going to be deleted! You have been kicked!");
			
			plugin.getServer().unloadWorld(worldName, false);
			
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				
				@Override
				public void run() {
					
					File worldFile = new File(worldName);
					
					deleteFile(worldFile);
					
				}
				
			});
			
			
			
		}
		
	}
	
	/**
	 * Kicks all the players to the main world spawn. 
	 * 
	 * @param worldOwner
	 * @param kickall is for if you want to kick everyone, event the owner, out of the world.
	 * @param kickMessage is the message players that are kicked are send after the kick.
	 * 
	 */
	public static void kickPlayersOut(String worldOwner, boolean kickall, String kickMessage) {
		
		World world = plugin.getServer().getWorld(PluginUtil.worldName(worldOwner));
		
		if (world == null) {
			return;
		}
		
		Location mainWorldSpawn = plugin.getServer().getWorlds().get(0).getSpawnLocation();
		
		for (Player player : world.getPlayers()) {
			if (!player.getName().equals(worldOwner) || kickall) {
				player.teleport(mainWorldSpawn);
				player.sendMessage(kickMessage);
			}
		}
		
	}
	
	/**
	 * Delete file, or if path represents a directory, recursively
	 * delete it's contents beforehand.
	 * 
	 * @param file
	 * 
	 */
	public static void deleteFile(File file) {
		
		synchronized(file) {
			
			if (file.isDirectory()) {
				File[] children = file.listFiles();
				if (children != null) {
					for (File child : children)
						deleteFile(child);
				}
				children = file.listFiles();
				if (children == null || children.length == 0) {
					if (!file.delete())
						System.out.println("Error: Could not delete folder: " + file.getPath());
				}
			} else if (file.isFile()) {
				if (!file.delete())
					System.out.println("Error: Could not delete file: " + file.getPath());
			}
		}
	}
	
	/**
	 * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
	 *
	 * @param packageName The base package
	 * @return The classes
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * 
	 */
	public static Class<?>[] getClasses(String packageName) throws ClassNotFoundException, IOException {
		
	    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	    
	    assert classLoader != null;
	    
	    String path = packageName.replace('.', '/');
	    
	    Enumeration<URL> resources = classLoader.getResources(path);
	    
	    List<File> dirs = new ArrayList<File>();
	    
	    while (resources.hasMoreElements()) {
	    	
	        URL resource = resources.nextElement();
	        dirs.add(new File(resource.getFile()));
	        
	    }
	    
	    ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
	    
	    for (File directory : dirs) {
	    	
	        classes.addAll(findClasses(directory, packageName));
	        
	    }
	    
	    return classes.toArray(new Class[classes.size()]);
	    
	}
	
	/**
	 * Recursive method used to find all classes in a given directory and subdirs.
	 *
	 * @param directory   The base directory
	 * @param packageName The package name for classes found inside the base directory
	 * @return The classes
	 * @throws ClassNotFoundException
	 */
	private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
		
	    List<Class<?>> classes = new ArrayList<Class<?>>();
	    
	    if (!directory.exists()) {
	        return classes;
	    }
	    
	    File[] files = directory.listFiles();
	    
	    for (File file : files) {
	    	
	        if (file.isDirectory()) {
	        	
	            assert !file.getName().contains(".");
	            classes.addAll(findClasses(file, packageName + "." + file.getName()));
	            
	        } else if (file.getName().endsWith(".class")) {
	        	
	            classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));

	        }
	        
	    }
	    
	    return classes;
	    
	}
	
	
	
	
}
