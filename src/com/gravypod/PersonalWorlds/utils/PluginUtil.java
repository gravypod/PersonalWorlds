package com.gravypod.PersonalWorlds.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.bukkit.ChatColor;
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
 * From src in <a
 * href='http://palmergames.com/downloads/antix/AntiX.jar'>AntiX</a>
 * 
 * @author <a href='https://github.com/ElgarL'>ElgarL</a>
 * 
 */
public class PluginUtil {
	
	static PersonalWorlds plugin;
	
	static Server server;
	
	static int borderSize;
	
	private static String fileSeparator = System.getProperty("file.separator");
	
	/**
	 * Initiation of the static class.
	 * 
	 * @param init
	 * 
	 */
	public static void init(final PersonalWorlds init) {
	
		PluginUtil.plugin = init;
		PluginUtil.server = PluginUtil.plugin.getServer();
		PluginUtil.borderSize = PluginUtil.plugin.getBorderSize();
		
	}
	
	/**
	 * Match a player with a player name you give
	 * 
	 * @param name
	 * @return String of the Players name online or off-line
	 * 
	 */
	public static String matchPlayer(final String name) {
	
		final List<Player> players = PluginUtil.server.matchPlayer(name);
		
		switch(players.size()) {
		
			case 0:
				return PluginUtil.matchOfflinePlayer(name);
			case 1:
				return players.get(0).getName();
				
			default:
				return null;
		}
		
	}
	
	/**
	 * Match a player with a player name you give
	 * 
	 * @param name
	 * @return Player who matched the name. Player will be online, null if no
	 *         one.
	 * 
	 */
	public static Player matchOnlinePlayer(final String name) {
	
		final List<Player> players = PluginUtil.server.matchPlayer(name);
		
		switch(players.size()) {
		
			case 1:
				return players.get(0);
			default:
				return null;
		}
		
	}
	
	public static Player matchName(final String name) {
	
		return PluginUtil.server.getPlayer(name);
	}
	
	/**
	 * Matches an String to an off-line player
	 * 
	 * @param name
	 * @return player name or null if there is no player
	 * 
	 */
	public static String matchOfflinePlayer(final String name) {
	
		for (final OfflinePlayer player : PluginUtil.server.getOfflinePlayers()) {
			if (player.getName().equals(name)) {
				return player.getName();
			}
		}
		
		return null;
	}
	
	/**
	 * Get the chunk the player is in.
	 * 
	 * @param name
	 *            must match a player, no error checking. Use
	 *            {@code=matchPlayer}
	 * @return Chunk
	 * 
	 */
	public static Chunk getPlayerChunk(final String name) {
	
		return PluginUtil.server.getPlayer(name).getLocation().getChunk();
		
	}
	
	/**
	 * Gets a Array of online players.
	 * 
	 * @return Array of player objects
	 * 
	 */
	public static Player[] getOnlinePlayers() {
	
		return PluginUtil.server.getOnlinePlayers();
	}
	
	/**
	 * Returns a String with path formating.
	 * 
	 * @param name
	 * @return String containing personal world path and name
	 * 
	 */
	public static String worldName(final String name) {
	
		return PluginUtil.plugin.getPluginName() + System.getProperty("file.separator") + name;
		
	}
	
	public static String worldName(final String name, final String worldName) {
	
		return PluginUtil.worldName(name) + "_" + worldName;
	}
	
	/**
	 * Finds if a world name is a player owned world.
	 * 
	 * @param worldName
	 * @return True if this is a player world.
	 * 
	 */
	public static Boolean isPlayerWorld(final String worldName) {
	
		return worldName.startsWith(PluginUtil.plugin.getPluginName() + System.getProperty("file.separator"));
		
	}
	
	/**
	 * Makes a safe spawn location.
	 * 
	 * @param spawnLocation
	 * @return Modified location so you are not buried.
	 * 
	 */
	public static Location safeSpawnLoc(final Location spawnLocation) {
	
		if (spawnLocation.getBlock().getType() == Material.AIR) {
			
			spawnLocation.setY(spawnLocation.getWorld().getMaxHeight());
			
			final World world = spawnLocation.getWorld();
			
			return world.getHighestBlockAt(spawnLocation).getLocation();
			
		}
		
		return spawnLocation;
		
	}
	
