package main.java.com.elytraforce.mttt2.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import main.java.com.elytraforce.mttt2.enums.GameStateEnum;
import main.java.com.elytraforce.mttt2.objects.GamePlayer;
import main.java.com.elytraforce.mttt2.objects.Manager;

public class ProtectionListener implements Listener
{
//make sure to listen for pvp!
	
	
	@EventHandler
	public void onTakeDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			
			
			Player player = (Player) event.getEntity();
			
			//checks if the player is in a game
			//if this throws an NPE it means either the arena doesnt exist or the player doesnt exist as a GamePlayer
			//so stop here.
			try {
				Manager.getInstance().findPlayerArena(player).findGamePlayer(player);
			} catch (NullPointerException e) {
				return;
			}
			
			GamePlayer gamePlayer = Manager.getInstance().findPlayerArena(player).findGamePlayer(player);
			
			if (gamePlayer.getArena().getArenaState().equals(GameStateEnum.MATCH)) {
				event.setCancelled(true);
				return;
			}
			
			
			
		}
	}
	
}
