package com.elytraforce.mttt2.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;
import org.bukkit.generator.ChunkGenerator.ChunkData;

import com.elytraforce.mttt2.Main;

public class WorldUtils {
	public static World createEmptyWorld(String name) {
		WorldCreator worldCreator = new WorldCreator(name);
		worldCreator.environment(World.Environment.NORMAL);
		worldCreator.generateStructures(false);
		worldCreator.generator(new ChunkGenerator() {
			@Override
			public List<BlockPopulator> getDefaultPopulators(World world) {
				return Arrays.asList(new BlockPopulator[0]);
			}

			@Override
			public boolean canSpawn(World world, int x, int z) {
				return true;
			}

			public byte[] generate(World world, Random random, int x, int z) {
				return new byte[32768];
			}
		
			public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
				return createChunkData(world);
				
			}

			@Override
			public Location getFixedSpawnLocation(World world, Random random) {
				return new Location(world, 0.0D, 64.0D, 0.0D);
			}
		});

		World world = worldCreator.createWorld();
		world.setDifficulty(Difficulty.NORMAL);
		world.setSpawnFlags(true, true);
		world.setPVP(true);
		world.setStorm(false);
		world.setThundering(false);
		world.setWeatherDuration(Integer.MAX_VALUE);
		world.setAutoSave(false);
		world.setKeepSpawnInMemory(false);
		world.setTicksPerAnimalSpawns(1);
		world.setTicksPerMonsterSpawns(1);
		world.setTime(1000);

		world.setGameRuleValue("doMobSpawning", "false");
		world.setGameRuleValue("mobGriefing", "false");
		world.setGameRuleValue("doDaylightCycle", "false");
		world.setGameRuleValue("doFireTick", "false");
		world.setGameRuleValue("showDeathMessages", "false");

		Block b = world.getBlockAt(0, 1, 0);
		b.setType(Material.GLASS);
		return world;
	}

	public static boolean loadWorld(String worldName) {
		WorldCreator worldCreator = new WorldCreator(worldName);
		worldCreator.generateStructures(false);
		worldCreator.generator(new ChunkGenerator() {
			@Override
			public List<BlockPopulator> getDefaultPopulators(World world) {
				return Arrays.asList(new BlockPopulator[0]);
			}

			@Override
			public boolean canSpawn(World world, int x, int z) {
				return true;
			}

			public byte[] generate(World world, Random random, int x, int z) {
				return new byte[32768];
			}
		
			public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
				return createChunkData(world);
				
			}

			@Override
			public Location getFixedSpawnLocation(World world, Random random) {
				return new Location(world, 0.0D, 64.0D, 0.0D);
			}
		});
		World world = worldCreator.createWorld();
		world.setDifficulty(Difficulty.NORMAL);
		world.setSpawnFlags(true, true);
		world.setStorm(false);
		world.setThundering(false);
		world.setPVP(true);
		world.setWeatherDuration(Integer.MAX_VALUE);
		world.setAutoSave(false);
		world.setKeepSpawnInMemory(false);
		world.setTicksPerAnimalSpawns(1);
		world.setTicksPerMonsterSpawns(1);
		world.setTime(1000);
		
		world.setGameRuleValue("doMobSpawning", "false");
		world.setGameRuleValue("mobGriefing", "false");
		world.setGameRuleValue("doFireTick", "false");
		world.setGameRuleValue("showDeathMessages", "false");
		world.setGameRuleValue("doDaylightCycle", "false");

		boolean loaded = false;
		for (World w : Main.getMain().getServer().getWorlds()) {
			if (w.getName().equals(world.getName())) {
				loaded = true;
				break;
			}
		}
		return loaded;
	}

	public static void unloadWorld(String w, boolean save) {
	    World world = Main.getMain().getServer().getWorld(w);
	    if(world != null) {
	    	Main.getMain().getServer().unloadWorld(world, save);
	    }
	}



	public static void copyWorld(File source, File target){
		try {
			ArrayList<String> ignore = new ArrayList<>(Arrays.asList("uid.dat", "session.dat", "session.lock"));
			if(!ignore.contains(source.getName())) {
				if(source.isDirectory()) {
					if(!target.exists())
						if (target.mkdirs()) {
							String[] files = source.list();
							if (files != null) {
								for (String file : files) {
									File srcFile = new File(source, file);
									File destFile = new File(target, file);
									copyWorld(srcFile, destFile);
								}
							}
						}
				} else {
					InputStream in = new FileInputStream(source);
					OutputStream out = new FileOutputStream(target);
					byte[] buffer = new byte[1024];
					int length;
					while ((length = in.read(buffer)) > 0)
						out.write(buffer, 0, length);
					in.close();
					out.close();
				}
			}
		} catch (IOException e) {
			Main.getMain().getLogger().info("Failed to copy world!");
		}
	}
	
	public static void deleteWorld(String name) {
		unloadWorld(name, false);
		File target = new File (Main.getMain().getServer().getWorldContainer().getAbsolutePath(), name);
		deleteWorld(target);
	}

	public static void deleteWorld(File path) {
		if(path.exists()) {
			File[] files = path.listFiles();
			if (files != null) {
				for (File file: files) {
					if(file.isDirectory()) {
						deleteWorld(file);
					} else {
						file.delete();
					}
				}
			}
		}
		path.delete();
	}

	public static void deleteWorldGuard(String name) {
		File f = new File(Main.getMain().getServer().getWorldContainer().getAbsolutePath());
		f = new File(f, "/plugins");
		for(File file : f.listFiles()) {
			if(!file.getName().equalsIgnoreCase("WorldGuard")) {
				return;
			}
		}
		f = new File(f, "/plugins/WorldGuard/worlds/");
		if (f != null) {
			File[] c = f.listFiles();
			for (File file : c) {
				if (!file.isDirectory() || !file.getName().matches(name)) {
					continue;
				}
				deleteWorld(file);
			}
		}
	}

}
