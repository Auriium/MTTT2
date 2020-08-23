package main.java.com.elytraforce.mttt2.objects.arena;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import main.java.com.elytraforce.mttt2.Main;
import main.java.com.elytraforce.mttt2.enums.GamePlayerRoleEnum;
import main.java.com.elytraforce.mttt2.enums.GameStateEnum;
import main.java.com.elytraforce.mttt2.objects.GamePlayer;
import main.java.com.elytraforce.mttt2.objects.Manager;

public class Arena {

	private String id;
	private ArrayList<GamePlayer> arenaPlayers;
	private int requiredPlayers;
	private String prefix;
	//enums
	private GameStateEnum gameState;
	//game states
	private ArenaCountdown arenaCountdown;
	private ArenaPreparationCountdown arenaPreparationCountdown;
	private ArenaGame arenaGame;
	private ArenaEndingCountdown arenaEndingCountdown;
	//locations
	private Location LOBBY_POINT;
	private Location MAP_POINT;
	
	public HashMap<Player, Scoreboard> scoreboardMap;
	
	
	private Main mainClass;
	
	public Arena(String id, Location lobbyLocation, Location mapLocation, Main main) {

		this.id = id;
		this.arenaPlayers = new ArrayList<GamePlayer>();

		// Initialise the arena's game state as waiting - what it will be when
		// the arena is created.

		this.gameState = GameStateEnum.WAITING;
		this.LOBBY_POINT = lobbyLocation;
		this.MAP_POINT = mapLocation;
		
		this.arenaCountdown = new ArenaCountdown(this);
		this.arenaPreparationCountdown = new ArenaPreparationCountdown(this);
		this.arenaGame = new ArenaGame(this);
		this.arenaEndingCountdown = new ArenaEndingCountdown(this);
		this.mainClass = main;

		//TODO: these need to be retrieved from a config
		this.requiredPlayers = 2;
		this.prefix = mainClass.getMessageHandler().getMessage("prefix", false);

		// Add the arena to the arena list in the manager class.
		Manager.getInstance().addArena(this);
	}
	
	//THIS METHOD MUST BE RAN AT THE *END* OF MAP ENDING STATE.
	public void reset() {

		for (GamePlayer player : this.arenaPlayers) {
			player.getPlayer().kickPlayer("DEBUG KICK MESSAGE!");
			//You will have to send them to hub instead of kicking them off.
		}
		this.arenaPlayers.clear();
		this.gameState = GameStateEnum.WAITING;
		
		//Please also run map resetting method here

	}
	
	
	//Getters and setters
	
	public Main getMain() {
		return this.mainClass;
	}
	
	public Arena getArena() {
		return this;
	}
	
	public void setArenaState(GameStateEnum state) {
		this.gameState = state;
	}
	
	public GameStateEnum getArenaState() {
		return this.gameState;
	}
	
	public ArrayList<GamePlayer> getArenaPlayers() {
		return this.arenaPlayers;
	}
	
	public GamePlayer findGamePlayer(Player player) {
		for (GamePlayer gamePlayer : this.arenaPlayers) {
			if (gamePlayer.getPlayer().equals(player)) {
				return gamePlayer;
			}
		}
		return null;
	}
	
	public GamePlayer findGamePlayer(GamePlayer player) {
		for (GamePlayer gamePlayer : this.arenaPlayers) {
			if (gamePlayer.equals(player)) {
				return gamePlayer;
			}
		}
		return null;
	}
	
	public ArrayList<GamePlayer> getLivingArenaPlayers() {
		ArrayList<GamePlayer> returnList = new ArrayList<GamePlayer>();
		for (GamePlayer player : this.arenaPlayers) {
			if (!player.getRole().equals(GamePlayerRoleEnum.SPECTATOR)) {
				returnList.add(player);
			}
		}
		return returnList;
	}
	
