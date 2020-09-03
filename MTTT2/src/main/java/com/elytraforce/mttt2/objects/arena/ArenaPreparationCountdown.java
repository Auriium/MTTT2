package main.java.com.elytraforce.mttt2.objects.arena;

import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.com.elytraforce.mttt2.Main;
import main.java.com.elytraforce.mttt2.enums.GameStateEnum;

public class ArenaPreparationCountdown extends BukkitRunnable{
	private int time;
	private final Arena arena;
	private int initialTime;

	public ArenaPreparationCountdown(Arena arena) {
		this.arena = arena;
		this.time = 0;
		this.initialTime = 0;
	}
	
	public void start(int time)  {

		
		//maybe bossbar?
		arena.actionPreparationPhase();
		
		arena.setArenaState(GameStateEnum.PRE_MATCH);
		this.time = time;
		this.initialTime = time;
		this.runTaskTimer(Main.getMain(), 0L, 20L);
		
		//method here should handle players being teleported to the arena.
		this.arena.sendArenaToGame();
		
		//guns also need to be spawned into the map here

	}
	
	public boolean isRunning() {
		return arena.getArenaState() == GameStateEnum.PRE_MATCH;
	}
	
	public int getCountdownTime() {
		return this.time;
	}
	
	@Override
	public void run() {
		
		if (arena.getArenaPlayers().size() < arena.getRequiredPlayers()) {
			cancel();
			arena.resetArenaPreperationCountdown();
			arena.setArenaState(GameStateEnum.WAITING);
			arena.broadcastMessage(ChatColor.RED + "There are too few players. Countdown stopped.");
			arena.sendArenaToLobby();
			return; 
		}

		// If time is 0 start the round!
		
		if (time == 0) {
			cancel();
			arena.resetArenaPreperationCountdown();
			
			//start the game, TODO: in the future this value for arena length should be gathered from config.
			arena.getArenaGame().start(180);
		
			return;
		}
		
		// If the time is divisible by 15 then broadcast a countdown
		// message.
		
		if (time % 5 == 0 || time <= 5) {
				arena.getMain().getSoundHandler().playSound(arena, "block.note_block.pling", 1, 1);
				if (time != 1) {
					arena.broadcastMessage(ChatColor.AQUA + "Game will start in " + time + " seconds.");
				} else {
					arena.broadcastMessage(ChatColor.AQUA + "Game will start in " + time + " second.");
				}
		}
		arena.setXPBar(time, initialTime);


		//THIS SHOULD NEVER HAPPEN. THE ONLY TIME AN ARENA COUNTDOWN SHOULD START IS WHEN THERE ARE ENOUGH PLAYERS.
		
		
	time--;
	}
	
	
}
