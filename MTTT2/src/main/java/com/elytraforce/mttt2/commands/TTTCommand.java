package main.java.com.elytraforce.mttt2.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

import main.java.com.elytraforce.mttt2.Main;
import main.java.com.elytraforce.mttt2.enums.GameStateEnum;
import main.java.com.elytraforce.mttt2.objects.Manager;
import main.java.com.elytraforce.mttt2.objects.MapObject;
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
		Location playerLocation = player.getLocation();
		
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
		
		if (args[0].equalsIgnoreCase("join")) {
			Arena currentArena = null;
			try {
				 currentArena = Manager.getInstance().getArenas().get(Integer.parseInt(args[1]));
			} catch (NullPointerException e) {
				player.sendMessage(mainClass.getMessageHandler().getMessage("prefix", false) + parseColor(
						"&cNo maps have been set up. If you are not a staff member and are seeing this,"
						+ " please report this to the staff team!"));
				return true;
			} catch (IndexOutOfBoundsException e) {
				player.sendMessage(mainClass.getMessageHandler().getMessage("prefix", false) + parseColor(
						"&cNo maps have been set up. If you are not a staff member and are seeing this,"
						+ " please report this to the staff team!"));
				return true;
			}
			

			if (!currentArena.getArenaState().equals(GameStateEnum.WAITING) || !currentArena.getArenaState().equals(GameStateEnum.COUNTDOWN)  ) {
				
				if (currentArena.containsPlayer(player)) {
					player.sendMessage(mainClass.getMessageHandler().getMessage("prefix", false) + parseColor(
							"&cYou are already in a match!"));
					return true;
				}
				currentArena.addPlayer(player);
				return true;
			}
			return true;
		}

		if (args[0].equalsIgnoreCase("createMap")) {
			if (!(args.length == 2)) {
				//not enough args!
				player.sendMessage(mainClass.getMessageHandler().getMessage("prefix", false) + parseColor(
						"&cIncorrect arguments!"));
				return true;
			}
			
			if (mainClass.getMapConfigHandler().getMapSection().contains(args[1])) {
				player.sendMessage(mainClass.getMessageHandler().getMessage("prefix", false) + parseColor(
						"&cMap already exists!"));
				return true;
			}
			
			if (!mainClass.getMapConfigHandler().getMapSection().contains(args[1])) {
				
				String mapName = args[1];
				new MapObject(mainClass, mapName);
				player.sendMessage(mainClass.getMessageHandler().getMessage("prefix", false) + parseColor(
						"&cSuccessfully created map! Use commands &7/ttt setSpawn, /ttt setTester, and"
						+ "/ttt addGunLocation &cto complete the map!"));
				return true;
			}
			return true;
		}
		
		if (args[0].equalsIgnoreCase("setSpawn")) {
			if (!(args.length == 2)) {
				//not enough args!
				player.sendMessage(mainClass.getMessageHandler().getMessage("prefix", false) + parseColor(
						"&cIncorrect arguments!"));
				return true;
			}
			
			if (!mainClass.getMapConfigHandler().getMapSection().contains(args[1])) {
				player.sendMessage(mainClass.getMessageHandler().getMessage("prefix", false) + parseColor(
						"&cMap does not exist!"));
				return true;
			}
			
			if (mainClass.getMapConfigHandler().getMapSection().contains(args[1])) {
				
				String mapName = args[1];
				
				mainClass.getMapConfigHandler().getMapFromString(mapName).setSpawn(playerLocation);
				
				player.sendMessage(mainClass.getMessageHandler().getMessage("prefix", false) + parseColor(
						"&cSet spawn location for map &7" + mapName + " &c!"));
				return true;
			}
			return true;
		}
		
		if (args[0].equalsIgnoreCase("setTester")) {
			if (!(args.length == 2)) {
				//not enough args!
				player.sendMessage(mainClass.getMessageHandler().getMessage("prefix", false) + parseColor(
						"&cIncorrect arguments!"));
				return true;
			}
			
			if (!mainClass.getMapConfigHandler().getMapSection().contains(args[1])) {
				player.sendMessage(mainClass.getMessageHandler().getMessage("prefix", false) + parseColor(
						"&cMap does not exist!"));
				return true;
			}
			
			if (mainClass.getMapConfigHandler().getMapSection().contains(args[1])) {
				
				String mapName = args[1];
				
				mainClass.getMapConfigHandler().getMapFromString(mapName).setTester(playerLocation);
				
				player.sendMessage(mainClass.getMessageHandler().getMessage("prefix", false) + parseColor(
						"&cSet tester location for map &7" + mapName + " &c!"));
				return true;
			}
			return true;
		}
		
		if (args[0].equalsIgnoreCase("addGunLocation")) {
			if (!(args.length == 2)) {
				//not enough args!
				player.sendMessage(mainClass.getMessageHandler().getMessage("prefix", false) + parseColor(
						"&cIncorrect arguments!"));
				return true;
			}
			
			if (!mainClass.getMapConfigHandler().getMapSection().contains(args[1])) {
				player.sendMessage(mainClass.getMessageHandler().getMessage("prefix", false) + parseColor(
						"&cMap does not exist!"));
				return true;
			}
			
			if (mainClass.getMapConfigHandler().getMapSection().contains(args[1])) {
				
				String mapName = args[1];
				
				Integer nextLocation = mainClass.getMapConfigHandler().getMapFromString(mapName).addGunLocation(playerLocation);
				
				player.sendMessage(mainClass.getMessageHandler().getMessage("prefix", false) + parseColor(
						"&cAdded gun location &7 " + nextLocation + " &cfor map &7" + mapName + " &c!"));
				return true;
			}
			return true;
		}
		
		
		
		
		return false;
	}
	
	public String parseColor(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}

}
