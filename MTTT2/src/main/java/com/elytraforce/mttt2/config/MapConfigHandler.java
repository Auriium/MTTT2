package main.java.com.elytraforce.mttt2.config;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import main.java.com.elytraforce.mttt2.Main;
import main.java.com.elytraforce.mttt2.objects.arena.Arena;

public class MapConfigHandler {
	public final static Location LOBBY_POINT = new Location(Bukkit.getWorld("world"), 0.0, 10.0, 0.0);
	
	FileConfiguration customConfig = null;
	File customConfigurationFile = null;
	
	ConfigurationSection mapSection = null;
	
	Main mainClass;
	
	public MapConfigHandler(Main main){
		this.mainClass = main;
	}
	
	public FileConfiguration getConfigFetcher() {
	    if (!customConfigurationFile.exists()) {
	    	createCustomConfig();
	    }
	    return this.customConfig;
	}

    public void createCustomConfig() {
    	mainClass.printDebugLine("[MTTT2] Initializing maps.yml");
        customConfigurationFile = new File(mainClass.getDataFolder(), "maps.yml");
        if (!customConfigurationFile.exists()) {
            customConfigurationFile.getParentFile().mkdirs();
            mainClass.saveResource("maps.yml", false);
         }

        customConfig= new YamlConfiguration();
        try {
			customConfig.load(customConfigurationFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
        
        

    }
    
    
    //i dont know what a singleton is
    //however this should act like one
    public ConfigurationSection getMapSection() {
    	
    	if (!this.getConfigFetcher().isConfigurationSection("Maps")) {
    		this.mapSection = mainClass.getMapConfigHandler().getConfigFetcher().createSection("Maps");
    	}
    	
    	this.mapSection = this.getConfigFetcher().getConfigurationSection("Maps");
    	
    	return this.mapSection;
    }
    
    public void save() {
    	try {
			this.customConfig.save(this.customConfigurationFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public File getConfig() {
    	return this.customConfigurationFile;
    }
    
    
    //Map related stuff
    
    public static Location genericLocation = new Location(Bukkit.getWorld("world"), 0.0, 10.0, 0.0, 0, 0);
    
    public void createGenericMap(String mapName) {
    	ConfigurationSection section = this.getMapSection();

		this.getConfigFetcher();
		MemorySection.createPath(section, mapName + ".spawn");
		MemorySection.createPath(section, mapName + ".tester");
		MemorySection.createPath(section, mapName + "guns.1");
		
		this.getConfigFetcher().set("Maps." + mapName + ".spawn", genericLocation);
		this.getConfigFetcher().set("Maps." + mapName + ".tester", genericLocation);
		this.getConfigFetcher().set("Maps." + mapName + ".guns.1", genericLocation);
		
		this.save();
    }
    
	
	public void readArenas() {
		
		ArrayList<Location> gunLocations = new ArrayList<Location>();
		
		for (String path : this.getConfigFetcher().getConfigurationSection("Maps").getKeys(false)) {
			
			Location spawnLocation = (Location) this.getConfigFetcher().get("Maps." + path + ".spawn");
			
			for (String subPath : this.getConfigFetcher().getConfigurationSection("Maps." + path).getKeys(false)) {
				Location subGunLocation = (Location) this.getConfigFetcher().get("Maps." + path + ".guns." + subPath);
				gunLocations.add(subGunLocation);
			}
			
			new Arena(path, LOBBY_POINT, spawnLocation, gunLocations);
			mainClass.printDebugLine("[MTTT2] Registered arena " + path);
			
		}
		
		
		return;
	}
	
}
