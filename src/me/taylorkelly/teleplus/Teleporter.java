package me.taylorkelly.teleplus;

import java.util.ArrayList;

import org.bukkit.*;
import org.bukkit.craftbukkit.CraftWorld;

public class Teleporter {
	private Location destination;
	private ArrayList<Player> players;
	private boolean verbose;
	
	public Teleporter(Location location) {
		this.destination = location;
		this.players = new ArrayList<Player>();
		verbose = true;
	}

	public void teleport() {
		World world = destination.getWorld();
		double x = destination.getX();
		double y = destination.getY();
		double z = destination.getZ();
		if (y < 1)
			y = 1;
		if(!((CraftWorld)world).getHandle().A.a(destination.getBlockX() >> 4, destination.getBlockZ() >> 4)) {	
		    ((CraftWorld)world).getHandle().A.d(destination.getBlockX() >> 4, destination.getBlockZ() >> 4);
		}

		while (blockIsAboveAir(world, x, y, z)) {
			y--;
		}
		while (!blockIsSafe(world, x, y, z)) {
			y++;
		}
		
		if(verbose) {
			if (destination.getY() != y) {
				for (Player player : players) {
					player.sendMessage("Supplied y location not safe.");
					player.sendMessage("Teleporting you to (" + (int) x + ", " + (int) y + ", " + (int) z + ")");
				}
			} else {
				for (Player player : players)
					player.sendMessage("Teleporting you to (" + (int) x + ", " + (int) y + ", " + (int) z + ")");
			}
		}
		for (Player player : players) {
			TeleHistory.pushLocation(player, player.getLocation());
			player.teleportTo(new Location(world, x, y, z, destination.getYaw(), destination.getPitch()));
		}
	}

	private boolean blockIsAboveAir(World world, double x, double y, double z) {
		return (world.getBlockAt((int) Math.floor(x), (int) Math.floor(y - 1), (int) Math.floor(z)).getType() == Material.Air);
	}

	public void addTeleportee(Player player) {
		players.add(player);

	}

	public boolean blockIsSafe(World world, double x, double y, double z) {
		return world.getBlockAt((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z)).getType() == Material.Air
				&& world.getBlockAt((int) Math.floor(x), (int) Math.floor(y + 1), (int) Math.floor(z)).getType() == Material.Air;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public void addAll(Player[] playerList) {
		for(Player player: playerList) {
			addTeleportee(player);
		}
	}
}
