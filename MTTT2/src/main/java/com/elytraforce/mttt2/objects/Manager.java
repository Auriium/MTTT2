package main.java.com.elytraforce.mttt2.objects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import main.java.com.elytraforce.mttt2.Main;
import main.java.com.elytraforce.mttt2.objects.arena.Arena;

public class Manager {
	private static Manager manager;
	
	//Define here the location of lobby, make this read from a config
	public final static Location LOBBY_POINT = new Location(Bukkit.getWorld("world"), 0.0, 10.0, 0.0);
	public final static Location TEST_MINIGAME_POINT = new Location(Bukkit.getWorld("world"), 0.0, 10.0, 0.0);
	
	private Integer selectedArena;
	
	private ArrayList<Arena> arenas;
	
	public int randInt(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
	
	public boolean isInArena(Player player) {
		try {
			this.findPlayerArena(player);
			return true;
		} catch (NullPointerException e) {
			return false;
		}
	}
	
	public static void setup() {
		//initialize all the new arenas from the config
		try {
			Main.getMain().getMapConfigHandler().readMaps();
			Main.getMain().getMapConfigHandler().readArenas();
		} catch (NullPointerException e) {
			Main.getMain().printDebugLine("[MTTT2] You have not created any maps. Please create a map and reload the plugin!");
			return;
		}
		
		//randomly select one from the list
		
		//randint (0, arenas.size)

	}
	
	public void setRandomArena() {
		this.selectedArena = randInt(1, this.arenas.size()) - 1;
	}
	
	
	//ok so this here gets the int of the selected arena to join
	public Integer getSelectedArena() {
		return this.selectedArena;
	}
	
	public Arena getSelectedArenaAsArena() {
		return this.arenas.get(this.selectedArena);
	}
	
	//singleton shit for single humans ;-;
	
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
	
	//find a player in an arena
	
	public Arena findPlayerArena(Player player) {
		for (Arena arena : this.arenas) {
			if (arena.containsPlayer(player)) {
				return arena;
			}
		}
		return null;
	}
	
	//adding and removing
	
	public void addArena(Arena arena) {
		this.arenas.add(arena);
	}
	
	public void removeArena(Arena arena) {
		this.arenas.remove(arena);
	}
	
	public void addPlayer(Player player) {
		this.arenas.get(selectedArena).addPlayer(player);
	}
	
	//not really a point to using this i dont think
	public void removePlayer(Player player) {
		this.arenas.get(selectedArena).removePlayer(player);
	}
	
	//TODO: not sure if we are going to use the reset methods for tasks or just create a new arena in the first place.
	//public void arenaEnd(Arena arena) {
	//	this.removeArena(arena);
	//	
	//	//create new arena from old arena data
	//	new Arena(arena.getID(), LOBBY_POINT, arena.getMapLocation(), null);
	//}
	
	
}