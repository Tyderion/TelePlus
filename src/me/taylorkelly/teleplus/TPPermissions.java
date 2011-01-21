package me.taylorkelly.teleplus;

import org.bukkit.entity.Player;

public class TPPermissions {
    public static boolean playerCanJump(Player player) {
        return TPSettings.nonAdminsCanJump || player.isOp();
    }
    
    public static boolean playerCanTP(Player player) {
        return TPSettings.nonAdminsCanTP || player.isOp();
    }
    
    public static boolean playerCanTPOthers(Player player) {
        return (TPSettings.nonAdminsCanTPOthers && TPSettings.nonAdminsCanTP) || player.isOp();
    }
    
    public static boolean playerCanTPToOthers(Player player) {
        return (TPSettings.nonAdminsCanTPToOthers && TPSettings.nonAdminsCanTP) || player.isOp();
    }
}
