package com.elytraforce.mttt2.objects;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import com.elytraforce.mttt2.Main;
import com.elytraforce.mttt2.enums.GamePlayerRoleEnum;
import com.elytraforce.mttt2.objects.arena.Arena;

public class GamePlayer{
	
	//wrapper that lets me store data like "random kills, kills" probably will need some way
	//of keeping track if player has a primary or not when i start coding in the gun pickup system...
	
	private final Player player;
	private GamePlayerRoleEnum playerRole;
	private final Arena arena;
	private Integer shopPoints;
	private Integer randomKills;
	private Integer deaths;
	private Integer kills;
	private Location deathLocation;
	
	public GamePlayer(Player player, Arena arena) {
		this.player = player;
		//This is defined as none here so that the player can be assigned a role later.
		this.playerRole = GamePlayerRoleEnum.NONE;
		this.arena = arena;
		this.shopPoints = 0;
		this.randomKills = 0;
		this.kills = 0;
		this.deaths = 0;
		this.deathLocation = null;
	}
	
	public String getColoredRole() {
		switch (this.playerRole) {
			case DETECTIVE:
				return parseColor("&9&lDETECTIVE");
			case TRAITOR:
				return parseColor("&c&lTRAITOR");
			case INNOCENT:
				return parseColor("&a&lINNOCENT");
			default:
				return parseColor("&7&lUNKNOWN");
		}
		
	}
	
	public String getColoredName() {
		switch (this.playerRole) {
			case DETECTIVE:
				return parseColor("&9&l" + this.player.getName());
			case TRAITOR:
				return parseColor("&c&l" + this.player.getName());
			case INNOCENT:
				return parseColor("&a&l" + this.player.getName());
			default:
				return parseColor("&7&l" + this.player.getName());
		}
		
	}
	
	public Location getDeathLocation() {
		return this.deathLocation;
	}
	
	public void setDeathLocation(Location loc) {
		this.deathLocation = loc;
	}
	
	public Integer getKills() {
		return this.kills;
	}
	
	public void setKills(Integer kills) {
		this.kills = kills;
	}
	
	public Integer getDeaths() {
		return this.deaths;
	}
	
	public void setDeaths(Integer deaths) {
		this.deaths = deaths;
	}
	
	public Integer getRandomKills() {
		return this.randomKills;
	}
	
	public void setRandomKills(Integer change) {
		this.randomKills = change;
	}
	
	public Integer getPoints() {
		return this.shopPoints;
	}
	
	public void setPoints(Integer point) {
		this.shopPoints = point;
	}

	
	public void subtractPoint() {
		if (this.shopPoints != 0) {
			this.shopPoints = this.shopPoints - 1;
		}
	}
	
	public void addPointFancy(Integer point) {
		this.shopPoints = this.shopPoints + point;
		Main.getMain().getTitleActionbarHandler().sendMessage(this.getPlayer(), "&cReceived &7" + point + " &cpoints!");
	}
	
	public boolean isSpectator() {
		return this.playerRole.equals(GamePlayerRoleEnum.SPECTATOR);
	}
	
	public GamePlayerRoleEnum getRole() {
		return this.playerRole;
	}
	
	public void setRole(GamePlayerRoleEnum role) {
		this.playerRole = role;
	}
	
	public Arena getArena() {
		return this.arena;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	
	//go deprecated fuck yourself
	//like legit
	//i dont care
	
	//please come up with a reason to use hide and show player :)
	public void cleanupPlayer(final GameMode gameMode) {
		
		GamePlayer player = this;
        player.getPlayer().getActivePotionEffects().forEach(effect -> player.getPlayer().removePotionEffect(effect.getType()));
        if (gameMode == GameMode.SURVIVAL) {
        	this.showPlayer();
        	player.getPlayer().setAllowFlight(false);
            player.getPlayer().setFlying(false);
            
            player.getPlayer().setGameMode(GameMode.ADVENTURE);
        }
        else if (gameMode == GameMode.SPECTATOR) {
        	this.hidePlayer();
        	player.getPlayer().setAllowFlight(true);
        	player.getPlayer().setFlying(true);
            
        	player.getPlayer().setFlySpeed(0.2f);
            player.getPlayer().setGameMode(GameMode.ADVENTURE);
        }
        player.getPlayer().setHealth(player.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        player.getPlayer().setFoodLevel(18);
        player.getPlayer().setFireTicks(0);
        player.getPlayer().getInventory().clear();
    }
	
	@SuppressWarnings("deprecation")
	public void hidePlayer() {
		for (GamePlayer fullPlayer : this.arena.getArenaPlayers()) {
			fullPlayer.getPlayer().hidePlayer(player);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void showPlayer() {
		for (GamePlayer fullPlayer : this.arena.getArenaPlayers()) {
			fullPlayer.getPlayer().showPlayer(player);
		}
	}
	
	public String parseColor(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}
	
	
}
