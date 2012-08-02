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

/**
 * This is called to create a new player world.
 * Must be passed a PersonalWorlds instance.
 * 
 * @param plugin
 * @param player
 * @param type is the kind of generator/environment that you use.
 * 
 */
public class WorldGenerator {
	
	Player player;
	PersonalWorlds plugin;
	String generator;
	
	public WorldGenerator(PersonalWorlds plugin, Player player, String type) {
		
		this.player = player;
		this.plugin = plugin;
		this.generator = type;
		run();
		
	}
	
	public void run() {
		
		final World w = PluginUtil.loadWorld(player.getName());
		
		if (w == null) {
		
			String worldName = PluginUtil.worldName(player.getName());
			
			WorldCreator worldCreator = new WorldCreator(worldName);
			
			String genName = plugin.getGenerator(generator);
				
			if (genName == null) {
				player.sendMessage("That is not a real generator. Here is a list" + plugin.joinedGenList());
				return;
			}
		
			ChunkGenerator gen = WorldCreator.getGeneratorForName(worldName, genName, null);
			
			boolean isEnv = false;
			
			if (gen != null) {
				worldCreator.generator(genName);
			} else  {
				worldCreator.environment(Environment.valueOf(genName));
				
				isEnv = true;
				
			}
			
			World world = worldCreator.createWorld();
		
			
			if (isEnv) {
				try {
					(new File(worldName + System.getProperty("file.separator") + "is" + genName)).createNewFile();
				} catch (IOException e) {
				}
			}
			
			world.setKeepSpawnInMemory(false);
			
			final Location spawnLocation = world.getSpawnLocation().clone();
		
			Chunk spawnChunk = world.getChunkAt(spawnLocation);
		
			if (!world.isChunkLoaded(spawnChunk)) {
				world.loadChunk(spawnChunk);
			}
			
			world.setMetadata(plugin.getPluginName(), new FixedMetadataValue(plugin, true));
		
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

				@Override
				public void run() {
					
					Location safeLoc = PluginUtil.safeSpawnLoc(spawnLocation);
					
					Block spawnBlock = safeLoc.getBlock().getRelative(BlockFace.DOWN);
					
					int blockID = Material.COBBLESTONE.getId();
					
					spawnBlock.setTypeId(blockID);
					
					spawnBlock.getRelative(BlockFace.EAST).setTypeId(blockID);
					spawnBlock.getRelative(BlockFace.NORTH).setTypeId(blockID);
					spawnBlock.getRelative(BlockFace.NORTH_EAST).setTypeId(blockID);
				
					player.teleport(PluginUtil.safeSpawnLoc(spawnLocation));
				
				}
			
			});
			
			player.sendMessage("Your world has generated!");
			
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
