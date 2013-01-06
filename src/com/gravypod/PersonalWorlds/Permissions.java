package com.gravypod.PersonalWorlds;

import org.bukkit.entity.Player;


public enum Permissions {
	
	TP("personalworlds.tp"),
	OVERRIDE("personalworlds.override"),
	TP_OTHER("personalworlds.tp.other"),
	ADD("personalworlds.add"),
	CREATE("personalworlds.add"),
	DELETE("personalworlds.delete"),
	DELETE_OTHER("personalworlds.delete.other"),
	GENERATOR("personalworlds.generator"),
	HELP("personalworlds.help"),
	HOME("personalworlds.home"),
	KICK("personalworlds.kick"),
	LIST("personalworlds.list");
	
	final String perm;
	
	Permissions(String perm) {
		this.perm = perm;
	}
	
	public boolean canUse(Player player) {
		return player.hasPermission(perm);
	}
	
	public boolean canUse(Player player, String addedPerm) {
		return player.hasPermission(perm + "." + addedPerm);
	}
	
}
