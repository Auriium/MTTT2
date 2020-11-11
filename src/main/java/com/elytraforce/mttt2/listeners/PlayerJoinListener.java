package com.elytraforce.mttt2.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.elytraforce.mttt2.Main;
import com.elytraforce.mttt2.objects.Manager;

public class PlayerJoinListener implements Listener {

	private final Main mainClass;
	
	public PlayerJoinListener(com.elytraforce.mttt2.Main main) {
		this.mainClass = main;
	}
	
	@EventHandler
	public void onPlayerLeaveEvent(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		Manager.getInstance().findPlayerArena(player).removePlayer(player);
	}  
	
}
