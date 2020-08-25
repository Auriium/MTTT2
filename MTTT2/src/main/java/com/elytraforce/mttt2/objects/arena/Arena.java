package main.java.com.elytraforce.mttt2.objects.arena;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
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
	private int maxPlayers;
	private String prefix;
	//enums
	private GameStateEnum gameState;
	//game states
	private ArenaCountdown arenaCountdown;
	private ArenaPreparationCountdown arenaPreparationCountdown;
	private ArenaGame arenaGame;
	private ArenaEndingCountdown arenaEndingCountdown;
	//locations
	private ArrayList<Location> GUN_POINTS;
	private Location LOBBY_POINT;
	private Location MAP_POINT;
	
	public HashMap<Player, Scoreboard> scoreboardMap;
	
	
	private Main mainClass;
	
	public Arena(String id, Location lobbyLocation, Location mapLocation, ArrayList<Location> gunLocations) {

		this.id = id;
		this.arenaPlayers = new ArrayList<GamePlayer>();

		// Initialise the arena's game state as waiting - what it will be when
		// the arena is created.

		this.gameState = GameStateEnum.WAITING;
		this.LOBBY_POINT = lobbyLocation;
		this.MAP_POINT = mapLocation;
		this.GUN_POINTS = gunLocations;
		
		this.arenaCountdown = new ArenaCountdown(this);
		this.arenaPreparationCountdown = new ArenaPreparationCountdown(this);
		this.arenaGame = new ArenaGame(this);
		this.arenaEndingCountdown = new ArenaEndingCountdown(this);
		this.mainClass = Main.getMain();

		//TODO: these need to be retrieved from a config
		this.requiredPlayers = 2;
		this.maxPlayers = 16;
		
		this.prefix = mainClass.getMessageHandler().getMessage("prefix", false);

		// Add the arena to the arena list in the manager class.
		Manager.getInstance().addArena(this);
	}
	
	//THIS METHOD MUST BE RAN AT THE *END* OF MAP ENDING STATE.
	public void reset() {

		for (GamePlayer player : arenaPlayers) {
			player.getPlayer().kickPlayer("Debug");
		}
		
		this.arenaPlayers.clear();
		
		this.gameState = GameStateEnum.WAITING;
		//Please also run map resetting method here

	}
	
	
	//Getters and setters
	
	public String getID() {
		return this.id;
	}
	
	public Location getMapLocation() {
		return this.MAP_POINT;
	}
	
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
	
	public boolean containsPlayer(Player player) {
		for (GamePlayer gamePlayer : this.arenaPlayers) {
			if (gamePlayer.getPlayer().equals(player)) {
				return true;
			}
		}
		return false;
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
 	
 	public void resetArenaEndingCountdown() {
		this.arenaEndingCountdown = new ArenaEndingCountdown(this);
	}
	
	public void resetArenaCountdown() {
		this.arenaCountdown = new ArenaCountdown(this);
	}
	
	public void resetArenaPreperationCountdown() {
		this.arenaPreparationCountdown = new ArenaPreparationCountdown(this);
	}
	
	public void resetArenaGame() {
		this.arenaGame = new ArenaGame(this);
	}
 	
 	//Methods for player shit
 	
 	public void addPlayer(Player player) {
 		GamePlayer addedPlayer = new GamePlayer(player, this);
 		
 		addedPlayer.cleanupPlayer(GameMode.SURVIVAL);
 		//IF YOU ARE GOING TO ADD LOBBY ITEMS RUN IT AFTER THIS METHOD.
 		sendPlayerToLobby(addedPlayer);
 		this.addPlayer(addedPlayer);
 		
 	}
 	
 	public void removePlayer(Player player) {
 		for (GamePlayer gamePlayer : this.arenaPlayers) {
 			if (gamePlayer.getPlayer().equals(player)) {
 				this.removePlayer(gamePlayer);
 				return;
 			}
 		}
 		//kick player or something
 	}
 	
 	public void addPlayer(GamePlayer gamePlayer) {
 		sendPlayerToLobby(gamePlayer);
 		this.arenaPlayers.add(gamePlayer);
 		this.arenaPlayers.add(gamePlayer);
 		Bukkit.broadcastMessage(this.arenaPlayers.size() + "");
 		
 		mainClass.getTitleActionbarHandler().sendTitle(gamePlayer.getPlayer(), "&4&lJoined Game", "&7" + this.getID());
 		mainClass.titleActionbarHandler
 		.sendMessageBroadcast(this, "&7%player%&e joined the game. (&7%players%&e/&7%maxplayers%&e)"
 				.replaceAll("%player%", gamePlayer.getPlayer().getDisplayName()).replaceAll("%players%", this.arenaPlayers.size() + "")
					.replaceAll("%maxplayers%", this.maxPlayers + ""));
 		
 		
 		if (!arenaCountdown.isRunning() && arenaPlayers.size() >= requiredPlayers) {

 			// an issue arises - if you join during a game it will start the countdown again.
 			// to fix this, either make sure the game isn't running or make sure players cannot join a game
 			// in progress
 			
 			if (this.gameState.equals(GameStateEnum.WAITING)) {
 				//This will only begin the countdown if the game is waiting for players. Otherwise, nah.
 				arenaCountdown.start(30);
 			}
 		}
 	}
 	
 	//WHEN KILLING SOMEONE< PUT THEM IN SPECTATOR FIRST THEN FUCKING U SE THIS
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
 		//for some asinine reason it makes me do this an im too lazy to figure out
 		this.arenaPlayers.remove(gamePlayer);
 		this.arenaPlayers.remove(gamePlayer);
 		//not working for some FUCKING REASON
 		Bukkit.broadcastMessage(this.arenaPlayers.size() + "");
 		
 		mainClass.titleActionbarHandler
 		.sendMessageBroadcast(this, "&7%player%&e left the game. (&7%players%&e/&7%maxplayers%&e)"
 				.replaceAll("%player%", gamePlayer.getPlayer().getDisplayName()).replaceAll("%players%", this.arenaPlayers.size() - 1 + "")
					.replaceAll("%maxplayers%", this.maxPlayers + ""));
 		
 		checkCancel();
 		
 	}
 	
 	public void checkCancel() {
 		if (this.getArenaState().equals(GameStateEnum.MATCH)) {
 			if (!checkWinner().equals(null)) {
 	 			this.arenaGame.cancel();
 	 			this.resetArenaGame();
 	 			this.arenaEndingCountdown.start(10, checkWinner());
 	 		}
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
 	
 	public void actionGenerateGuns() {
 		
 	}
 	
 	public void actionSendArenaSound(Sound sound, int volume, int pitch) {
 		for (GamePlayer player : this.arenaPlayers) {
 			player.getPlayer().playSound(player.getPlayer().getLocation(), sound, volume, pitch);
 		}
 	}
 	
 	public void actionSendGameStartTitle() {
 		//TODO: fancy sex animation
 		
 		mainClass.getTitleActionbarHandler().sendTitle(this, "&4&lGame Start!", "&7Trouble in Traitor Town");
 		mainClass.getSoundHandler().playSound(this, "entity.experience_orb.pickup", 1, 1);
 		
 		new BukkitRunnable() {
	            public void run() {
	            	
	            	for (GamePlayer player : getArena().getArenaPlayers()) {
	         			//Tell them what they are!
	         			
	            		switch (player.getRole()) {
	            		case TRAITOR:
	            			mainClass.getTitleActionbarHandler().sendTitle(player.getPlayer(), "&c&lTRAITOR", "&7Kill all the &aInnocents!");
	            			mainClass.getSoundHandler().playSound(player, "entity.wolf.howl", 1, 1);
	            			mainClass.getTitleActionbarHandler().sendActionBar(player.getPlayer(), "&cPress shift twice to open the Traitor Shop!");
	            			player.addPointFancy(3);
	            			break;
	            		case DETECTIVE:
	            			mainClass.getTitleActionbarHandler().sendTitle(player.getPlayer(), "&9&lDETECTIVE", "&7Protect the &aInnocents");
	            			mainClass.getSoundHandler().playSound(player, "entity.iron_golem.repair", 1, 1);
	            			mainClass.getTitleActionbarHandler().sendActionBar(player.getPlayer(), "&cPress shift twice to open the Detective Shop!");
	            			player.addPointFancy(3);
	            			break;
	            		case INNOCENT:
	            			mainClass.getTitleActionbarHandler().sendTitle(player.getPlayer(), "&a&lINNOCENT", "&7Try to stay alive and kill the &cTraitor!");
	            			mainClass.getSoundHandler().playSound(player, "entity.experience_orb.pickup", 1, 1);
	            			break;
	            		case SPECTATOR:
	            			break;
	            		case NONE:
	            			break;
	            		default:
	            			mainClass.getTitleActionbarHandler().sendTitle(player.getPlayer(), "&7&lNONE", "&cTHIS IS AN ERROR");
	            			break;
	            		}
	            		
	         		}
	            }
	        }.runTaskLater(mainClass, (long)30L);
 		
 		
 		
 	}
 	
 	public void actionCountRDM() {
 		new BukkitRunnable() {
            public void run() {
            	
            	for (GamePlayer player : getArena().getArenaPlayers()) {
         			//count rdm
            		//in the future, karma will be adjusted per rdm
            		mainClass.getTitleActionbarHandler().sendTitle(
            				player.getPlayer(), "&9&l" + player.getPlayer().getDisplayName(), "&7You random killed &c" + player.getRandomKills() + " &7players!");
        			mainClass.getSoundHandler().playSound(player, "entity.iron_golem.repair", 1, 1);
         		}
            }
        }.runTaskLater(mainClass, (long)30L);
        
        new BukkitRunnable() {
            public void run() {
            	
            	for (GamePlayer player : getArena().getArenaPlayers()) {
         			//now count deaths and adjust deaths
            		mainClass.getTitleActionbarHandler().sendTitle(
            				player.getPlayer(), "&9&l" + player.getPlayer().getDisplayName(), "&7And eliminated &c" + player.getRandomKills() + " &7in total!");
        			mainClass.getSoundHandler().playSound(player, "entity.iron_golem.repair", 1, 1);
         		}
            }
        }.runTaskLater(mainClass, (long)60L);
        
        new BukkitRunnable() {
            public void run() {
            	
            	for (GamePlayer player : getArena().getArenaPlayers()) {
         			//count rdm
            		//in the future, karma will be adjusted per rdm
            		//TODO: make this configurable
            		Integer reward = (player.getKills() - player.getRandomKills()) * 50;
            		mainClass.getTitleActionbarHandler().sendTitle(
            				player.getPlayer(), "&9&lRewards", "&7You earned &c" + reward + "$ &7from this match!");
        			mainClass.getSoundHandler().playSound(player, "entity.iron_golem.repair", 1, 1);
         		}
            }
        }.runTaskLater(mainClass, (long)90L);
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
	
	//run this on death
	public void cleanPlayer() {
		
	}
	
	public void setXPBar(GamePlayer player, int minXP, int maxXP) {
		player.getPlayer().setLevel(minXP);
		double formattedValue = (minXP / maxXP);
		
		player.getPlayer().setExp((float) formattedValue);
	}
	
	public void setXPBar(int minXP, int maxXP) {
		
		for (GamePlayer player : this.arenaPlayers) {
			player.getPlayer().setLevel(minXP);
		}
		
	}
	
	
}
