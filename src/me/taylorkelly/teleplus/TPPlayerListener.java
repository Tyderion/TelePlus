package me.taylorkelly.teleplus;

import java.util.HashMap;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.World;
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
            event.setCancelled(true);
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
}
