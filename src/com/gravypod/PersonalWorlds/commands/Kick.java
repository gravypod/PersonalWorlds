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
			player.sendMessage("You do not have permission.");
			return;
		}
		
		if (args.length != 2) {
			player.sendMessage("You used the wrong number of arguments!");
			player.sendMessage("Usage: /pw kick (player name)");
			return;
		}
		
		final Player inWorld = PluginUtil.matchOnlinePlayer(args[1]);
		
		if (inWorld == null || !inWorld.isOnline()) {
			player.sendMessage("That player is not online.");
			return;
		}
		
		if (!PluginUtil.isWorldOwner(player.getName(), inWorld.getWorld().getName())) {
			player.sendMessage("You do not own the world that " + player.getName() + " is in!");
			return;
		}
		
		if (Permissions.OVERRIDE.canUse(inWorld)) {
			player.sendMessage("That player has the PersonalWorld override permission, you cannot kick him from your world.");
			return;
		}
		
		inWorld.sendMessage(player.getName() + " has kicked you from their world.");
		inWorld.teleport(plugin.getServer().getWorlds().get(0).getSpawnLocation());
		
    }
	
}
