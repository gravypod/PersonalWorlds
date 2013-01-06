package com.gravypod.PersonalWorlds.commands;

import org.bukkit.entity.Player;

import com.gravypod.PersonalWorlds.Permissions;
import com.gravypod.PersonalWorlds.PersonalWorlds;
import com.gravypod.PersonalWorlds.CommandHandlers.ICommand;
import com.gravypod.PersonalWorlds.utils.PluginUtil;


public class Kick implements ICommand {

	@Override
    public void command(Player player, String[] args, PersonalWorlds plugin) {
		
		if (!PluginUtil.isWorldLoaded(player.getName())) {
			player.sendMessage("You world is not loaded! That means no one is inside the world!");
			return;
		}
		
		if (!Permissions.KICK.canUse(player)) {
			player.sendMessage(plugin.getMessage("noPermissions"));
			return;
		}
		
		if (args.length != 2) {
			player.sendMessage(plugin.getMessage("wrongArguments"));
			return;
		}
		
		final Player inWorld = PluginUtil.matchOnlinePlayer(args[1]);
		
		if (inWorld == null || !inWorld.isOnline()) {
			player.sendMessage(plugin.getMessage("playerNotOnline"));
			return;
		}
		
		if (!PluginUtil.isWorldOwner(player.getName(), inWorld.getWorld().getName())) {
			player.sendMessage(plugin.getMessage("dontOwnWorldPlayerIn").replace("{PLAYER}", inWorld.getName()));
			return;
		}
		
		if (Permissions.OVERRIDE.canUse(inWorld)) {
			player.sendMessage(plugin.getMessage("playerCanOverride"));
			return;
		}
		
		inWorld.sendMessage(plugin.getMessage("kickedFromWorld").replace("{PLAYER}", player.getName()));
		inWorld.teleport(plugin.getServer().getWorlds().get(0).getSpawnLocation());
		
    }
	
}
