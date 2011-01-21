package me.taylorkelly.teleplus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;

public class TelePlus extends JavaPlugin {
    private TPPlayerListener playerListener;
    public static Logger log;
    public final String name = this.getDescription().getName();
    public final String version = this.getDescription().getVersion();
    private HashMap<String, Boolean> jtoggle;

    public TelePlus(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File folder, File plugin, ClassLoader cLoader) {
        super(pluginLoader, instance, desc, folder, plugin, cLoader);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onDisable() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEnable() {
        TPSettings.initialize(getDataFolder());
        jtoggle = new HashMap<String, Boolean>();
        playerListener = new TPPlayerListener(this, jtoggle);

        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_ITEM, playerListener, Priority.Normal, this);

        // ARM SWING Hook
        log = Logger.getLogger("Minecraft");
        log.info(name + " " + version + " enabled");
    }

    public boolean onCommand(Player player, Command command, String commandLabel, String[] args) {
        String[] split = args;
        String commandName = command.getName().toLowerCase();
        
        // TODO permissions
        if (commandName.equals("tp")) {
            /**
             * /tp <x> <y> <z>
             */
            if (split.length == 3 && isNumber(split[0]) && isNumber(split[1]) && isNumber(split[2]) && TPPermissions.playerCanTP(player)) {
                World currentWorld = player.getWorld();
                Location loc = new Location(currentWorld, Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), player
                        .getLocation().getYaw(), player.getLocation().getPitch());
                Teleporter tp = new Teleporter(loc);
                tp.addTeleportee(player);
                tp.teleport();
                /**
                 * /tp <world>
                 */
            } else if (split.length == 1 && isInteger(split[0]) && TPPermissions.playerCanTP(player)) {
                if (!validWorld(Integer.parseInt(split[0]))) {
                    player.sendMessage(ChatColor.RED + "Not a valid world.");
                } else {
                    World currentWorld = getServer().getWorlds()[Integer.parseInt(split[0])];
                    Location loc = new Location(currentWorld, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player
                            .getLocation().getYaw(), player.getLocation().getPitch());
                    Teleporter tp = new Teleporter(loc);
                    tp.addTeleportee(player);
                    tp.teleport();
                }
                /**
                 * /tp up
                 */
            } else if (split.length == 1 && split[0].equalsIgnoreCase("up") && TPPermissions.playerCanTP(player)) {
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
            } else if (split.length == 1 && split[0].equalsIgnoreCase("jump") && TPPermissions.playerCanJump(player)) {
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
            } else if (split.length == 1 && split[0].equalsIgnoreCase("back")) {
                Location location = TeleHistory.popLocation(player);
                if (location == null) {
                    player.sendMessage(ChatColor.RED + "No locations in your teleport history.");
                } else {
                    player.teleportTo(location);
                }
                /**
                 * /tp clear
                 */
            } else if (split.length == 1 && split[0].equalsIgnoreCase("clear")) {
                if (TeleHistory.clearHistory(player)) {
                    player.sendMessage(ChatColor.AQUA + "Your history has been cleared");
                } else {
                    player.sendMessage(ChatColor.RED + "No locations in your history to clear.");
                }
                /**
                 * /tp origin
                 */
            } else if (split.length == 1 && split[0].equalsIgnoreCase("origin")) {
                Location location = TeleHistory.origin(player);
                if (location == null) {
                    player.sendMessage(ChatColor.RED + "No locations in your teleport history.");
                } else {
                    player.teleportTo(location);
                }
                /**
                 * /tp qjump
                 */
            } else if (split.length == 1 && split[0].equalsIgnoreCase("qjump") && TPPermissions.playerCanJump(player)) {
                if (!jtoggle.containsKey(player.getName()) || !jtoggle.get(player.getName())) {
                    jtoggle.put(player.getName(), true);
                    player.sendMessage(ChatColor.AQUA + "Quick jump is on.");
                } else {
                    jtoggle.put(player.getName(), false);
                    player.sendMessage(ChatColor.AQUA + "Quick jump is off.");
                }
                /**
                 * /tp help
                 */
            } else if (split.length == 1 && split[0].equalsIgnoreCase("help")) {
                ArrayList<String> messages = new ArrayList<String>();
                messages.add(ChatColor.RED + "-------------------- " + ChatColor.WHITE + "/TP HELP" + ChatColor.RED + " --------------------");
                messages.add(ChatColor.RED + "/tp <player>" + ChatColor.WHITE + "  -  Teleport to " + ChatColor.GRAY + "<player>");
                messages.add(ChatColor.RED + "/tp <x> <y> <z>" + ChatColor.WHITE + "  -  Teleport to " + ChatColor.GRAY + "<x> <y> <z>");
                messages.add(ChatColor.RED + "/tp up" + ChatColor.WHITE + "  -  Teleports you to the block highest above you");
                messages.add(ChatColor.RED + "/tp jump" + ChatColor.WHITE + "  -  Teleports you to the block you're looking at");
                messages.add(ChatColor.RED + "/tp qjump" + ChatColor.WHITE + "  -  Toggles whether or not quick jump is on");
                messages.add(ChatColor.GOLD + "Quick Jump:" + ChatColor.WHITE + " Right click with an item in your hand");
                messages.add(ChatColor.RED + "/tp here <player> ... " + ChatColor.WHITE + "  -  Teleports 1+ players to you");
                messages.add(ChatColor.RED + "/tp to <target> <player> ... " + ChatColor.WHITE + "  -  Teleports player to target");
                messages.add(ChatColor.RED + "/tp here * " + ChatColor.WHITE + "  -  Teleports one or more players to you");
                messages.add(ChatColor.GOLD + "/tp back" + ChatColor.WHITE + "  -  Teleports you back to previous locations");
                messages.add(ChatColor.GOLD + "/tp origin" + ChatColor.WHITE + "  -  Go back to where you were before all tps");
                messages.add(ChatColor.GOLD + "/tp clear" + ChatColor.WHITE + "  -  Clear your entire tp history");
         
                for(String message: messages) {
                    player.sendMessage(message);
                }
                /**
                 * /tp <player>
                 */
            } else if (split.length == 1 && TPPermissions.playerCanTPToOthers(player)) {
                List<Player> targets = getServer().matchPlayer(split[0]);
                if (targets.size() == 1) {
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
            } else if (split.length == 4 && isInteger(split[0]) && isNumber(split[1]) && isNumber(split[2]) && isNumber(split[3])  && TPPermissions.playerCanTP(player)) {
                if (!validWorld(Integer.parseInt(split[0]))) {
                    player.sendMessage(ChatColor.RED + "Not a valid world.");
                } else {
                    World currentWorld = getServer().getWorlds()[Integer.parseInt(split[0])];
                    Location loc = new Location(currentWorld, Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), player
                            .getLocation().getYaw(), player.getLocation().getPitch());
                    Teleporter tp = new Teleporter(loc);
                    tp.addTeleportee(player);
                    tp.teleport();
                }
                /**
                 * /tp here <player1> <player2> <player3>
                 */
            } else if (split.length > 1 && split[0].equalsIgnoreCase("here") && TPPermissions.playerCanTPOthers(player)) {
                Location loc = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player
                        .getLocation().getYaw(), player.getLocation().getPitch());
                Teleporter tp = new Teleporter(loc);
                tp.setVerbose(false);
                if (split.length == 2 && split[1].equalsIgnoreCase("*")) {
                    tp.addAll(getServer().getOnlinePlayers());
                } else {
                    for (int i = 1; i < split.length; i++) {
                        List<Player> targets = getServer().matchPlayer(split[i]);
                        if (targets.size() == 1) {
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
            } else if (split.length > 2 && split[0].equalsIgnoreCase("to") && TPPermissions.playerCanTPOthers(player) && TPPermissions.playerCanTPToOthers(player)) {
                List<Player> targets = getServer().matchPlayer(split[1]);
                if (targets.size() == 1) {
                    Player target = targets.get(0);
                    Location loc = new Location(target.getWorld(), target.getLocation().getX(), target.getLocation().getY(), target.getLocation().getZ(),
                            target.getLocation().getYaw(), target.getLocation().getPitch());
                    Teleporter tp = new Teleporter(loc);
                    tp.setVerbose(false);
                    if (split.length == 3 && split[2].equalsIgnoreCase("*")) {
                        tp.addAll(getServer().getOnlinePlayers());
                    } else {
                        for (int i = 2; i < split.length; i++) {
                            targets = getServer().matchPlayer(split[i]);
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
               return false;
            }
            return true;
        }
        return false;
    }

    private boolean validWorld(int worldIndex) {
        if (worldIndex < 0)
            return false;
        if (worldIndex >= getServer().getWorlds().length)
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
