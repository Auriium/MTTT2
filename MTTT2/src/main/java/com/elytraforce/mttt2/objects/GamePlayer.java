package main.java.com.elytraforce.mttt2.objects;

import org.bukkit.entity.Player;

import main.java.com.elytraforce.mttt2.enums.GamePlayerRoleEnum;
import main.java.com.elytraforce.mttt2.objects.arena.Arena;

public class GamePlayer{
	
	private Player player;
	private GamePlayerRoleEnum playerRole;
	private Arena arena;
	private Integer shopPoints;
	
	public GamePlayer(Player player, Arena arena) {
		this.player = player;
		//This is defined as none here so that the player can be assigned a role later.
		this.playerRole = GamePlayerRoleEnum.NONE;
		this.arena = arena;
		this.shopPoints = 0;
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
