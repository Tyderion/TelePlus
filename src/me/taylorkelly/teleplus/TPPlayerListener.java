package me.taylorkelly.teleplus;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Block;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Player;
import org.bukkit.World;
import org.bukkit.event.block.BlockRightClickedEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerItemEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.plugin.Plugin;

public class TPPlayerListener extends PlayerListener {
    private Plugin plugin;
    private HashMap<String, Boolean> jtoggle;

    public TPPlayerListener(Plugin plugin, HashMap<String, Boolean> jtoggle) {
        this.plugin = plugin;
        this.jtoggle = jtoggle;
    }

    public void onPlayerItem(PlayerItemEvent event) {
        Player player = event.getPlayer();
        if (jtoggle.containsKey(player.getName()) && jtoggle.get(player.getName())) {
            player.sendMessage(ChatColor.GRAY + "quick jump.");
            AimBlock aiming = new AimBlock(player);
            Block block = aiming.getTargetBlock();
            if (block == null) {
                player.sendMessage(ChatColor.RED + "Not pointing to valid block");
            } else {
                int x = block.getX();
                int y = block.getY() + 1;
                int z = block.getZ();
                World world = block.getWorld();
                Location loc = new Location(world, x, y, z, player.getLocation().getYaw(), player.getLocation().getPitch());
                Teleporter tp = new Teleporter(loc);
                tp.setVerbose(false);
                tp.addTeleportee(player);
                tp.teleport();
            }
        }
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
                World currentWorld = player.getWorld();
                Location loc = new Location(currentWorld, Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), player
                        .getLocation().getYaw(), player.getLocation().getPitch());
                Teleporter tp = new Teleporter(loc);
                tp.addTeleportee(player);
                tp.teleport();
                /**
                 * /tp <world>
                 */
            } else if (split.length == 2 && isInteger(split[1])) {
                if (!validWorld(Integer.parseInt(split[1]))) {
                    player.sendMessage(ChatColor.RED + "Not a valid world.");
                } else {
                    World currentWorld = plugin.getServer().getWorlds()[Integer.parseInt(split[1])];
                    Location loc = new Location(currentWorld, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player
                            .getLocation().getYaw(), player.getLocation().getPitch());
                    Teleporter tp = new Teleporter(loc);
                    tp.addTeleportee(player);
                    tp.teleport();
                }
                /**
                 * /tp up
                 */
            } else if (split.length == 2 && split[1].equalsIgnoreCase("up")) {
                int y = player.getWorld().getHighestBlockYAt(player.getLocation().getBlockX(), player.getLocation().getBlockZ());
                Location loc = new Location(player.getWorld(), player.getLocation().getX(), y, player.getLocation().getZ(), player.getLocation().getYaw(),
                        player.getLocation().getPitch());
                Teleporter tp = new Teleporter(loc);
                tp.setVerbose(false);
                tp.addTeleportee(player);
                tp.teleport();
                /**
                 * /tp jump
                 */
            } else if (split.length == 2 && split[1].equalsIgnoreCase("jump")) {
                AimBlock aiming = new AimBlock(player);
                Block block = aiming.getTargetBlock();
                if (block == null) {
                    player.sendMessage(ChatColor.RED + "Not pointing to valid block");
                } else {
                    int x = block.getX();
                    int y = block.getY() + 1;
                    int z = block.getZ();
                    World world = block.getWorld();
                    Location loc = new Location(world, x, y, z, player.getLocation().getYaw(), player.getLocation().getPitch());
                    Teleporter tp = new Teleporter(loc);
                    tp.setVerbose(false);
                    tp.addTeleportee(player);
                    tp.teleport();
                }
                /**
                 * /tp back
                 */
            } else if (split.length == 2 && split[1].equalsIgnoreCase("back")) {
                Location location = TeleHistory.popLocation(player);
                if (location == null) {
                    player.sendMessage(ChatColor.RED + "No locations in your teleport history.");
                } else {
                    player.teleportTo(location);
                }
                /**
                 * /tp qjump
                 */
            } else if (split.length == 2 && split[1].equalsIgnoreCase("qjump")) {
                if (!jtoggle.containsKey(player.getName()) || !jtoggle.get(player.getName())) {
                    jtoggle.put(player.getName(), true);
                    player.sendMessage(ChatColor.AQUA + "Quick jump is on.");
                } else {
                    jtoggle.put(player.getName(), false);
                    player.sendMessage(ChatColor.AQUA + "Quick jump is off.");
                }
                /**
                 * /tp <player>
                 */
            } else if (split.length == 2) {
                List<Player> targets = plugin.getServer().matchPlayer(split[1]);
                if(targets.size() == 1) {
                    Player target = targets.get(0);
                    Location loc = new Location(target.getWorld(), target.getLocation().getX(), target.getLocation().getY(), target.getLocation().getZ(),
                            target.getLocation().getYaw(), target.getLocation().getPitch());
                    Teleporter tp = new Teleporter(loc);
                    tp.addTeleportee(player);
                    tp.setVerbose(false);
                    tp.teleport();
                } else {
                    player.sendMessage(ChatColor.RED + split[1] + " did not match a player, cancelling teleport");
                }
                /**
                 * /tp <world> <x> <y> <z>
                 */
            } else if (split.length == 5 && isInteger(split[1]) && isNumber(split[2]) && isNumber(split[3]) && isNumber(split[4])) {
                if (!validWorld(Integer.parseInt(split[1]))) {
                    player.sendMessage(ChatColor.RED + "Not a valid world.");
                } else {
                    World currentWorld = plugin.getServer().getWorlds()[Integer.parseInt(split[1])];
                    Location loc = new Location(currentWorld, Double.parseDouble(split[2]), Double.parseDouble(split[3]), Double.parseDouble(split[4]), player
                            .getLocation().getYaw(), player.getLocation().getPitch());
                    Teleporter tp = new Teleporter(loc);
                    tp.addTeleportee(player);
                    tp.teleport();
                }
                /**
                 * /tp here <player1> <player2> <player3>
                 */
            } else if (split.length > 2 && split[1].equalsIgnoreCase("here")) {
                Location loc = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player
                        .getLocation().getYaw(), player.getLocation().getPitch());
                Teleporter tp = new Teleporter(loc);
                tp.setVerbose(false);
                if (split.length == 3 && split[1].equalsIgnoreCase("*")) {
                    tp.addAll(plugin.getServer().getOnlinePlayers());
                } else {
                    for (int i = 2; i < split.length; i++) {
                        List<Player> targets = plugin.getServer().matchPlayer(split[i]);
                        if(targets.size() == 1) {
                            Player teleportee = targets.get(0);
                            tp.addTeleportee(teleportee);
                        } else {
                            player.sendMessage(ChatColor.RED + split[i] + " did not match a player");
                        }
                    }
                }
                tp.teleport();

                /**
                 * /tp to <target> <player1> <player2> ...
                 */
            } else if (split.length > 3 && split[1].equalsIgnoreCase("to")) {
                List<Player> targets = plugin.getServer().matchPlayer(split[2]);
                if(targets.size() == 1) {
                    Player target = targets.get(0);
                    Location loc = new Location(target.getWorld(), target.getLocation().getX(), target.getLocation().getY(), target.getLocation().getZ(),
                            target.getLocation().getYaw(), target.getLocation().getPitch());
                    Teleporter tp = new Teleporter(loc);
                    tp.setVerbose(false);
                    if (split.length == 4 && split[1].equalsIgnoreCase("*")) {
                        tp.addAll(plugin.getServer().getOnlinePlayers());
                    } else {
                        for (int i = 3; i < split.length; i++) {
                            targets = plugin.getServer().matchPlayer(split[i]);
                            if (targets.size() == 1) {
                                Player teleportee = targets.get(0);
                                tp.addTeleportee(teleportee);
                            } else {
                                player.sendMessage(ChatColor.RED + split[i] + " did not match a player");
                            }
                        }
                    }
                    tp.teleport();
                } else {
                    player.sendMessage(ChatColor.RED + split[2] + " did not match a player, cancelling teleport");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Invalid /tp command");
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
