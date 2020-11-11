package com.elytraforce.mttt2.config;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import com.elytraforce.mttt2.Main;
import com.elytraforce.mttt2.objects.Manager;
import com.elytraforce.mttt2.objects.MapObject;
import com.elytraforce.mttt2.objects.arena.Arena;

public class GunConfigHandler {
	
	FileConfiguration customConfig = null;
	File customConfigurationFile = null;
	
	ConfigurationSection mapSection = null;
	
	public static ArrayList<MapObject> MapList;
	
	Main mainClass;
	
	public GunConfigHandler(Main main){
		//run this here so that if any of the methods are called the maps are read first.
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
    	mainClass.printDebugLine("[MTTT2] Initializing guns.yml");
        customConfigurationFile = new File(mainClass.getDataFolder(), "guns.yml");
        if (!customConfigurationFile.exists()) {
            customConfigurationFile.getParentFile().mkdirs();
            mainClass.saveResource("guns.yml", false);
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
    	
    	if (!this.getConfigFetcher().isConfigurationSection("Guns")) {
    		this.mapSection = mainClass.getMapConfigHandler().getConfigFetcher().createSection("Guns");
    	}
    	
    	this.mapSection = this.getConfigFetcher().getConfigurationSection("Guns");
    	
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
    
    
    //Please have a readGunObjectFromConfig thing as well as a loadGunObjects system here, similar
    //to how map objects work.
	
}
