package main.java.com.elytraforce.mttt2.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import main.java.com.elytraforce.mttt2.enums.GamePlayerRoleEnum;
import main.java.com.elytraforce.mttt2.enums.GameStateEnum;
import main.java.com.elytraforce.mttt2.objects.GamePlayer;
import main.java.com.elytraforce.mttt2.objects.Manager;

public class ProtectionListener implements Listener
{
//make sure to listen for pvp!
	
	
	@EventHandler
	public void onTakeDamage(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player) {
			
			
			Player player = (Player) event.getEntity();
			
			//checks if the player is in a game
			//if this throws an NPE it means either the arena doesnt exist or the player doesnt exist as a GamePlayer
			//so stop here.
			GamePlayer gamePlayer;
			try {
				gamePlayer = Manager.getInstance().findPlayerArena(player).findGamePlayer(player);
			} catch (NullPointerException e) {
				return;
			}
			
			
			if (gamePlayer.getArena().getArenaState().equals(GameStateEnum.COUNTDOWN)
					|| gamePlayer.getArena().getArenaState().equals(GameStateEnum.PRE_MATCH)
					|| gamePlayer.getArena().getArenaState().equals(GameStateEnum.ENDING)
					|| gamePlayer.getArena().getArenaState().equals(GameStateEnum.WAITING)) {
				event.setCancelled(true);
				return;
			}
			

			Player damager = (Player) event.getDamager();
			GamePlayer damagerPlayer;
			
			try {
				damagerPlayer = Manager.getInstance().findPlayerArena(damager).findGamePlayer(damager);
			} catch (NullPointerException e) {
				return;
			}
			
			
			
			if (damagerPlayer.getRole().equals(GamePlayerRoleEnum.SPECTATOR) || damagerPlayer.getRole().equals(GamePlayerRoleEnum.NONE)) {
				event.setCancelled(true);
				return;
			}
			
			if (gamePlayer.getRole().equals(GamePlayerRoleEnum.SPECTATOR) || gamePlayer.getRole().equals(GamePlayerRoleEnum.NONE)) {
				event.setCancelled(true);
				return;
			}
			
			
			
			
			
		}
	}
	
}