	/**
	 * Tests if the player has a personal world.
	 * 
	 * @param playerName
	 * @return True if they own a world
	 * 
	 */
	public static boolean hasWorld(final String playerName) {
	
		final File worldFile = new File(PluginUtil.worldName(playerName));
		
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
	public static boolean isWorldLoaded(final String playerName) {
	
		return PluginUtil.plugin.getServer().getWorld(PluginUtil.worldName(playerName)) != null;
		
	}
	
	/**
	 * List all the worlds a player has
	 * 
	 * @param player
	 * @return
	 */
	public static String[] listWorlds(final String player) {
	
		final File[] worldFolder = new File(PluginUtil.plugin.getName()).listFiles();
		
		final StringBuilder homes = new StringBuilder(ChatColor.AQUA.toString() + "");
		
		int howMany = 0;
		
		for (final File worldFile : worldFolder) {
			
			if (worldFile.isDirectory() && worldFile.getName().startsWith(player)) {
				
				String name = worldFile.getName();
				
				if (name.equals(player)) {
					
					name = "main";
					
				} else {
					
					name = name.substring(player.length() + 1);
					
				}
				
				homes.append(name + ", ");
				
				howMany++;
				
			}
			
		}
		
		final String[] messages = new String[] { ChatColor.AQUA.toString() + "You have " + howMany + " homes.", homes.substring(0, homes.length() - 2).toString() };
		
		return messages;
		
	}
	
	/**
	 * Loads a world from a player name.
	 * 
	 * @param playerName
	 *            - Player to make a world for
	 * @param givenWorldName
	 *            - World name given by the player
	 * @return World object that has been loaded
	 * 
	 */
	public static World loadWorld(final String playerName, final String givenWorldName) {
	
		if (PluginUtil.hasWorld(playerName)) {
			
			World world;
			
			final String worldName = givenWorldName == "" ? PluginUtil.worldName(playerName) : PluginUtil.worldName(playerName, givenWorldName);
			
			if (PluginUtil.isWorldLoaded(worldName)) {
				
				world = PluginUtil.plugin.getServer().getWorld(worldName);
				
			} else {
				
				Environment worldEnv = null;
				for (final Environment env : Environment.values()) {
					final File checkFile = new File(worldName + System.getProperty("file.separator") + "is" + env.name());
					if (checkFile.exists()) {
						worldEnv = env;
					}
				}
				
				final WorldCreator worldCreator = new WorldCreator(worldName);
				
				if (worldEnv != null) {
					worldCreator.environment(worldEnv);
				}
				
				world = PluginUtil.plugin.getServer().createWorld(worldCreator);
				
				world.setMetadata(PluginUtil.plugin.getPluginName(), new FixedMetadataValue(PluginUtil.plugin, true));
				
			}
			
			world.setKeepSpawnInMemory(false);
			return world;
			
		}
		
		return null;
		
	}
	
	/**
	 * Loads a world from a player name.
	 * 
	 * @param playerName
	 * @return World object that has been loaded
	 * 
	 */
	public static World loadWorld(final String playerName) {
	
		return PluginUtil.loadWorld(playerName, "");
		
	}
	
	/**
	 * Checks if the the player is outside of the personal world border.
	 * 
	 * @param destination
	 * @return True if they are out of bounds
	 * 
	 */
	public static boolean borderTest(final Location destination) {
	
		final int x = destination.getBlockX();
		final int z = destination.getBlockZ();
		
		final Location spawnLoc = destination.getWorld().getSpawnLocation();
		
		final int x1 = spawnLoc.getBlockX();
		final int z1 = spawnLoc.getBlockZ();
		
		boolean hasChanged = false;
		
		if (x >= x1 + PluginUtil.borderSize) {
			destination.setX(x - 5);
			hasChanged = true;
		} else if (x <= x1 - PluginUtil.borderSize) {
			destination.setX(x + 5);
			hasChanged = true;
		}
		
		if (z >= z1 + PluginUtil.borderSize) {
			destination.setZ(z - 5);
			hasChanged = true;
		} else if (z <= z1 - PluginUtil.borderSize) {
			destination.setZ(z + 5);
			hasChanged = true;
		}
		
		return hasChanged;
		
	}
	
	/**
	 * Delete a world that is matched to a player name. This delayed one tick
	 * after the world is unloaded to fully delete.
	 * 
	 * @param player
	 * 
	 */
	public static void deleteWorld(final Player sender, final String player) {
	
		if (!PluginUtil.hasWorld(player)) {
			
			sender.sendMessage("That world does not exist");
			return;
			
		}
		
		final String worldName = PluginUtil.worldName(player);
		
		final Server server = PluginUtil.plugin.getServer();
		
		final World world = server.getWorld(worldName);
		
		if (world == null) {
			
			sender.sendMessage("The world is unloaded, deleting");
			
		} else {
			
			PluginUtil.kickPlayersOut(player, true, "This world is going to be deleted! You have been kicked!");
			
			server.unloadWorld(worldName, false);
			
		}
		
		server.getScheduler().runTaskAsynchronously(PluginUtil.plugin, new Runnable() {
			
			@Override
			public void run() {
			
				final File worldFile = new File(worldName);
				
				PluginUtil.deleteFile(worldFile);
				
			}
			
		});
		
	}
	
	/**
	 * Kicks all the players to the main world spawn.
	 * 
	 * @param worldOwner
	 * @param kickall
	 *            is for if you want to kick everyone, event the owner, out of
	 *            the world.
	 * @param kickMessage
	 *            is the message players that are kicked are send after the
	 *            kick.
	 * 
	 */
	public static void kickPlayersOut(final String worldOwner, final boolean kickall, final String kickMessage) {
	
		final World world = PluginUtil.plugin.getServer().getWorld(PluginUtil.worldName(worldOwner));
		
		if (world == null) {
			
			return;
			
		}
		
		final Location mainWorldSpawn = PluginUtil.plugin.getServer().getWorlds().get(0).getSpawnLocation();
		
		for (final Player player : world.getPlayers()) {
			
			if (!player.getName().equals(worldOwner) || kickall) {
				
				player.teleport(mainWorldSpawn);
				player.sendMessage(kickMessage);
				
			}
			
		}
		
	}
	
	/**
	 * Delete file, or if path represents a directory, recursively delete it's
	 * contents beforehand.
	 * 
	 * @param file
	 * 
	 */
	public static void deleteFile(final File folder) {
	
		synchronized(folder) {
		    File[] files = folder.listFiles();
		    
		    if(files!=null) { //some JVMs return null for empty dirs
		        
		    	for(File f: files) {
		            
		    		if(f.isDirectory()) {
		                
		    			deleteFile(f);
		            
		    		} else {
		                
		    			f.delete();
		            }
		    		
		        }
		    	
		    }
		    
		    folder.delete();
			
		}
		
	}
	
	/**
	 * Scans all classes accessible from the context class loader which belong
	 * to the given package and subpackages.
	 * 
	 * @param packageName
	 *            The base package
	 * @return The classes
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * 
	 */
	public static Class<?>[] getClasses(final String packageName) throws ClassNotFoundException, IOException {
	
		final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		
		assert classLoader != null;
		
		final String path = packageName.replace('.', '/');
		
		final Enumeration<URL> resources = classLoader.getResources(path);
		
		final List<File> dirs = new ArrayList<File>();
		
		while(resources.hasMoreElements()) {
			
			final URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
			
		}
		
		final ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		
		for (final File directory : dirs) {
			
			classes.addAll(PluginUtil.findClasses(directory, packageName));
			
		}
		
		return classes.toArray(new Class[classes.size()]);
		
	}
	
	/**
	 * Recursive method used to find all classes in a given directory and
	 * subdirs.
	 * 
	 * @param directory
	 *            The base directory
	 * @param packageName
	 *            The package name for classes found inside the base directory
	 * @return The classes
	 * @throws ClassNotFoundException
	 */
	private static List<Class<?>> findClasses(final File directory, final String packageName) throws ClassNotFoundException {
	
		final List<Class<?>> classes = new ArrayList<Class<?>>();
		
		if (!directory.exists()) {
			
			return classes;
			
		}
		
		final File[] files = directory.listFiles();
		
		for (final File file : files) {
			
			if (file.isDirectory()) {
				
				assert !file.getName().contains(".");
				classes.addAll(PluginUtil.findClasses(file, packageName + "." + file.getName()));
				
			} else if (file.getName().endsWith(".class")) {
				
				classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
				
			}
			
		}
		
		return classes;
		
	}
	
	public static boolean isWorldOwner(final String playerName, final String worldName) {
	
		return PluginUtil.worldName(playerName).equals(worldName);
		
	}
	
	public static String getFileSeparator() {
	
		return PluginUtil.fileSeparator;
	}
	
	public static void setFileSeparator(final String fileSeparator) {
	
		PluginUtil.fileSeparator = fileSeparator;
	}
	
}
