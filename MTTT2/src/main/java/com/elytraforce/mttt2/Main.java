package main.java.com.elytraforce.mttt2;


import org.bukkit.plugin.java.JavaPlugin;

import main.java.com.elytraforce.mttt2.commands.TTTCommand;
import main.java.com.elytraforce.mttt2.config.MessageHandler;
import main.java.com.elytraforce.mttt2.objects.Manager;
import main.java.com.elytraforce.mttt2.utils.TitleActionbarHandler;

public class Main extends JavaPlugin {

	public MessageHandler messageHandler;
	public TitleActionbarHandler titleActionbarHandler;
	//private static instance i know you arent supposed to do this but fuck it
	//most of the time i will pass the main class through a constructor
	//however in this instance it is required to use a static
	
	public static Main instance;
	
	//Commands
	
	public TTTCommand tttcommand;
	
	
	
	@Override
	public void onEnable() {
		Main.instance = this;
		
		//setup main manager
		Manager.setup();
		
		initializeClasses();
		initializeCommands();

	}
	
	@Override 
	public void onDisable() {
		
	}
	
	private void initializeClasses() {
		this.messageHandler = new MessageHandler(this);
		this.messageHandler.createCustomConfig();
		this.titleActionbarHandler = new TitleActionbarHandler(this);
		this.tttcommand = new TTTCommand(this);
	}
	
	private void initializeCommands() {
		this.getCommand("ttt").setExecutor(this.tttcommand);
	}
	
	// 
	//
	//
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
}
