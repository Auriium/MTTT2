package com.elytraforce.mttt2.utils;

import com.elytraforce.mttt2.Main;
import com.elytraforce.mttt2.objects.GamePlayer;
import com.elytraforce.mttt2.objects.arena.Arena;

public class SoundHandler {

	private final Main mainClass;
	
	public SoundHandler(Main main) {
		this.mainClass = main;
	}
	
	public void playSound(GamePlayer player, String sound, int volume, int pitch) {
		player.getPlayer().playSound(player.getPlayer().getLocation(), sound, volume, pitch);
	}
	
	public void playSound(Arena arena, String sound, int volume, int pitch) {
		for (GamePlayer player : arena.getArenaPlayers()) {
			player.getPlayer().playSound(player.getPlayer().getLocation(), sound, volume, pitch);
		}
	}
	

	
}
