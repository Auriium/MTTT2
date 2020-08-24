package main.java.com.elytraforce.mttt2.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

import main.java.com.elytraforce.mttt2.Main;
import main.java.com.elytraforce.mttt2.enums.GameStateEnum;
import main.java.com.elytraforce.mttt2.objects.Manager;
import main.java.com.elytraforce.mttt2.objects.arena.Arena;


public class TTTCommand implements CommandExecutor{

	private Main mainClass;
	
	public TTTCommand(Main main) {
		this.mainClass = main;
	}
	
	@Override
	//debug command
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		Player player = (Player) sender;
		//make this better later but tbh im too tired to atm
		if (args.length == 0) {
			player.sendMessage(parseColor("&c&lMTTT2&7 by Aurium_"));
			player.sendMessage("");
			player.sendMessage(parseColor("&7Usage:"));
			player.sendMessage(parseColor("&7/ttt join &c<numerical>"));   
			player.sendMessage(parseColor("&7/ttt createMap &c<map_id>"));
			player.sendMessage("");
			return true;
		}
		
		if (args[0].equals("join")) {
			Arena currentArena = Manager.getInstance().getArenas().get(Integer.parseInt(args[1]));

			if (!currentArena.getArenaState().equals(GameStateEnum.WAITING) || !currentArena.getArenaState().equals(GameStateEnum.COUNTDOWN)  ) {
				
				if (currentArena.containsPlayer((Player)sender)) {
					//already in a match
					return true;
				}
				currentArena.addPlayer((Player)sender);
				return true;
			}
		}

		if (args[0].equalsIgnoreCase("createMap")) {
			if (!(args.length == 2)) {
				//not enough args!
				sender.sendMessage(mainClass.getMessageHandler().getMessage("prefix", false) + parseColor(
						"&cIncorrect arguments!"));
				return true;
			}
			
			if (mainClass.getMapConfigHandler().getMapSection().contains(args[1])) {
				sender.sendMessage(mainClass.getMessageHandler().getMessage("prefix", false) + parseColor(
						"&cMap already exists!"));
				return true;
			}
			
			if (!mainClass.getMapConfigHandler().getMapSection().contains(args[1])) {
				
				String mapName = args[1];
				mainClass.getMapConfigHandler().createGenericMap(mapName);
				sender.sendMessage(mainClass.getMessageHandler().getMessage("prefix", false) + parseColor(
						"&cSuccessfully (?) created map! Use commands /ttt setSpawn, setTester(useless), and"
						+ "addGunLocation to complete the map!"));
				return true;
			}
		}
		
		
		
		
		return false;
	}
	
	public String parseColor(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}

}
