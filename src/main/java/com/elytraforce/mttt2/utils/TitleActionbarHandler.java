package com.elytraforce.mttt2.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.elytraforce.mttt2.Main;
import com.elytraforce.mttt2.objects.GamePlayer;
import com.elytraforce.mttt2.objects.arena.Arena;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class TitleActionbarHandler {

	private final Main mainClass;
	
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
	
	public void sendActionBar(Player player, String actionbar) {
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(parseColor(actionbar)));
	}
	
	public void sendActionBar (Arena arena, String actionbar) {
		for (GamePlayer player : arena.getArenaPlayers()) {
			player.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(parseColor(actionbar)));
		}
	}
	
	public void sendMessage(Player player, String message) {
		player.sendMessage(mainClass.getMessageHandler().getMessage("prefix", false) + parseColor(message));
	}
	
	public void sendMessageBroadcast(Arena arena, String message) {
		for (GamePlayer player : arena.getArenaPlayers()) {
			player.getPlayer().sendMessage(mainClass.getMessageHandler().getMessage("prefix", false) + parseColor(message));
		}
	}
	
	public void sendMessageBroadcast(String message) {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			player.sendMessage(mainClass.getMessageHandler().getMessage("prefix", false) + parseColor(message));
		}
	}
	
	public String parseColor(String input) {
		return ChatColor.translateAlternateColorCodes('&', input);
	}
}
