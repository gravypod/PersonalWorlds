package com.gravypod.PersonalWorlds;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.World.Environment;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.gravypod.PersonalWorlds.Listener.PlayerListener;
import com.gravypod.PersonalWorlds.utils.PluginUtil;

public class PersonalWorlds extends JavaPlugin {

	Logger log = Logger.getLogger("Minecraft");
	
	private List<String> generators = null;
	private List<String> commands = null;
	private int borderSize;
	
	@Override
	public void onEnable() {
		
		File config = new File(this.getDataFolder() + System.getProperty("file.separator") + "config.yml");
		
		if (!config.exists()) {
			this.getConfig().options().copyDefaults(true);
			try {
				this.getConfig().save(config);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		setBorderSize(this.getConfig().getInt("Borders"));
		
		generators = new ArrayList<String>();
		
		commands = ListClasses.getClasseNamesInPackage(this.getFile().getAbsolutePath(), "com.gravypod.PersonalWorlds.commands.");
		
		log.info("Enabling PersonalWorlds. Made by Gravypod");
		
		getCommand(getPluginName()).setExecutor(new CommandHandler(this));
		getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
		PluginUtil.init(this);
		
		File worldsFolder = new File(getPluginName());
		
		if (!worldsFolder.exists()) {
			
			worldsFolder.mkdir();
			
		} else if (!worldsFolder.canRead() || !worldsFolder.canWrite()) {
			
			throw new IllegalStateException("You do not have Read/Write access for the server root folder!");
			
		}
		
		getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				
		        Plugin[] plugins = getServer().getPluginManager().getPlugins();
		        log.warning("Ignore the message: 'Plugin {Plugin} does not contain any generators'");
		        
		        for (Plugin p : plugins) {
		        	// Collect a list of generators
		        	if (p.isEnabled() && p.getDefaultWorldGenerator("world", "") != null) {
		        		generators.add(p.getDescription().getName());
		        	}
		        }
		        
		        for (Environment t : Environment.values()) {
		        	if (!generators.contains(t.name()))
		        		generators.add(t.name());
		        }
		        
			}
		});
		
	}
	
	@Override
	public void onDisable() {
		
		generators = null;
		commands = null;
		
		log.info("Disabling PersonalWorlds. Made by gravypod");
		
	}
	
	/**
	 * Gets the plugin's name from the plugin.yml
	 * 
	 * @return String of the plugin's name
	 */
	public String getPluginName() {
		
		return this.getName();
		
	}
	
	/**
	 * Tests if a string is a world generator we know about.
	 * 
	 * @param name
	 * @return String of a world generators name.
	 * 
	 */
	public String getGenerator(String name) {
		
		for (String genName : generators) {
			
			if (genName.equalsIgnoreCase(name))
				return genName;
			
		}
		
		return null;
		
	}
	
	/**
	 * This returns a joined list of worlds. Separated by ", "
	 * 
	 * @return String of all the generators we know about.
	 */
	public String joinedGenList() {
		String generatorsList = "";
		
		for (String gen : generators) {
			generatorsList += gen.toLowerCase() + ", ";
		}
		
		
		return (generatorsList.length() >= 1) ? generatorsList.substring(0, generatorsList.length() - 2) : "";
		
	}

	public List<String> getCommands() {
		return commands;
	}

	public void setCommands(List<String> commands) {
		this.commands = commands;
	}

	public int getBorderSize() {
		return borderSize;
	}

	public void setBorderSize(int borderSize) {
		this.borderSize = borderSize;
	}

}
