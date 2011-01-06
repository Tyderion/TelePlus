package me.taylorkelly.teleplus;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Player;
import org.bukkit.World;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.plugin.Plugin;

public class TPPlayerListener extends PlayerListener {
	private Plugin plugin;

	public TPPlayerListener(Plugin plugin) {
		this.plugin = plugin;
	}

	public void onPlayerCommand(PlayerChatEvent event) {
		String[] split = event.getMessage().split(" ");
		Player player = event.getPlayer();

		// TODO permissions
		if (split[0].equalsIgnoreCase("/tp")) {
			event.setCancelled(true);
			/**
			 * /tp <x> <y> <z>
			 */
			if (split.length == 4 && isNumber(split[1]) && isNumber(split[2]) && isNumber(split[3])) {
				// TODO ChunkLoading
				World currentWorld = player.getWorld();
				Location loc = new Location(currentWorld, Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]));
				Teleporter tp = new Teleporter(loc);
				tp.addTeleportee(player);
				tp.teleport();
				/**
				 * /tp <world>
				 */
			} else if (split.length == 2 && isInteger(split[1])) {
				// TODO ChunkLoading
				if (!validWorld(Integer.parseInt(split[1]))) {
					player.sendMessage(Color.RED + "Not a valid world.");
				} else {
					World currentWorld = plugin.getServer().getWorlds()[Integer.parseInt(split[1])];
					Location loc = new Location(currentWorld, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
					Teleporter tp = new Teleporter(loc);
					tp.addTeleportee(player);
					tp.teleport();
				}
				/**
				 * /tp up
				 */
			} else if (split.length == 2 && split[1].equalsIgnoreCase("up")) {
				int y = player.getWorld().getHighestBlockYAt(player.getLocation().getBlockX(), player.getLocation().getBlockZ());
				Location loc = new Location(player.getWorld(), player.getLocation().getX(), y, player.getLocation().getZ());
				Teleporter tp = new Teleporter(loc);
				tp.setVerbose(false);
				tp.addTeleportee(player);
				tp.teleport();
				/**
				 * /tp <player>
				 */
			} else if (split.length == 2) {
				// TODO matchPlayer
				Player target = plugin.getServer().getPlayer(split[1]);
				if (target != null) {
					Location loc = new Location(target.getWorld(), target.getLocation().getX(), target.getLocation().getY(), target.getLocation().getZ());
					Teleporter tp = new Teleporter(loc);
					tp.addTeleportee(player);
					tp.setVerbose(false);
					tp.teleport();
				} else {
					player.sendMessage(Color.RED + split[1] + " is not a player, cancelling teleport");
				}
				/**
				 * /tp <world> <x> <y> <z>
				 */
			} else if (split.length == 5 && isInteger(split[1]) && isNumber(split[2]) && isNumber(split[3]) && isNumber(split[4])) {
				// TODO ChunkLoading
				if (!validWorld(Integer.parseInt(split[1]))) {
					player.sendMessage(Color.RED + "Not a valid world.");
				} else {
					World currentWorld = plugin.getServer().getWorlds()[Integer.parseInt(split[1])];
					Location loc = new Location(currentWorld, Double.parseDouble(split[2]), Double.parseDouble(split[3]), Double.parseDouble(split[4]));
					Teleporter tp = new Teleporter(loc);
					tp.addTeleportee(player);
					tp.teleport();
				}
				/**
				 * /tp here <player1> <player2> <player3>
				 */
			} else if (split.length > 2 && split[1].equalsIgnoreCase("here")) {
				Location loc = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
				Teleporter tp = new Teleporter(loc);
				tp.setVerbose(false);
				for (int i = 2; i < split.length; i++) {
					// TODO better player matching
					Player teleportee = plugin.getServer().getPlayer(split[i]);
					if (teleportee != null) {
						tp.addTeleportee(teleportee);
					} else {
						player.sendMessage(Color.RED + split[i] + " is not a player");
					}
				}
				tp.teleport();

				/**
				 * /tp to <target> <player1> <player2> ...
				 */
			} else if (split.length > 3 && split[1].equalsIgnoreCase("to")) {
				Player target = plugin.getServer().getPlayer(split[2]);
				if (target != null) {
					Location loc = new Location(target.getWorld(), target.getLocation().getX(), target.getLocation().getY(), target.getLocation().getZ());
					Teleporter tp = new Teleporter(loc);
					tp.setVerbose(false);
					for (int i = 3; i < split.length; i++) {
						// TODO better player matching
						Player teleportee = plugin.getServer().getPlayer(split[i]);
						if (teleportee != null) {
							tp.addTeleportee(teleportee);
						} else {
							player.sendMessage(Color.RED + split[i] + " is not a player");
						}
					}
					tp.teleport();
				} else {
					player.sendMessage(Color.RED + split[2] + " is not a player, cancelling teleport");
				}
			} else {
				player.sendMessage(Color.RED + "Invalid /tp command");
			}
		}
	}

	private boolean validWorld(int worldIndex) {
		if (worldIndex < 0)
			return false;
		if (worldIndex >= plugin.getServer().getWorlds().length)
			return false;
		return true;
	}

	public static boolean isNumber(String string) {
		try {
			Double.parseDouble(string);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean isInteger(String string) {
		try {
			Integer.parseInt(string);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static double distance(Location from, Location to) {
		return Math.sqrt(Math.pow(from.getX() - to.getX(), 2) + Math.pow(from.getY() - to.getY(), 2) + Math.pow(from.getZ() - to.getZ(), 2));
	}
}
