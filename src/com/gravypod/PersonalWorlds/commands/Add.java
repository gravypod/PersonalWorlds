package com.gravypod.PersonalWorlds.commands;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import com.gravypod.PersonalWorlds.Permissions;
import com.gravypod.PersonalWorlds.PersonalWorlds;
import com.gravypod.PersonalWorlds.WorldGenerator;
import com.gravypod.PersonalWorlds.CommandHandlers.ICommand;
import com.gravypod.PersonalWorlds.utils.PluginUtil;

/**
 * This class deals with the argument "add" and "create". Creates an world for a
 * player.
 * 
 * @author gravypod and ElgarL
 * 
 */
public class Add implements ICommand {
	
	@Override
	public void command(final Player player, final String[] args, final PersonalWorlds plugin) {
	
		if (args.length >= 2 && Permissions.GENERATOR.canUse(player, args[1])) {
			
			if (PluginUtil.hasWorld(player.getName())) {
				
				player.sendMessage(plugin.getMessage("alreadyOwnWorld"));
				
			}
			
			if (!PluginUtil.hasWorld(player.getName())) {
				new WorldGenerator(plugin, player, args[1]);
			}
			
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				
				@Override
				public void run() {
				
					player.setMetadata(plugin.getPluginName(), new FixedMetadataValue(plugin, true));
				}
			});
			
		} else {
			
			player.sendMessage(plugin.getMessage("invalidWorldGenerator") + plugin.joinedGenList());
			
		}
		
	}
	
}
