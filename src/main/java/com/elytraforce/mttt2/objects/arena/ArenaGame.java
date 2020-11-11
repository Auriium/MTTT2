package com.elytraforce.mttt2.objects.arena;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.scheduler.BukkitRunnable;

import com.elytraforce.mttt2.Main;
import com.elytraforce.mttt2.enums.GamePlayerRoleEnum;
import com.elytraforce.mttt2.enums.GameStateEnum;
import com.elytraforce.mttt2.objects.GamePlayer;

public class ArenaGame extends BukkitRunnable{
	private int time;
	private final Arena arena;
	private int initialTime;
	
	//store these values so you dont always have to run the fucking for loop
	private ArrayList<GamePlayer> innocentGamePlayers;
	private ArrayList<GamePlayer> traitorGamePlayers;
	private ArrayList<GamePlayer> detectiveGamePlayers;
	//may cause issues as these values change, so we may have to integrate this with the for loop.

	public ArenaGame(Arena arena) {
		this.arena = arena;
		this.time = 0;
		this.initialTime = 0;
	}
	
	public void start(int time)  {
		
		
		arena.setArenaState(GameStateEnum.MATCH);
		this.time = time;
		this.initialTime = time;
		this.runTaskTimer(Main.getMain(), 0L, 20L);
		
		
		//first clean up anyone who is dead from retarddation
		
		for (GamePlayer player : arena.getArenaPlayers()) {
			if (player.getRole().equals(GamePlayerRoleEnum.SPECTATOR)) {
				player.setRole(GamePlayerRoleEnum.NONE);
				player.cleanupPlayer(GameMode.SURVIVAL);
				arena.getMain().getTitleActionbarHandler().sendActionBar(player.getPlayer(), "&cRespawned! &7(No penalty)");
			}
		}
		
		//Display roles to players here.
		arena.actionAssignRoles();
		for (GamePlayer player : arena.getArenaPlayers()) {
			arena.getMain().getTitleActionbarHandler().sendMessageBroadcast("Player " + player.getPlayer().getDisplayName()
					+ " is a " + player.getRole().toString());
			
		}
		arena.actionSendGameStartTitle();
		
		this.innocentGamePlayers = arena.getArenaPlayers(GamePlayerRoleEnum.INNOCENT);
		this.traitorGamePlayers = arena.getArenaPlayers(GamePlayerRoleEnum.TRAITOR);
		this.detectiveGamePlayers = arena.getArenaPlayers(GamePlayerRoleEnum.DETECTIVE);

	}
	
	public boolean isRunning() {
		return arena.getArenaState() == GameStateEnum.MATCH;
	}
	
	public int getCountdownTime() {
		return this.time;
	}
	
	@Override
	public void run() {
		
		//TODO: EVERY SECOND SEND A TRAITOR GLOW PACKET PER TRAITOR FOR EVERY TRAITOR.
		//for player in traitorGamePlayers send packet
		
		if (arena.getArenaPlayers().size() < arena.getRequiredPlayers()) {
			cancel();
			arena.resetArenaGame();
			
			//game is over because everyone left
			arena.getArenaEndingCountdown().start(10, GamePlayerRoleEnum.NONE);
			return; 
		}

		// If time is 0 the innocents by DEFAULT WIN! The only time traitors should win is using
		//the playerkillplayerevent. A innocent win is also a detective win.
		
		if (time == 0) {
			cancel();
			arena.resetArenaGame();
			
			arena.getArenaEndingCountdown().start(10, GamePlayerRoleEnum.INNOCENT);
		
			return;
		}
		
		if (time % 60 == 0) {
			arena.getMain().getSoundHandler().playSound(arena, "block.note_block.pling", 1, 1);
				if (time != 60) {
					arena.broadcastMessage(ChatColor.AQUA + "Game ends in " + time / 60 + " minutes.");
				} else {
					arena.broadcastMessage(ChatColor.AQUA + "Game ends in " + time / 60 + " minute.");
				}
		}
		
		if ((time % 15 == 0 && time < 60) || time <= 5) {
			arena.getMain().getSoundHandler().playSound(arena, "block.note_block.pling", 1, 1);
				if (time != 1) {
					arena.broadcastMessage(ChatColor.AQUA + "Game ends in " + time + " seconds.");
				} else {
					arena.broadcastMessage(ChatColor.AQUA + "Game ends in " + time + " second.");
				}
		}
		arena.setXPBar(time, initialTime);


		//THIS SHOULD NEVER HAPPEN. THE ONLY TIME AN ARENA COUNTDOWN SHOULD START IS WHEN THERE ARE ENOUGH PLAYERS.
		
		
	time--;
	}
	
	
}
