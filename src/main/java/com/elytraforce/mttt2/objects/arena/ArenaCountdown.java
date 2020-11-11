package com.elytraforce.mttt2.objects.arena;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitRunnable;

import com.elytraforce.mttt2.Main;
import com.elytraforce.mttt2.enums.GameStateEnum;
import com.elytraforce.mttt2.objects.GamePlayer;

public class ArenaCountdown extends BukkitRunnable{
	private int time;
	private final Arena arena;
	private int initialTime;

	public ArenaCountdown(Arena arena) {
		this.arena = arena;
		this.time = 0;
		this.initialTime = 0;
	}
	
	public void start(int time)  {

		arena.setArenaState(GameStateEnum.COUNTDOWN);
		this.time = time;
		this.initialTime = time;
		this.runTaskTimer(Main.getMain(), 0L, 20L);
		
		arena.registerTeams();

	}
	
	public boolean isRunning() {
		return arena.getArenaState() == GameStateEnum.COUNTDOWN;
	}
	
	public int getCountdownTime() {
		return this.time;
	}
	
	@Override
	public void run() {

		if (arena.getArenaPlayers().size() < arena.getRequiredPlayers()) {
			cancel();
			arena.resetArenaCountdown();
			arena.setArenaState(GameStateEnum.WAITING);
			arena.broadcastMessage(ChatColor.RED + "There are too few players. Countdown stopped.");
			return; 
		}
		// If time is 0 start the round!
		if (time == 0) {
			cancel();
			arena.resetArenaCountdown();
			arena.getDisplayBar1().removeAll();
			
			//start the Arena Preparation Countdown Phase
			
			//TODO: This 20 here represents the arena preparation time. In the future, get this from a config.
			arena.getArenaPreparationCountdown().start(20);
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
		
	time--;
	}
	
	
}
