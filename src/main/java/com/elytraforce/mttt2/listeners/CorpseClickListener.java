package com.elytraforce.mttt2.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.golde.bukkit.corpsereborn.CorpseAPI.events.CorpseClickEvent;

import com.elytraforce.mttt2.Main;
import com.elytraforce.mttt2.objects.CorpseObject;
import com.elytraforce.mttt2.objects.Manager;
import com.elytraforce.mttt2.objects.arena.Arena;

public class CorpseClickListener implements Listener{
	
	@EventHandler
	public void onClickCorpse(CorpseClickEvent event) {
		event.setCancelled(true);
		Arena clickedArena = Manager.getInstance().findPlayerArena(event.getClicker());
		CorpseObject clicked = clickedArena.findCorpse(event.getCorpse());
		Bukkit.broadcastMessage(clicked.toString());
		if (clicked.isRevealed()) {
			return; 
		}
		
		Main.getMain().getTitleActionbarHandler().sendMessageBroadcast(clickedArena, "&fThe body of &7" + clicked.getGamePlayer().getPlayer().getName() + " &fwas found. They were a " + clicked.getRole() + "&f!");
		
		clicked.reveal();
		
		
		
		
	}

}
