package main.java.com.elytraforce.mttt2.config;

import java.io.File;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import main.java.com.elytraforce.mttt2.Main;

public class MessageHandler {
	FileConfiguration customConfig = null;
	File customConfigurationFile = null;
	Main mainClass;
	
	public MessageHandler(Main main){
		this.mainClass = main;
	}
	
	public void reloadMessageFile() {
	    if (customConfigurationFile == null) {
	    	customConfigurationFile = new File(mainClass.getDataFolder(), "messages.yml");
	    }
	    customConfig = YamlConfiguration.loadConfiguration(customConfigurationFile);

	    java.io.InputStream defConfigStream = mainClass.getResource("messages.yml");
	    if (defConfigStream != null) {
		    Reader reader = new InputStreamReader(defConfigStream);
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(reader);
	        customConfig.setDefaults(defConfig);
	    }
	}
	
	public FileConfiguration getMessageFetcher() {
	    if (customConfig == null) {
	    	this.reloadMessageFile();
	    }
	    return customConfig;
	}
	
	public void saveMessageFile() {
	    if (customConfig == null || customConfigurationFile == null) {
	    return;
	    }
	    try {
	        customConfig.save(customConfigurationFile);
	    } catch (IOException ex) {
	        Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Theres an issue with " + customConfigurationFile + " please fix it!", ex);
	    }
	}
	
	public String getMessage (String messageID, boolean prefixreplace){
		String msg = getMessageFetcher().getString(messageID);
		if (msg != null){
			if (prefixreplace){
				msg =  msg.replace("%prefix%", getMessage("prefix" , false));
			}
			return ChatColor.translateAlternateColorCodes('&',msg);
		}
		else{
			return "Your custom messages.yml doesn't contain this key ("+messageID+")";
		}
	}
}
