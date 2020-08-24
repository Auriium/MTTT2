package main.java.com.elytraforce.mttt2.objects;

import org.bukkit.entity.Player;

import main.java.com.elytraforce.mttt2.Main;
import main.java.com.elytraforce.mttt2.enums.GamePlayerRoleEnum;
import main.java.com.elytraforce.mttt2.objects.arena.Arena;

public class GamePlayer{
	
	private Player player;
	private GamePlayerRoleEnum playerRole;
	private Arena arena;
	private Integer shopPoints;
	private Integer randomKills;
	private Integer kills;
	
	public GamePlayer(Player player, Arena arena) {
		this.player = player;
		//This is defined as none here so that the player can be assigned a role later.
		this.playerRole = GamePlayerRoleEnum.NONE;
		this.arena = arena;
		this.shopPoints = 0;
		this.randomKills = 0;
		this.kills = 0;
	}
	
	public Integer getKills() {
		return this.kills;
	}
	
	public void setKills(Integer kills) {
		this.kills = kills;
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
	
	
}
