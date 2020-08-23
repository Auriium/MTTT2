package main.java.com.elytraforce.mttt2.objects.arena;

import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.com.elytraforce.mttt2.Main;
import main.java.com.elytraforce.mttt2.enums.GameStateEnum;

public class ArenaGame extends BukkitRunnable{
	private int time;
	private final Arena arena;

	public ArenaGame(Arena arena) {
		this.arena = arena;
		this.time = 0;
	}
	
	public void start(int time)  {

		arena.setArenaState(GameStateEnum.MATCH);
		this.time = time;
		this.runTaskTimer(Main.getMain(), 0L, 20L);
		
		//method here should handle players being teleported to the arena.
		this.arena.sendArenaToGame();
		
		//Maybe roles should be assigned pre game actually beginning
		
		//guns also need to be spawned into the map here

	}
	
	public boolean isRunning() {
		return arena.getArenaState() == GameStateEnum.MATCH;
	}
	
	public int getCountdownTime() {
		return this.time;
	}
	
	@Override
	public void run() {
		
		if (arena.getArenaPlayers().size() < arena.getRequiredPlayers()) {
			cancel();
			
			//game is over 
			arena.setArenaState(GameStateEnum.WAITING);
			arena.broadcastMessage(ChatColor.RED + "There are too few players. Countdown stopped.");
			arena.sendArenaToLobby();
			return; 
		}

		// If time is 0 start the round!
		
		if (time == 0) {
			cancel();
			
			//start the game
			//TODO: arena.getGame().start();
		
			return;
		}
		
		// If the time is divisible by 15 then broadcast a countdown
		// message.
		
		if (time % 15 == 0 || time <= 10) {
				if (time != 1) {
					arena.broadcastMessage(ChatColor.AQUA + "Game will start in " + time + " seconds.");
				} else {
					arena.broadcastMessage(ChatColor.AQUA + "Game will start in " + time + " second.");
				}
		}


		//THIS SHOULD NEVER HAPPEN. THE ONLY TIME AN ARENA COUNTDOWN SHOULD START IS WHEN THERE ARE ENOUGH PLAYERS.
		
		
	time--;
	}
	
	
}
