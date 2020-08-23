package main.java.com.elytraforce.mttt2.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import main.java.com.elytraforce.mttt2.Main;
import main.java.com.elytraforce.mttt2.objects.GamePlayer;
import main.java.com.elytraforce.mttt2.objects.arena.Arena;
import net.md_5.bungee.api.ChatColor;

public class TitleActionbarHandler {

	private Main mainClass;
	
	public TitleActionbarHandler(Main main) {
		this.mainClass = main;
	}
	
	public void sendTitle(Player player, String title, String subtitle) {
		player.sendTitle(parseColor(title), parseColor(subtitle), 0, 20, 10);
	}
	
	public void sendTitle(Arena arena, String title, String subtitle) {
		for (GamePlayer player : arena.getArenaPlayers()) {
			player.getPlayer().sendTitle(parseColor(title), parseColor(subtitle), 0, 20, 10);
		}
	}
	
	public void sendTitleAll(String title, String subtitle) {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			player.sendTitle(parseColor(title), parseColor(subtitle), 0, 20, 10);
		}
	}
	
	public void sendMessage(Player player, String message) {
		player.sendMessage(mainClass.getMessageHandler().getMessage("prefix", false) + message);
	}
	
	public void sendMessageBroadcast(Arena arena, String message) {
		for (GamePlayer player : arena.getArenaPlayers()) {
			player.getPlayer().sendMessage(mainClass.getMessageHandler().getMessage("prefix", false) + message);
		}
	}
	
	public void sendMessageBroadcast(String message) {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			player.sendMessage(mainClass.getMessageHandler().getMessage("prefix", false) + message);
		}
	}
	
	public String parseColor(String input) {
		return ChatColor.translateAlternateColorCodes('&', input);
	}
}
