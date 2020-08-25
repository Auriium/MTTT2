package main.java.com.elytraforce.mttt2.config;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import main.java.com.elytraforce.mttt2.Main;
import main.java.com.elytraforce.mttt2.objects.Manager;
import main.java.com.elytraforce.mttt2.objects.MapObject;
import main.java.com.elytraforce.mttt2.objects.arena.Arena;

public class MapConfigHandler {
	
	//TODO: make this a singleton and make all references to it use it as such
	
	//private Location LOBBY_POINT;
	
	public final static Location LOBBY_POINT = new Location(Bukkit.getWorld("world"), 0.0, 10.0, 0.0);
	public final static Location genericLocation = new Location(Bukkit.getWorld("world"), 0.0, 10.0, 0.0);
	
	FileConfiguration customConfig = null;
	File customConfigurationFile = null;
	
	ConfigurationSection mapSection = null;
	ConfigurationSection lobbySection = null;
	
	private ArrayList<MapObject> MapList;
	
	Main mainClass;
	
	public MapConfigHandler(Main main){
		//run this here so that if any of the methods are called the maps are read first.
		
		//FUCKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK
		//8 hours spent figuring out why mapObject was null -
		//spoiler alert it wasnt null the fucking map list is null
		this.MapList = new ArrayList<>();
		//FUCKING SHOOT ME
		
		this.mainClass = main;
		this.createCustomConfig();
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
    
    public ArrayList<MapObject> getMaps() {
    	return this.MapList;
    }
    
    public void addMap(MapObject object) {
    	this.MapList.add(object);
    }
    
    public void removeMap(MapObject object) {
    	this.MapList.remove(object);
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
    
    public ConfigurationSection getLobbySection() {
    	
    	if (!this.getConfigFetcher().isConfigurationSection("Lobby")) {
    		mainClass.getMapConfigHandler().getConfigFetcher().createSection("Lobby");
    		mainClass.getMapConfigHandler().getConfigFetcher().set("Lobby.lobby", genericLocation);
    		this.save();
    	}
    	
    	this.lobbySection = this.getConfigFetcher().getConfigurationSection("Lobby");
    	
    	return this.lobbySection;
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
    
    public MapObject getMapFromString(String string) {
    	for (MapObject object : MapList) {
    		if (object.getId().equals(string)) {
    			return object;
    		}
    	}
		return null;
    }
    
    public void setLobbySection(Location location) {
    	mainClass.getMapConfigHandler().getConfigFetcher().set("Lobby.lobby", location);
    	this.save();
    	
    }
    
    
	
	public void readArenas() {
		
		for (MapObject mapObject : MapList) {
			new Arena(mapObject.getId(), MapConfigHandler.LOBBY_POINT, mapObject.getSpawn(), mapObject.getGunLocations());
			mainClass.printDebugLine("[MTTT2] Registered arena " + mapObject.getId());
		}
		
		
		return;
	}
	
	public void readMaps() {
		for (String path : this.getConfigFetcher().getConfigurationSection("Maps").getKeys(false)) {

			new MapObject(mainClass, path);

		}
		return;
	}
	
}
