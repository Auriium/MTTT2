package main.java.com.elytraforce.mttt2.objects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.golde.bukkit.corpsereborn.CorpseAPI.CorpseAPI;
import org.golde.bukkit.corpsereborn.nms.Corpses.CorpseData;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import main.java.com.elytraforce.mttt2.Main;
import main.java.com.elytraforce.mttt2.enums.GamePlayerRoleEnum;

public class CorpseObject {
	
	//yet another wrapper
	
	private CorpseData corpseData;
	private Location corpseLocation;
	private GamePlayer player;
	private GamePlayerRoleEnum role;
	private boolean revealed;
	private Hologram corpseHolo;

	public CorpseObject(Location corpseLocation, GamePlayer player, GamePlayerRoleEnum corpseRole) {
		this.revealed = false;
		this.corpseLocation = corpseLocation;
		this.player = player;
		this.role = corpseRole;
		this.corpseData = CorpseAPI.spawnCorpse(player.getPlayer(), corpseLocation, null, new ItemStack(Material.PUMPKIN), new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.IRON_BOOTS));
		this.corpseHolo = HologramsAPI.createHologram(Main.getMain(), corpseLocation);
		Bukkit.broadcastMessage("spawning hologram also this role is a " + role.toString());
		
		corpseData.resendCorpseToEveryone();


		corpseHolo.appendTextLine(parseColor("&7Unidentified Body"));
		corpseHolo.appendTextLine(parseColor("&7Unidentified Body"));
		//add corpse to the list
		
		player.getArena().getCorpses().add(this);
		
	}
	
	public GamePlayerRoleEnum getRole() {
		return this.role;
	}
	
	public boolean isRevealed() {
		return this.revealed;
	}
	
	public CorpseData getCorpseData() {
		return this.corpseData;
	}
	
	public void setCorpseData(CorpseData data) {
		this.corpseData = data;
	}
	
	public GamePlayer getGamePlayer() {
		return this.player;
	}
	
	public void setGamePlayer(GamePlayer player) {
		this.player = player;
	}
	
	public void reveal() {
		if (this.revealed == true) {
			return;
		}
		this.revealed = true;
		CorpseAPI.removeCorpse(this.corpseData);
		//todo: color of player = their role
		//TODO: DNA scanner tracking n shit
		corpseHolo.insertTextLine(0, parseColor("&7Body of " + this.player.getColoredName()));
		this.corpseData = CorpseAPI.spawnCorpse(player.getPlayer(), corpseLocation, null, null, new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.IRON_BOOTS));
 
	}
	
	public void clear() {
		CorpseAPI.removeCorpse(this.corpseData);
		this.corpseHolo.delete();
		
	}
	
	public void remove() {
		CorpseAPI.removeCorpse(this.corpseData);
		player.getArena().getCorpses().remove(this);
		this.corpseHolo.delete();
		
	}
	
	public String parseColor(String string) {
		return ChatColor.translateAlternateColorCodes('&', "");
	}
	
	
	
	//Since this will be a runnable that manages the hologram etc etc etc we need to make sure
	//that the corpse object is passed to us so we can properly display
	//what role it is.
	
	//also need a spawn() method 
	
}