	public ArrayList<GamePlayer> getArenaPlayers(GamePlayerRoleEnum role) {
		ArrayList<GamePlayer> returnList = new ArrayList<GamePlayer>();
		for (GamePlayer player : this.arenaPlayers) {
			if (player.getRole().equals(role)) {
				returnList.add(player);
			}
		}
		return returnList;
	}
	
	public int getRequiredPlayers() {
		return this.requiredPlayers;
	}
	
	public ArenaEndingCountdown getArenaEndingCountdown() {
		return this.arenaEndingCountdown;
	}
	
	public ArenaCountdown getArenaCountdown() {
		return this.arenaCountdown;
	}
	
 	public ArenaPreparationCountdown getArenaPreparationCountdown() {
 		return this.arenaPreparationCountdown;
 	}
 	
 	public ArenaGame getArenaGame() {
 		return this.arenaGame;
 	}
 	
 	//Methods for player shit
 	
 	public void addPlayer(Player player) {
 		GamePlayer addedPlayer = new GamePlayer(player, this);
 		
 		sendPlayerToLobby(addedPlayer);
 		this.arenaPlayers.add(addedPlayer);
 		
 		if (!arenaCountdown.isRunning() && arenaPlayers.size() >= requiredPlayers) {

 			arenaCountdown.start(60);

 		}
 	}
 	
 	public void removePlayer(Player player) {
 		for (GamePlayer gamePlayer : this.arenaPlayers) {
 			if (gamePlayer.getPlayer().equals(player)) {
 				this.arenaPlayers.remove(gamePlayer);
 				return;
 			}
 		}
 		//kick player or something
 	}
 	
 	public void addPlayer(GamePlayer gamePlayer) {
 		sendPlayerToLobby(gamePlayer);
 		this.arenaPlayers.add(gamePlayer);
 		
 		if (!arenaCountdown.isRunning() && arenaPlayers.size() >= requiredPlayers) {

 			// an issue arises - if you join during a game it will start the countdown again.
 			// to fix this, either make sure the game isn't running or make sure players cannot join a game
 			// in progress
 			
 			if (this.gameState.equals(GameStateEnum.WAITING)) {
 				//This will only begin the countdown if the game is waiting for players. Otherwise, nah.
 				arenaCountdown.start(60);
 			}
 		}
 	}
 	
 	public GamePlayerRoleEnum checkWinner() {
 		if (this.arenaPlayers.size() < this.requiredPlayers) {
 			return GamePlayerRoleEnum.NONE;
 		}
 		
 		if (this.getArenaPlayers(GamePlayerRoleEnum.TRAITOR).size() == 0) {
 			return GamePlayerRoleEnum.INNOCENT;
 		}
 		
 		if (this.getArenaPlayers(GamePlayerRoleEnum.INNOCENT).size() + this.getArenaPlayers(GamePlayerRoleEnum.DETECTIVE).size() == 0) {
 			return GamePlayerRoleEnum.TRAITOR;
 		}
 		
		return null;
 	}
 	
 	public void removePlayer(GamePlayer gamePlayer) {
 		this.arenaPlayers.remove(gamePlayer);
 		
 		//TODO: CHeck if victory method here.
 		if (!checkWinner().equals(null)) {
 			//instantly run an ArenaEndingCountdown
 			this.arenaEndingCountdown.start(10, checkWinner());
 		}
 		
 	}
 	
 	
 	public void sendArenaToLobby() {
 		for (GamePlayer player : this.arenaPlayers) {
 			this.sendPlayerToLobby(player);
 		}
 	}
 	
 	public void sendArenaToGame() {
 		for (GamePlayer player : this.arenaPlayers) {
 			this.sendPlayerToGame(player);
 		}
 	}
 	
 	public void sendPlayerToLobby(GamePlayer player) {
 			player.getPlayer().getInventory().clear();
 			player.getPlayer().teleport(this.LOBBY_POINT);
 	}
 	
 	public void sendPlayerToGame(GamePlayer player) {
 		player.getPlayer().getInventory().clear();
		player.getPlayer().teleport(this.MAP_POINT);
 	}
 	
