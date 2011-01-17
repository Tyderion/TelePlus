package me.taylorkelly.teleplus;

import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.*;

public class TeleHistory {
    public static HashMap<String, LinkedList<Location>> history = new HashMap<String, LinkedList<Location>>();

    public static void pushLocation(Player player, Location location) {
        if (!history.containsKey(player.getName())) {
            history.put(player.getName(), new LinkedList<Location>());
        }
        history.get(player.getName()).addFirst(location);
    }

    public static Location popLocation(Player player) {
        if (!history.containsKey(player.getName()))
            return null;
        if (history.get(player.getName()).size() == 0) {
            return null;
        } else {
            return history.get(player.getName()).removeFirst();
        }
    }

    public static boolean clearHistory(Player player) {
        if (!history.containsKey(player.getName()))
            return false;
        if (history.get(player.getName()).size() == 0) {
            return false;
        } else {
            history.get(player.getName()).clear();
            return true;
        }
    }

    public static Location origin(Player player) {
        if (!history.containsKey(player.getName()))
            return null;
        Location ret = history.get(player.getName()).removeLast();
        clearHistory(player);
        return ret;
    }
}
