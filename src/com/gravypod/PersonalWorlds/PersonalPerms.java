/**
 * 
 */
package com.gravypod.PersonalWorlds;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;

import com.gravypod.PersonalWorlds.utils.PluginUtil;

/**
 * 
 * All of this code is taken from towny with permissions of ElgarL
 * 
 * @author ElgarL
 * 
 */
public class PersonalPerms {
	
	protected final static LinkedHashMap<String, Permission> registeredPermissions = new LinkedHashMap<String, Permission>();
	
	protected final static HashMap<String, PermissionAttachment> attachments = new HashMap<String, PermissionAttachment>();
	
	private static PersonalWorlds plugin;
	
	public static void initialize(final PersonalWorlds _plugin) {
	
		PersonalPerms.plugin = _plugin;
	}
	
	private static Field permissions;
	
	static {
		try {
			PersonalPerms.permissions = PermissionAttachment.class.getDeclaredField("permissions");
			PersonalPerms.permissions.setAccessible(true);
		} catch (final SecurityException e) {
			e.printStackTrace();
		} catch (final NoSuchFieldException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Pushes respective permissions to superperms.
	 * 
	 * @param player
	 * 
	 */
	public static void assignPermissions(final Player player) {
	
		PermissionAttachment playersAttachment = null;
		
		/*
		 * Find the current attachment or create a new one (if the player is
		 * online)
		 */
		
		if (player == null || !player.isOnline()) {
			PersonalPerms.attachments.remove(player.getName());
			return;
		}
		
		World World = null;
		
		World = player.getWorld();
		
		if (PersonalPerms.attachments.containsKey(player.getName())) {
			
			playersAttachment = PersonalPerms.attachments.get(player.getName());
			
		} else {
			
			playersAttachment = Bukkit.getPlayer(player.getName()).addAttachment(PersonalPerms.plugin);
			
		}
		
		try {
			
			@SuppressWarnings("unchecked")
			final Map<String, Boolean> orig = (Map<String, Boolean>) PersonalPerms.permissions.get(playersAttachment);
			/*
			 * Clear the map (faster than removing the attachment and
			 * recalculating)
			 */
			orig.clear();
			
			orig.putAll(PersonalPerms.getPlayerPermissions(player, World.getName()));
			
			/*
			 * Tell bukkit to update it's permissions
			 */
			playersAttachment.getPermissible().recalculatePermissions();
			
		} catch (final IllegalArgumentException e) {
			
			e.printStackTrace();
			
		} catch (final IllegalAccessException e) {
			
			e.printStackTrace();
			
		}
		
		/*
		 * Store the attachment for future reference
		 */
		PersonalPerms.attachments.put(player.getName(), playersAttachment);
		
	}
	
	/**
	 * Remove attached permissions.
	 * 
	 * @param name
	 * 
	 */
	public static void removeAttachment(final String name) {
	
		if (PersonalPerms.attachments.containsKey(name)) {
			PersonalPerms.attachments.remove(name);
		}
		
	}
	
	/**
	 * Gets a sorted list of permissions ready to push to superperms.
	 * 
	 * @param player
	 * @param world
	 * @return LinkedHashMap of a permissions and if to allow/deny
	 * 
	 */
	public static LinkedHashMap<String, Boolean> getPlayerPermissions(final Player player, final String world) {
	
		final Set<String> permList = new HashSet<String>();
		
		if (PluginUtil.isPlayerWorld(world)) {
			
			if (PluginUtil.isWorldOwner(player.getName(), world)) {
				
				permList.addAll(PersonalPerms.plugin.getOwnerPerms());
				
			}
			
			permList.addAll(PersonalPerms.plugin.getGuestPerms());
			
		}
		
		final List<String> playerPermArray = PersonalPerms.sort(new ArrayList<String>(permList));
		final LinkedHashMap<String, Boolean> newPerms = new LinkedHashMap<String, Boolean>();
		
		Boolean value = false;
		
		for (final String permission : playerPermArray) {
			value = !permission.startsWith("-");
			newPerms.put(value ? permission : permission.substring(1), value);
		}
		
		return newPerms;
		
	}
	
	/*
	 * Permission utility functions taken from GroupManager (which I wrote
	 * anyway).
	 */
	
	/**
	 * Update the list of permissions registered with bukkit
	 */
	public static void collectPermissions() {
	
		PersonalPerms.registeredPermissions.clear();
		
		for (final Permission perm : Bukkit.getPluginManager().getPermissions()) {
			PersonalPerms.registeredPermissions.put(perm.getName().toLowerCase(), perm);
		}
		
	}
	
	/**
	 * Sort a permission node list by parent/child
	 * 
	 * @param permList
	 * @return List sorted for priority
	 */
	private static List<String> sort(final List<String> permList) {
	
		final List<String> result = new ArrayList<String>();
		
		for (final String key : permList) {
			final String a = key.charAt(0) == '-' ? key.substring(1) : key;
			final Map<String, Boolean> allchildren = PersonalPerms.getAllChildren(a, new HashSet<String>());
			if (allchildren != null) {
				
				final ListIterator<String> itr = result.listIterator();
				
				while(itr.hasNext()) {
					final String node = itr.next();
					final String b = node.charAt(0) == '-' ? node.substring(1) : node;
					
					// Insert the parent node before the child
					if (allchildren.containsKey(b)) {
						itr.set(key);
						itr.add(node);
						break;
					}
				}
			}
			if (!result.contains(key)) {
				result.add(key);
			}
		}
		
		return result;
	}
	
	/**
	 * Fetch all permissions which are registered with superperms. {can include
	 * child nodes)
	 * 
	 * @param includeChildren
	 * @return List of all permission nodes
	 */
	public List<String> getAllRegisteredPermissions(final boolean includeChildren) {
	
		final List<String> perms = new ArrayList<String>();
		
		for (final String key : PersonalPerms.registeredPermissions.keySet()) {
			if (!perms.contains(key)) {
				perms.add(key);
				
				if (includeChildren) {
					final Map<String, Boolean> children = PersonalPerms.getAllChildren(key, new HashSet<String>());
					if (children != null) {
						for (final String node : children.keySet()) {
							if (!perms.contains(node)) {
								perms.add(node);
							}
						}
					}
				}
			}
			
		}
		return perms;
	}
	
	/**
	 * Returns a map of ALL child permissions registered with bukkit null is
	 * empty
	 * 
	 * @param node
	 * @param playerPermArray
	 *            current list of perms to check against for negations
	 * @return Map of child permissions
	 */
	public static Map<String, Boolean> getAllChildren(final String node, final Set<String> playerPermArray) {
	
		final LinkedList<String> stack = new LinkedList<String>();
		final Map<String, Boolean> alreadyVisited = new HashMap<String, Boolean>();
		stack.push(node);
		alreadyVisited.put(node, true);
		
		while(!stack.isEmpty()) {
			final String now = stack.pop();
			
			final Map<String, Boolean> children = PersonalPerms.getChildren(now);
			
			if (children != null && !playerPermArray.contains("-" + now)) {
				for (final String childName : children.keySet()) {
					if (!alreadyVisited.containsKey(childName)) {
						stack.push(childName);
						alreadyVisited.put(childName, children.get(childName));
					}
				}
			}
		}
		alreadyVisited.remove(node);
		if (!alreadyVisited.isEmpty()) {
			return alreadyVisited;
		}
		
		return null;
	}
	
	/**
	 * Returns a map of the child permissions (1 node deep) as registered with
	 * Bukkit. null is empty
	 * 
	 * @param node
	 * @return Map of child permissions
	 */
	public static Map<String, Boolean> getChildren(final String node) {
	
		final Permission perm = PersonalPerms.registeredPermissions.get(node.toLowerCase());
		if (perm == null) {
			return null;
		}
		
		return perm.getChildren();
		
	}
	
}
