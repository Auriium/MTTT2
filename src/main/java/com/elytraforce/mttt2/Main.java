package com.elytraforce.mttt2;


import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;


import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import com.elytraforce.mttt2.commands.TTTCommand;
import com.elytraforce.mttt2.config.MapConfigHandler;
import com.elytraforce.mttt2.config.MessageHandler;
import com.elytraforce.mttt2.listeners.CorpseClickListener;
import com.elytraforce.mttt2.listeners.PlayerDeathListener;
import com.elytraforce.mttt2.listeners.PlayerJoinListener;
import com.elytraforce.mttt2.listeners.ProtectionListener;
import com.elytraforce.mttt2.objects.Manager;
import com.elytraforce.mttt2.objects.arena.Arena;
import com.elytraforce.mttt2.utils.SoundHandler;
import com.elytraforce.mttt2.utils.TitleActionbarHandler;
import com.elytraforce.mttt2.utils.TraitorGlowHandler;

public class Main extends JavaPlugin {

	public MessageHandler messageHandler;
	public TitleActionbarHandler titleActionbarHandler;
	public SoundHandler soundHandler;
	public MapConfigHandler mapConfigHandler;
	public TraitorGlowHandler glowHandler;
	//private static instance i know you arent supposed to do this but fuck it
	//most of the time i will pass the main class through a constructor
	//however in this instance it is required to use a static
	
	public static Main instance;
	private ProtocolManager protocolManager;
	
	//Commands
	
	public TTTCommand tttcommand;
	
	
	
	@Override
	public void onEnable() {
		Main.instance = this;
		
		this.protocolManager = ProtocolLibrary.getProtocolManager();
		
		this.initializeClasses();
		this.initializeCommands();
		this.initializeListeners();
		
		
		final File worldsFile = new File(this.getDataFolder(), "template_worlds");
        if (!worldsFile.exists()) {
            worldsFile.mkdir();
            this.getLogger().warning("*** ATTENTION ***");
            this.getLogger().warning("The template_worlds folder has been generated. The plugin will be disabled. Please drop at least one template world into this folder and restart or load the plugin.");
        }
		
		//setup main manager
		Manager.setup();
		
		
		//fuck java and static references
		Manager.getInstance().setRandomArena();
		this.printDebugLine("[MTTT2] Setting bungee autoselected map to " + Manager.getInstance().getSelectedArena());

		Manager.getInstance().setupTeams();
		//initialize protocol shit
	}
	
	
	@Override 
	public void onDisable() {
		for (Arena arena : Manager.getInstance().getArenas()) {
			arena.shutdown(false, true);
		}
	}
	
	private void initializeClasses() {
		//Load main config BEFORE all other configs 
		
		this.messageHandler = new MessageHandler(this);
		
		this.soundHandler = new SoundHandler(this);
		
		this.mapConfigHandler = new MapConfigHandler(this);
		
		this.titleActionbarHandler = new TitleActionbarHandler(this);
		this.tttcommand = new TTTCommand(this);
		this.glowHandler = new TraitorGlowHandler(this);
	}
	
	private void initializeCommands() {
		this.getCommand("ttt").setExecutor(this.tttcommand);
	}
	
	private void initializeListeners() {
		this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
		this.getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
		this.getServer().getPluginManager().registerEvents(new ProtectionListener(), this);
		this.getServer().getPluginManager().registerEvents(new CorpseClickListener(), this);
	}
	// 
	//
	//
	
	public TraitorGlowHandler getTraitorGlowHandler() {
		return this.glowHandler;
	}
	
	public ProtocolManager getProtocolManager() {
		return this.protocolManager;
	}
	
	public MapConfigHandler getMapConfigHandler() {
		return this.mapConfigHandler;
	}
	
	public SoundHandler getSoundHandler() {
		return this.soundHandler;
	}
	public TitleActionbarHandler getTitleActionbarHandler() {
		return this.titleActionbarHandler;
	}
	
	public MessageHandler getMessageHandler() {
		return this.messageHandler;
	}
	
	public static Main getMain() {
		return Main.instance;
	}
	
	public void printDebugLine(String string) {
		System.out.println(string);
	}
	
	public String formatSeconds(Integer seconds) {
		return null;
		
	}
	
	
}
