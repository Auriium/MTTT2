package main.java.com.elytraforce.mttt2.objects;

import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import main.java.com.elytraforce.mttt2.Main;
import main.java.com.elytraforce.mttt2.enums.GamePlayerRoleEnum;
import main.java.com.elytraforce.mttt2.objects.arena.Arena;

public class GamePlayer{
	
	//wrapper that lets me store data like "random kills, kills" probably will need some way
	//of keeping track if player has a primary or not when i start coding in the gun pickup system...
	
	private Player player;
	private GamePlayerRoleEnum playerRole;
	private Arena arena;
	private Integer shopPoints;
	private Integer randomKills;
	private Integer deaths;
	private Integer kills;
	
	public GamePlayer(Player player, Arena arena) {
		this.player = player;
		//This is defined as none here so that the player can be assigned a role later.
		this.playerRole = GamePlayerRoleEnum.NONE;
		this.arena = arena;
		this.shopPoints = 0;
		this.randomKills = 0;
		this.kills = 0;
		this.deaths = 0;
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
		if (this.playerRole.equals(GamePlayerRoleEnum.SPECTATOR)) {
			return true;
		}
		return false;
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
	public void cleanupPlayer(final GameMode gameMode) {
		GamePlayer player = this;
        player.getPlayer().getActivePotionEffects().forEach(effect -> player.getPlayer().removePotionEffect(effect.getType()));
        if (gameMode == GameMode.SURVIVAL) {
        	this.showPlayer();
            player.getPlayer().setFlying(false);
            player.getPlayer().setAllowFlight(false);
            player.getPlayer().setGameMode(GameMode.ADVENTURE);
        }
        else if (gameMode == GameMode.SPECTATOR) {
        	this.hidePlayer();
        	player.getPlayer().setFlying(true);
            player.getPlayer().setAllowFlight(true);
        	player.getPlayer().setFlySpeed(0.2f);
            player.getPlayer().setGameMode(GameMode.ADVENTURE);
        }
        player.getPlayer().setHealth(player.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        player.getPlayer().setFoodLevel(18);
        player.getPlayer().setFireTicks(0);
        player.getPlayer().getInventory().clear();
    }
	
	public void hidePlayer() {
		for (GamePlayer player : this.arena.getArenaPlayers()) {
			player.getPlayer().hidePlayer(this.getPlayer());
		}
	}
	
	public void showPlayer() {
		for (GamePlayer player : this.arena.getArenaPlayers()) {
			player.getPlayer().showPlayer(this.getPlayer());
		}
	}
	
	
}
