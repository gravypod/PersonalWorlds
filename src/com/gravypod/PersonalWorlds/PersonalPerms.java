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
 * @author ElgarL
 * 
 */
public class PersonalPerms {

	protected static LinkedHashMap<String, Permission> registeredPermissions = new LinkedHashMap<String, Permission>();
	protected static HashMap<String, PermissionAttachment> attachments = new HashMap<String, PermissionAttachment>();
	private static PersonalWorlds plugin;
	
	public static void initialize(PersonalWorlds _plugin) {
		plugin = _plugin;
	}
	
	private static Field permissions;

	static {
		try {
			permissions = PermissionAttachment.class.getDeclaredField("permissions");
			permissions.setAccessible(true);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Pushes respective permissions to superperms.
	 * 
	 * @param player
	 * 
	 */
	public static void assignPermissions(Player player) {
		
		PermissionAttachment playersAttachment = null;
		
		/*
		 * Find the current attachment
		 * or create a new one (if the player is online)
		 */

		if ((player == null) || !player.isOnline()) {
			attachments.remove(player.getName());
			return;
		}
		
		World World = null;

		World = player.getWorld();
		
		if (attachments.containsKey(player.getName())) {
			
			playersAttachment = attachments.get(player.getName());
			
		} else {
			
			playersAttachment = Bukkit.getPlayer(player.getName()).addAttachment(plugin);
			
		}
		
		try {
			
			@SuppressWarnings("unchecked")
			Map<String, Boolean> orig = (Map<String, Boolean>) permissions.get(playersAttachment);
			/*
			 *  Clear the map (faster than removing the attachment and recalculating)
			 */
			orig.clear();
			
			orig.putAll(PersonalPerms.getPlayerPermissions(player, World.getName()));
				
			/*
			 * Tell bukkit to update it's permissions
			 */
			playersAttachment.getPermissible().recalculatePermissions();
			
		} catch (IllegalArgumentException e) {
			
			e.printStackTrace();
			
		} catch (IllegalAccessException e) {
			
			e.printStackTrace();
			
		}
		
		/*
		 * Store the attachment for future reference
		 */
		attachments.put(player.getName(), playersAttachment);
		
	}
	
	/**
	 * Remove attached permissions.
	 * 
	 * @param name
	 * 
	 */
	public static void removeAttachment(String name) {
		
		if (attachments.containsKey(name))
			attachments.remove(name);
		
	}
	
	/**
	 * Gets a sorted list of permissions ready to push to superperms.
	 * 
	 * @param player
	 * @param world
	 * @return LinkedHashMap of a permissions and if to allow/deny
	 * 
	 */
	public static LinkedHashMap<String, Boolean> getPlayerPermissions(Player player, String world) {
		
		Set<String> permList = new HashSet<String>();
		
		if (PluginUtil.isPlayerWorld(world)) {
			
			if (PluginUtil.isWorldOwner(player.getName(), world)) {
					
				permList.addAll(plugin.getOwnerPerms());
					
			} 
				
			permList.addAll(plugin.getGuestPerms());
						
		}
		
		List<String> playerPermArray = sort(new ArrayList<String>(permList));
		LinkedHashMap<String, Boolean> newPerms = new LinkedHashMap<String, Boolean>();

		Boolean value = false;
		for (String permission : playerPermArray) {
			value = (!permission.startsWith("-"));
			newPerms.put((value ? permission : permission.substring(1)), value);
		}
		
		
		
		return newPerms;
		
	}

	/*
	 * Permission utility functions taken from GroupManager (which I wrote anyway).
	 */
	
	/**
	 * Update the list of permissions registered with bukkit
	 */
	public static void collectPermissions() {

		registeredPermissions.clear();

		for (Permission perm : Bukkit.getPluginManager().getPermissions()) {
			registeredPermissions.put(perm.getName().toLowerCase(), perm);
		}

	}
	
	/**
	 * Sort a permission node list by parent/child
	 * 
	 * @param permList
	 * @return List sorted for priority
	 */
	private static List<String> sort(List<String> permList) {
		
		List<String> result = new ArrayList<String>();

		for (String key : permList) {
			String a = key.charAt(0) == '-' ? key.substring(1) : key;
			Map<String, Boolean> allchildren = getAllChildren(a, new HashSet<String>());
			if (allchildren != null) {

				ListIterator<String> itr = result.listIterator();

				while (itr.hasNext()) {
					String node = (String) itr.next();
					String b = node.charAt(0) == '-' ? node.substring(1) : node;

					// Insert the parent node before the child
					if (allchildren.containsKey(b)) {
						itr.set(key);
						itr.add(node);
						break;
					}
				}
			}
			if (!result.contains(key))
				result.add(key);
		}

		return result;
	}

	/**
	 * Fetch all permissions which are registered with superperms.
	 * {can include child nodes)
	 * 
	 * @param includeChildren
	 * @return List of all permission nodes
	 */
	public List<String> getAllRegisteredPermissions(boolean includeChildren) {

		List<String> perms = new ArrayList<String>();

		for (String key : registeredPermissions.keySet()) {
			if (!perms.contains(key)) {
				perms.add(key);

				if (includeChildren) {
					Map<String, Boolean> children = getAllChildren(key, new HashSet<String>());
					if (children != null) {
						for (String node : children.keySet())
							if (!perms.contains(node))
								perms.add(node);
					}
				}
			}

		}
		return perms;
	}

	/**
	 * Returns a map of ALL child permissions registered with bukkit
	 * null is empty
	 * 
	 * @param node
	 * @param playerPermArray current list of perms to check against for
	 *            negations
	 * @return Map of child permissions
	 */
	public static Map<String, Boolean> getAllChildren(String node, Set<String> playerPermArray) {

		LinkedList<String> stack = new LinkedList<String>();
		Map<String, Boolean> alreadyVisited = new HashMap<String, Boolean>();
		stack.push(node);
		alreadyVisited.put(node, true);

		while (!stack.isEmpty()) {
			String now = stack.pop();

			Map<String, Boolean> children = getChildren(now);

			if ((children != null) && (!playerPermArray.contains("-" + now))) {
				for (String childName : children.keySet()) {
					if (!alreadyVisited.containsKey(childName)) {
						stack.push(childName);
						alreadyVisited.put(childName, children.get(childName));
					}
				}
			}
		}
		alreadyVisited.remove(node);
		if (!alreadyVisited.isEmpty())
			return alreadyVisited;

		return null;
	}
	
	/**
	 * Returns a map of the child permissions (1 node deep) as registered with
	 * Bukkit.
	 * null is empty
	 * 
	 * @param node
	 * @return Map of child permissions
	 */
	public static Map<String, Boolean> getChildren(String node) {

		Permission perm = registeredPermissions.get(node.toLowerCase());
		if (perm == null)
			return null;

		return perm.getChildren();

	}

}