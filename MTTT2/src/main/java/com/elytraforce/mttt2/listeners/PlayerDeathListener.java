package main.java.com.elytraforce.mttt2.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;

import main.java.com.elytraforce.mttt2.Main;
import main.java.com.elytraforce.mttt2.enums.CauseOfDeathEnum;
import main.java.com.elytraforce.mttt2.enums.GamePlayerRoleEnum;
import main.java.com.elytraforce.mttt2.enums.GameStateEnum;
import main.java.com.elytraforce.mttt2.objects.GamePlayer;
import main.java.com.elytraforce.mttt2.objects.Manager;
import main.java.com.elytraforce.mttt2.objects.arena.Arena;

public class PlayerDeathListener implements Listener{
	
	private Main mainClass;
	
	public PlayerDeathListener(Main main) {
		this.mainClass = main;
	}
	
	//todo: call custom event here
	@EventHandler
	public void onDeathEvent(PlayerDeathEvent event) {
		
		Player player = event.getEntity();
		
		//checks if the player is in a game
		//if this throws an NPE it means either the arena doesnt exist or the player doesnt exist as a GamePlayer
		//so stop here.
		try {
			Manager.getInstance().findPlayerArena(player).findGamePlayer(player);
		} catch (NullPointerException e) {
			return;
		}
		
		GamePlayer gamePlayer = Manager.getInstance().findPlayerArena(player).findGamePlayer(player);
		Arena playerArena = Manager.getInstance().findPlayerArena(player);
		
		//redundant check
		if (playerArena.getArenaState().equals(GameStateEnum.MATCH)) {
			
			gamePlayer.setDeaths(gamePlayer.getDeaths() + 1);
			
			gamePlayer.getPlayer().spigot().respawn();
			
			//get cause of death and then send titles
			
			//TODO: make sure that PVPlevels or whatever isnt' sending death messages here.
			if (hasMurderer(gamePlayer)) {
				CauseOfDeathEnum death = this.getCauseOfDeath(gamePlayer, true);
				GamePlayer murderer = Manager.getInstance().findPlayerArena(player.getPlayer().getKiller()).findGamePlayer(player.getPlayer().getKiller());
				
				mainClass.getTitleActionbarHandler().sendTitle(
						gamePlayer.getPlayer(), "&4&lYou were killed by", 
						death.getFormattedString() + "&7 " + gamePlayer.getPlayer().getKiller().getDisplayName() + "&c!" );
				
				//give the murderer a kill... if it if an rdm or not, we will have to see..
				murderer.setKills(murderer.getKills() + 1);
			}	else {
				CauseOfDeathEnum death = this.getCauseOfDeath(gamePlayer, true);
				
				mainClass.getTitleActionbarHandler().sendTitle(
						gamePlayer.getPlayer(), "&4&lYou died by", 
						death.getFormattedString() + "&c!" );
				
				
				this.getCauseOfDeath(gamePlayer, false);
			}
			
			//CHECK RDM HERE
			
			//Main.getRDMHandler.checkRDM(gamePlayer, gamePlayer.getPlayer.getKiller.turn this into a GamePlayer)
			
			gamePlayer.setRole(GamePlayerRoleEnum.SPECTATOR);
			
			//DOES NOT ACTUALLY PUT PLAYER IN SPECTATOR, rather sets them to invisible and flying lmao
			gamePlayer.cleanupPlayer(GameMode.SPECTATOR);
			
			playerArena.checkCancel();
			
		}
		
		
	}
	
	public boolean hasMurderer(GamePlayer player) {
		try {
			Manager.getInstance().findPlayerArena(player.getPlayer().getKiller()).findGamePlayer(player.getPlayer().getKiller());
			return true;
		} catch (NullPointerException e) {
			//should not happen
			return false;
		}
	}

	public CauseOfDeathEnum getCauseOfDeath(GamePlayer player, boolean hasMurdererInternal) {
		if (hasMurdererInternal) {
			//this should run if they have a killer
			
			//there should probably be a try/catch loop here to check if the killer is 
			//in an arena 
			
			GamePlayer murderer = Manager.getInstance().findPlayerArena(player.getPlayer().getKiller()).findGamePlayer(player.getPlayer().getKiller());
			
			//murderer exists, get their role
			GamePlayerRoleEnum murderRole = murderer.getRole();
			
			switch(murderRole) {
			case DETECTIVE:
				return CauseOfDeathEnum.DETECTIVE;
			case INNOCENT:
				return CauseOfDeathEnum.INNOCENT;
			case NONE:
				return CauseOfDeathEnum.UNKNOWN;
			case SPECTATOR:
				return CauseOfDeathEnum.UNKNOWN;
			case TRAITOR:
				return CauseOfDeathEnum.TRAITOR;
			default:
				return CauseOfDeathEnum.UNKNOWN;
			}
			
			
		} else {
			//this should run if they dont have a killer
			DamageCause checker = player.getPlayer().getLastDamageCause().getCause();
			
			if (checker.equals(DamageCause.FIRE) || checker.equals(DamageCause.FLY_INTO_WALL) || checker.equals(DamageCause.LAVA)) {
				return CauseOfDeathEnum.BURNING;
			}
			
			if (checker.equals(DamageCause.FALL)) {
				return CauseOfDeathEnum.FALLING;
			}
			
			
		}
		return null;
	}
}
