package me.taylorkelly.teleplus;
import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;


public class TelePlus extends JavaPlugin {
	private TPPlayerListener playerListener;
	public static Logger log;
    public final String name = this.getDescription().getName();
    public final String version = this.getDescription().getVersion();
	public TelePlus(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File plugin, ClassLoader cLoader) {
		super(pluginLoader, instance, desc, plugin, cLoader);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEnable() {
	    HashMap<String, Boolean> jtoggle = new HashMap<String, Boolean>();
		playerListener = new TPPlayerListener(this, jtoggle);

		getServer().getPluginManager().registerEvent(Event.Type.PLAYER_COMMAND, playerListener, Priority.Normal, this);
        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_ITEM, playerListener, Priority.Normal, this);

        //ARM SWING Hook
		log = Logger.getLogger("Minecraft");
		log.info(name + " " + version + " enabled");
	}

}
