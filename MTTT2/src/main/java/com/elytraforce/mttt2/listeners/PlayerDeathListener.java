package main.java.com.elytraforce.mttt2.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import main.java.com.elytraforce.mttt2.Main;
import main.java.com.elytraforce.mttt2.enums.CauseOfDeathEnum;
import main.java.com.elytraforce.mttt2.enums.GamePlayerRoleEnum;
import main.java.com.elytraforce.mttt2.enums.GameStateEnum;
import main.java.com.elytraforce.mttt2.objects.GamePlayer;
import main.java.com.elytraforce.mttt2.objects.Manager;
import main.java.com.elytraforce.mttt2.objects.arena.Arena;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_15_R1.PacketPlayInClientCommand;
import net.minecraft.server.v1_15_R1.PacketPlayInClientCommand.EnumClientCommand;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;


public class PlayerDeathListener implements Listener{
	
	private Main mainClass;
	
	public PlayerDeathListener(Main main) {
		this.mainClass = main;
	}
	
	
	@EventHandler
	public void onRespawnEvent(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		GamePlayer gamePlayer;
		try {
			gamePlayer = Manager.getInstance().findPlayerArena(player).findGamePlayer(player);
		} catch (NullPointerException e) {
			
			Bukkit.broadcastMessage("not found");
			return;
		}
		
		if (!gamePlayer.getDeathLocation().equals(null)) {
			event.setRespawnLocation(gamePlayer.getDeathLocation());
		}
		
		gamePlayer.setRole(GamePlayerRoleEnum.SPECTATOR);
    	gamePlayer.cleanupPlayer(GameMode.SPECTATOR);
		
		
	}
	
	//todo: call custom event here
	@EventHandler
	public void onDeathEvent(PlayerDeathEvent event) {
		
		Bukkit.broadcastMessage("death listener");
		Player player = event.getEntity();
		GamePlayer gamePlayer;
		
		event.setDroppedExp(0);
		
		Bukkit.broadcastMessage(player.getLastDamageCause().getCause().toString());
		
		//checks if the player is in a game
		//if this throws an NPE it means either the arena doesnt exist or the player doesnt exist as a GamePlayer
		//so stop here.
		try {
			gamePlayer = Manager.getInstance().findPlayerArena(player).findGamePlayer(player);
		} catch (NullPointerException e) {
			
			Bukkit.broadcastMessage("not found");
			return;
		}
		
		Arena playerArena = Manager.getInstance().findPlayerArena(player);
		//delays because bukkit is ass and makes you wait when you want to respoon people
		
		CauseOfDeathEnum damageCauseEnum = null;
		GamePlayer murderer = null;
		
		//figure out what killed them
		if (playerArena.getArenaState().equals(GameStateEnum.MATCH)) {
			
			DamageCause lastDamageCause = player.getLastDamageCause().getCause();
			//set death location
			gamePlayer.setDeathLocation(player.getLocation());
			
			if (lastDamageCause.equals(DamageCause.FIRE) 
					|| lastDamageCause.equals(DamageCause.FIRE_TICK)
					|| lastDamageCause.equals(DamageCause.LAVA)) {
				damageCauseEnum = CauseOfDeathEnum.BURNING;
			}
			
			if (lastDamageCause.equals(DamageCause.FALL)) {
				damageCauseEnum = CauseOfDeathEnum.FALLING;
			}
			
			if (this.hasMurderer(gamePlayer)) {
				murderer = Manager.getInstance().findPlayerArena(player.getPlayer().getKiller()).findGamePlayer(player.getPlayer().getKiller());
				switch(murderer.getRole()) {
				case DETECTIVE:
					damageCauseEnum = CauseOfDeathEnum.DETECTIVE;
					break;
				case INNOCENT:
					damageCauseEnum = CauseOfDeathEnum.INNOCENT;
					break;
				case NONE:
					damageCauseEnum = CauseOfDeathEnum.UNKNOWN;
					break;
				case SPECTATOR:
					damageCauseEnum = CauseOfDeathEnum.UNKNOWN;
					break;
				case TRAITOR:
					damageCauseEnum = CauseOfDeathEnum.TRAITOR;
					break;
				default:
					damageCauseEnum = CauseOfDeathEnum.UNKNOWN;
					break;
				}
			}
			
			if (lastDamageCause.equals(null)) {
				damageCauseEnum = CauseOfDeathEnum.UNKNOWN;
			}	
		}
		
		new BukkitRunnable() {
            public void run() {
            	Bukkit.broadcastMessage("respawning");
            	player.spigot().respawn();
            	Bukkit.broadcastMessage("respawned");
            	
            }
        }.runTaskLater(mainClass, (long)1L);
        
        
        new BukkitRunnable() {
            public void run() {
            	//handle rdm stuff here pls
            	if (hasMurderer(gamePlayer)) {
            		GamePlayer smallMurderer = Manager.getInstance().findPlayerArena(player.getPlayer().getKiller()).findGamePlayer(player.getPlayer().getKiller());
            		smallMurderer.setKills(smallMurderer.getKills() + 1);
            	}
            	gamePlayer.setDeaths(gamePlayer.getDeaths() + 1);
            	playerArena.checkCancel();
            }
        }.runTaskLater(mainClass, (long)4L);
		
		//this as a cause/
        
		
	}
	
	public String getFormattedString(CauseOfDeathEnum thing) {
		//issue happens at 149 FOCUHAEOEOISFHSIUEHISUEHIEFH
		switch(thing) {
		case BURNING:
			return parseColor("&c&lBURNING");
		case DETECTIVE:
			return parseColor("&9&lDETECTIVE");
		case FALLING:
			return parseColor("&f&lFALLING");
		case INNOCENT:
			return parseColor("&a&lINNOCENT");
		case TRAITOR:
			return parseColor("&4&lTRAITOR");     
		case UNKNOWN:
			return parseColor("&8&lUNKNOWN");
		default:
			return parseColor("&8&lUNKNOWN");
		}
	}
	
	
	public String parseColor(String input) {
		return ChatColor.translateAlternateColorCodes('&', input);
	}
	
	public boolean hasMurderer(GamePlayer player) {
		
		//if (player.getPlayer().getLastDamageCause().getCause().equals(DamageCause.PROJECTILE || DamageCause.CRAMMING))
		if (player.getPlayer().getKiller() != null) {
			return true;
		}
			return false;

	}

	//useless
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
