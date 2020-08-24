package main.java.com.elytraforce.mttt2.listeners;

import org.bukkit.craftbukkit.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import main.java.com.elytraforce.mttt2.objects.Manager;

public class PlayerJoinLeaveEvent implements Listener {

	private Main mainClass;
	
	public PlayerJoinLeaveEvent(Main main) {
		this.mainClass = main;
	}
	
	@EventHandler
	public void onPlayerLeaveEvent(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		Manager.getInstance().findPlayerArena(player).removePlayer(player);
	}
	
}
