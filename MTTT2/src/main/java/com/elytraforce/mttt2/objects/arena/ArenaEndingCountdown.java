package main.java.com.elytraforce.mttt2.objects.arena;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.com.elytraforce.mttt2.Main;
import main.java.com.elytraforce.mttt2.enums.GamePlayerRoleEnum;
import main.java.com.elytraforce.mttt2.enums.GameStateEnum;
import main.java.com.elytraforce.mttt2.objects.GamePlayer;

public class ArenaEndingCountdown extends BukkitRunnable{
	private int time;
	private final Arena arena;
	private int initialTime;

	public ArenaEndingCountdown(Arena arena) {
		this.arena = arena;
		this.time = 0;
		this.initialTime = 0;
	}
	
	public void start(int time, GamePlayerRoleEnum winner)  {
		
		//Now we need to tell everyone who won!
		
		arena.getMain().getSoundHandler().playSound(arena, "entity.experience_orb.pickup", 1, 1);
		switch (winner) {
		case INNOCENT:
			arena.getMain().getTitleActionbarHandler().sendTitle(arena, "&4&lGame Over!", "&7The &aInnocents &7win!");
			break;
		case TRAITOR:
			arena.getMain().getTitleActionbarHandler().sendTitle(arena, "&4&lGame Over!", "&7The &cTraitors &7win!");
			break;
		case DETECTIVE:
			//this should never happen
			arena.getMain().getTitleActionbarHandler().sendTitle(arena, "&4&lGame Over!", "&7The &9Detectives &7win!");
			break;
		case SPECTATOR:
			//this also should never happen
			break;
		case NONE:
			arena.getMain().getTitleActionbarHandler().sendTitle(arena, "&4&lGame Over!", "&7Nobody won!");
			break;
		}
		
		//TODO: now tell everyone how many rdm's they did and punish them accordingly.
		this.arena.actionCountRDM();

		arena.setArenaState(GameStateEnum.ENDING);
		this.time = time;
		this.initialTime = time;
		this.runTaskTimer(Main.getMain(), 0L, 20L);
		
		//run pre-cleanup
		
		//TODO: show what everyone was, show the traitors and etc
		

	}
	
	public boolean isRunning() {
		return arena.getArenaState() == GameStateEnum.ENDING;
	}
	
	public int getCountdownTime() {
		return this.time;
	}
	
	@Override
	public void run() {
		
		//This method is pointless as the game is over
		//if (arena.getArenaPlayers().size() < arena.getRequiredPlayers()) {
			//cancel();
			//
			//game is over 
			//Run method arena.getArenaEnding().start(10, GamePlayerRoleEnum.NONE)
			//return; 
		//}
		
		if (time == 0) {
			cancel();
			arena.resetArenaEndingCountdown();
			
			//it is about to reset.
		
			this.arena.reset();
			
			return;
		}
		
		// If the time is divisible by 15 then broadcast a countdown
		// message.
		
		if (time % 5 == 0 || time <= 3) {
				arena.getMain().getSoundHandler().playSound(arena, "block.note_block.pling", 1, 1);
				if (time != 1) {
					arena.broadcastMessage(ChatColor.AQUA + "Sending you to hub in " + time + " seconds.");
				} else {
					arena.broadcastMessage(ChatColor.AQUA + "Sending you to hub in " + time + " second.");
				}
		}
		arena.setXPBar(time, initialTime);

		
		
	time--;
	}
	
	
}
