package com.gravypod.PersonalWorlds;

import java.io.File;
import java.io.IOException;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.metadata.FixedMetadataValue;

import com.gravypod.PersonalWorlds.utils.PluginUtil;

/**
 * This is called to create a new player world. Must be passed a PersonalWorlds
 * instance.
 * 
 * @param plugin
 * @param player
 * @param type
 *            is the kind of generator/environment that you use.
 * 
 */
public class WorldGenerator {
	
	final Player player;
	
	final PersonalWorlds plugin;
	
	final String generator;
	
	public WorldGenerator(final PersonalWorlds plugin, final Player player, final String type) {
	
		this.player = player;
		this.plugin = plugin;
		generator = type;
		run();
		
	}
	
	public void run() {
	
		final World w = PluginUtil.loadWorld(player.getName());
		
		if (w == null) {
			
			final String worldName = PluginUtil.worldName(player.getName());
			
			final WorldCreator worldCreator = new WorldCreator(worldName);
			
			final String genName = plugin.getGenerator(generator);
			
			if (genName == null) {
				player.sendMessage("That is not a real generator. Here is a list" + plugin.joinedGenList());
				return;
			}
			
			final ChunkGenerator gen = WorldCreator.getGeneratorForName(worldName, genName, plugin.getServer().getConsoleSender());
			
			boolean isEnv = false;
			
			if (gen != null) {
				worldCreator.generator(genName);
			} else {
				worldCreator.environment(Environment.valueOf(genName));
				
				isEnv = true;
				
			}
			
			final World world = plugin.getServer().createWorld(worldCreator);
			
			if (isEnv) {
				try {
					new File(worldName + System.getProperty("file.separator") + "is" + genName).createNewFile();
				} catch (final IOException e) {
				}
			}
			
			world.setKeepSpawnInMemory(false);
			
			final Location spawnLocation = world.getSpawnLocation();
			
			final Chunk spawnChunk = world.getChunkAt(spawnLocation);
			
			if (!world.isChunkLoaded(spawnChunk)) {
				world.loadChunk(spawnChunk);
				player.sendMessage("Your world has generated!");
			}
			
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				
				@Override
				public void run() {
					
					player.sendMessage("You are getting teleported to your world!");
				
					world.setMetadata(plugin.getPluginName(), new FixedMetadataValue(plugin, true));
					
					final Location safeLoc = PluginUtil.safeSpawnLoc(spawnLocation);
					
					final Block spawnBlock = safeLoc.getBlock().getRelative(BlockFace.DOWN);
					
					final int blockID = Material.COBBLESTONE.getId();
					
					spawnBlock.setTypeId(blockID);
					
					spawnBlock.getRelative(BlockFace.EAST).setTypeId(blockID);
					spawnBlock.getRelative(BlockFace.NORTH).setTypeId(blockID);
					spawnBlock.getRelative(BlockFace.NORTH_EAST).setTypeId(blockID);
					
					world.setSpawnLocation(safeLoc.getBlockX(), safeLoc.getBlockY(), safeLoc.getBlockZ());
					
					player.teleport(safeLoc);
				}
				
			});
			
			
		} else {
			
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				
				@Override
				public void run() {
				
					player.teleport(PluginUtil.safeSpawnLoc(w.getSpawnLocation()));
					
				}
				
			});
			
		}
	}
	
}