 	//Arena actions
 	
 	public void actionSendArenaSound(Sound sound, int volume, int pitch) {
 		for (GamePlayer player : this.arenaPlayers) {
 			player.getPlayer().playSound(player.getPlayer().getLocation(), sound, volume, pitch);
 		}
 	}
 	
 	public void actionSendGameStartTitle() {
 		//TODO: fancy sex animation
 		
 		mainClass.getTitleActionbarHandler().sendTitle(this, "&4Game Start!", "&7Trouble in Traitor Town");
 		
 		new BukkitRunnable() {
	            public void run() {
	            	
	            	for (GamePlayer player : getArena().getArenaPlayers()) {
	         			//Tell them what they are!
	         			
	            		GamePlayerRoleEnum playerRole = player.getRole();
	            		switch (playerRole) {
	            		case INNOCENT:
	            			mainClass.getTitleActionbarHandler().sendTitle(player.getPlayer(), "&a&lINNOCENT", "&7Try to stay alive and kill the &cTraitor!");
	            			break;
	            		
	            		case TRAITOR:
	            			mainClass.getTitleActionbarHandler().sendTitle(player.getPlayer(), "&c&lTRAITOR", "&7Kill all the &aInnocents!");
	            			break;
	            			
	            		case DETECTIVE:
	            			mainClass.getTitleActionbarHandler().sendTitle(player.getPlayer(), "&9&lDETECTIVE", "&7Protect the &aInnocents");
	            			break;
	            			
	            		default:
	            			mainClass.getTitleActionbarHandler().sendTitle(player.getPlayer(), "&7&lNONE", "&cTHIS IS AN ERROR");
	            			break;
	            		}
	            		
	         		}
	            }
	        }.runTaskLater(mainClass, (long)60L);
 		
 		
 		
 	}
 	
 	public void actionAssignRoles() {
 		//i am skid i copy pasted this shuffling shit straight from mttt the original
 		//ngl my code better tho

 		//first, set all players to innocents
 		
 		for (GamePlayer player : this.arenaPlayers) {
 			player.setRole(GamePlayerRoleEnum.INNOCENT);
 		}
 		
 		//this here should shuffle the players up a bit
 		
 		Collections.shuffle(this.arenaPlayers);
		Collections.shuffle(this.arenaPlayers);
		
		int traitorRoles = (int) Math.round((this.arenaPlayers.size() / 4)+0.5);
		
		if (this.arenaPlayers.size() <= 4){
			traitorRoles = 1;
		}
		
		int detectiveRoles = (int)(this.arenaPlayers.size() / 7);
		if (traitorRoles == 0){
			traitorRoles = 1;
		}
		if (detectiveRoles == 1 && this.arenaPlayers.size() < 6){
			detectiveRoles = 0;
		}
 		
 		for (int i = 0; i < traitorRoles; i++) {
 			
 			//This person is a traitor! oh god 
 			// i cant tell you i understand the code, i just write it
 			GamePlayer t = this.arenaPlayers.get(randInt(0 , this.arenaPlayers.size()-1));
 			t.setRole(GamePlayerRoleEnum.TRAITOR);
 			
 		}
 		
 		//do the same thing for detectives
 		//TODO: make this better 
 		for (int i = 0; i < detectiveRoles; i++) {
 			
 			GamePlayer t = this.arenaPlayers.get(randInt(0 , this.arenaPlayers.size()-1));
 			t.setRole(GamePlayerRoleEnum.DETECTIVE);
 			
 		}
 		
 		//now players should be switched up in roles, but dont show them this yet.
 	}
 	
	//Random Utility Methods
	
	public void broadcastMessage(String message) {
		for (int i = 0; i < this.arenaPlayers.size(); i++) {
			this.arenaPlayers.get(i).getPlayer().sendMessage(this.prefix + message);
		}
	}
	
	public static int randInt(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
	
	
}
