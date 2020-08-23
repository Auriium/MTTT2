package main.java.com.elytraforce.mttt2.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		Arena currentArena = Manager.getInstance().getArenas().get(Integer.parseInt(args[0]));

		if (!currentArena.getArenaState().equals(GameStateEnum.WAITING) || !currentArena.getArenaState().equals(GameStateEnum.COUNTDOWN)  ) {
			
			currentArena.addPlayer((Player)sender);
			return true;
		}
		return false;
	}

}
