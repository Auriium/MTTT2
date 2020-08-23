package main.java.com.elytraforce.mttt2.objects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import main.java.com.elytraforce.mttt2.Main;
import main.java.com.elytraforce.mttt2.objects.arena.Arena;

public class Manager {
	private static Manager manager;
	
	//Define here the location of lobby, make this read from a config
	public final static Location LOBBY_POINT = new Location(Bukkit.getWorld("world"), 0.0, 10.0, 0.0);
	public final static Location TEST_MINIGAME_POINT = new Location(Bukkit.getWorld("world"), 0.0, 10.0, 0.0);
	
	//TODO: In the future, do not use this. Instead, read from a config.
	
	private ArrayList<Arena> arenas;
	
	
	public static void setup() {

		 /*
		 * Create 10 new arena objects (you could obviously replace this number
		 * with a number from an configuration file etc.), it is just for the
		 * sake of this tutorial.
		 */

		
		//Now pass main class to the arena through asinine static abuse

		//TODO: use a for loop to loop through config and create one of these for each map
		//Either do that or select a random one and add its details.
		
		//TODO: in the future, "test" replaced with the id of the map read from config as well.
		
		 new Arena("Test", LOBBY_POINT, TEST_MINIGAME_POINT); 
		 
		 
		 // The arena is added to the arenas list in the constructor of the
		 // arena class.

	}
	
	private Manager() {
		this.arenas = new ArrayList<Arena>();
	}

	public static Manager getInstance() {

		if (manager == null) {
			manager = new Manager();
		}

		return manager;
	}
	
	public ArrayList<Arena> getArenas() {
		return this.arenas;
	}
	
	public void addArena(Arena arena) {
		this.arenas.add(arena);
	}
	
	public void removeArena(Arena arena) {
		this.arenas.remove(arena);
	}
}