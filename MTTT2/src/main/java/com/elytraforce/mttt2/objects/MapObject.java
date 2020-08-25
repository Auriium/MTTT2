package main.java.com.elytraforce.mttt2.objects;

import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;

import main.java.com.elytraforce.mttt2.Main;

public class MapObject {
	
	public final static Location genericSpawn = new Location(Bukkit.getWorld("world"), 0.0, 0.0, 0.0, 0, 0);
	public final static Location genericGun = new Location(Bukkit.getWorld("world"), 0.0, 1.0, 0.0, 0, 0);
	public final static Location genericTester = new Location(Bukkit.getWorld("world"), 0.0, 2.0, 0.0, 0, 0);
	
	private String id;
	private Location spawnLocation;
	private Location testerLocation;
	private ArrayList<Location> gunLocations;
	
	private Main mainClass;

	public MapObject(Main mainClass, String id) {
		this.mainClass = mainClass;
		ArrayList<Location> internalGunLocations = new ArrayList<Location>();
		
		this.id = id;
		
		if (!this.exists()) {
			
			//TODO: redundant check that the world exists, implement when doing SWM shit
			
			
			ConfigurationSection section = mainClass.getMapConfigHandler().getMapSection();

			
			mainClass.getMapConfigHandler().getConfigFetcher();
			MemorySection.createPath(section, this.id + ".spawn");
			MemorySection.createPath(section, this.id + ".tester");
			MemorySection.createPath(section, this.id + ".guns.1");
			
			mainClass.getMapConfigHandler().getConfigFetcher().set("Maps." + this.id + ".spawn", genericSpawn);
			mainClass.getMapConfigHandler().getConfigFetcher().set("Maps." + this.id + ".tester", genericTester);
			mainClass.getMapConfigHandler().getConfigFetcher().set("Maps." + this.id + ".guns.1", genericGun);
			
			mainClass.getMapConfigHandler().save();
		}
		
		//not sure what thhe point of this line here is
		
		this.id = mainClass.getMapConfigHandler().getConfigFetcher()
				.getConfigurationSection("Maps." + id).getName();
		
		
		
		this.spawnLocation = (Location) mainClass.getMapConfigHandler().getConfigFetcher()
			.get("Maps." + this.id + ".spawn");
			
		this.testerLocation = (Location) mainClass.getMapConfigHandler().getConfigFetcher()
			.get("Maps." + this.id + ".tester");
		
		for (String subPath : mainClass.getMapConfigHandler().getConfigFetcher().getConfigurationSection("Maps." + this.id + ".guns").getKeys(false)) {
			Location subGunLocation = (Location) mainClass.getMapConfigHandler().getConfigFetcher().get("Maps." + this.id + ".guns." + subPath);
			internalGunLocations.add(subGunLocation);
		}
		
		this.gunLocations = internalGunLocations;
		mainClass.getMapConfigHandler().addMap(this);
	}
	
	public String getId() {
		return this.id;
	}
	
	public Location getSpawn() {
		return this.spawnLocation;
	}
	
	public Location getTester() {
		return this.testerLocation;
	}
	
	public ArrayList<Location> getGunLocations() {
		return this.gunLocations;
	}
	
	public void setSpawn(Location location) {
		mainClass.getMapConfigHandler().getConfigFetcher().set("Maps." + this.id + ".spawn", location);
		mainClass.getMapConfigHandler().save();
    }
    
    public void setTester(Location location) {
    	mainClass.getMapConfigHandler().getConfigFetcher().set("Maps." + this.id + ".spawn", location);
    	mainClass.getMapConfigHandler().save();
    }
    
    public Integer addGunLocation(Location location) {
    	
    	Integer gunInteger;
    	
    	ArrayList<Integer> gunList = new ArrayList<>();
    	
    	if (this.mainClass.getMapConfigHandler().getConfigFetcher().get("Maps." + this.id + ".guns.1").equals(null)) {
    		gunInteger = 1;
    		
    	} else {
    		for (String subPath : mainClass.getMapConfigHandler().getConfigFetcher().getConfigurationSection("Maps." + this.id + ".guns").getKeys(false)) {
				int numberToAdd = Integer.parseInt(subPath);
				
				gunList.add(numberToAdd);
			}
    		
    		gunInteger = Collections.max(gunList) + 1;
    	}
    	
    	this.mainClass.getMapConfigHandler().getConfigFetcher().set("Maps." + this.id + ".guns." + gunInteger, location);
    	mainClass.getMapConfigHandler().save();
    	return gunInteger;
    	
    }
	
	public boolean exists() {
		return mainClass.getMapConfigHandler().getMapSection().contains(this.id);
	}
	
}
