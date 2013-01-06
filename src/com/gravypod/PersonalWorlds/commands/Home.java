package com.gravypod.PersonalWorlds.commands;

import org.bukkit.World;
import org.bukkit.entity.Player;

import com.gravypod.PersonalWorlds.PersonalWorlds;
import com.gravypod.PersonalWorlds.CommandHandlers.ICommand;
import com.gravypod.PersonalWorlds.utils.PluginUtil;
import com.gravypod.PersonalWorlds.utils.TeleportUtils;

public class Home implements ICommand {
	
	@Override
	public void command(Player player, String[] args, PersonalWorlds plugin) {
	
		if (args.length == 2) {
			
			final World world;
			
			if ((world = PluginUtil.loadWorld(player.getName(), args[1])) != null) {
				TeleportUtils.teleportToWorld(world, player);
				return;
			}
			
		} else {
			TeleportUtils.tpToMy(player);
		}
		
	}
	
}
