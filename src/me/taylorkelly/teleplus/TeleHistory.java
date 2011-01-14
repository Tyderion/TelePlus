package me.taylorkelly.teleplus;

import org.bukkit.*;
import java.util.*;

public class TeleHistory {
	public static HashMap<String, LinkedList<Location>> history = new HashMap<String, LinkedList<Location>>();
	
	
	public static void pushLocation(Player player, Location location) {
		if(!history.containsKey(player.getName())) {
			history.put(player.getName(), new LinkedList<Location>());
		}
		history.get(player.getName()).addFirst(location);
	}
	
	public static Location popLocation(Player player) {
		if(!history.containsKey(player.getName())) return null;
		if(history.get(player.getName()).size() == 0) {
			return null;
		} else {
			return history.get(player.getName()).removeFirst();
		}
	}
}
