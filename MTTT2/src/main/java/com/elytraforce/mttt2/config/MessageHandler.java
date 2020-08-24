package main.java.com.elytraforce.mttt2.config;

import java.io.File;

import java.io.IOException;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import main.java.com.elytraforce.mttt2.Main;

public class MessageHandler {
	FileConfiguration customConfig = null;
	File customConfigurationFile = null;
	Main mainClass;
	
	public MessageHandler(Main main){
		this.mainClass = main;
		this.createCustomConfig();
	}
	
	public FileConfiguration getCustomConfig() {
        return this.customConfig;
    }

    public void createCustomConfig() {
    	mainClass.printDebugLine("[MTTT2] Initializing messages.yml");
        customConfigurationFile = new File(mainClass.getDataFolder(), "messages.yml");
        if (!customConfigurationFile.exists()) {
            customConfigurationFile.getParentFile().mkdirs();
            mainClass.saveResource("messages.yml", false);
         }

        customConfig= new YamlConfiguration();
        try {
			customConfig.load(customConfigurationFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}

    }
	
	public FileConfiguration getMessageFetcher() {
	    if (!customConfigurationFile.exists()) {
	    	createCustomConfig();
	    }
	    return this.customConfig;
	}
	
	
	public String getMessage(String messageID, boolean prefixreplace){
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
