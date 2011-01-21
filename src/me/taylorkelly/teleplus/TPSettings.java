package me.taylorkelly.teleplus;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.bukkit.util.config.Configuration;

public class TPSettings {
    
    private static final String settingsFile = "TelePlus.yml";
    
    public static boolean nonAdminsCanJump;
    public static boolean nonAdminsCanTP;
    public static boolean nonAdminsCanTPToOthers;
    public static boolean nonAdminsCanTPOthers;

    
    public static void initialize(File dataFolder) {
        if(!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        File configFile  = new File(dataFolder, settingsFile);
        if(!configFile.exists()) {
            createSettingsFile(configFile);
        }
        Configuration config = new Configuration(configFile);
        config.load();
        nonAdminsCanJump = config.getBoolean("nonAdminsCanJump", true);
        nonAdminsCanTP = config.getBoolean("nonAdminsCanTP", true);
        nonAdminsCanTPToOthers = config.getBoolean("nonAdminsCanTPToOthers", false);
        nonAdminsCanTPOthers = config.getBoolean("nonAdminsCanTPOthers", false);

    }

    private static void createSettingsFile(File configFile) {
        BufferedWriter bwriter = null;
        FileWriter fwriter = null;
        try {
            configFile.createNewFile();
            fwriter = new FileWriter(configFile, true);
            bwriter = new BufferedWriter(fwriter);
            bwriter.write("nonAdminsCanJump: true");
            bwriter.newLine();
            bwriter.write("nonAdminsCanTP: true");
            bwriter.newLine();
            bwriter.write("nonAdminsCanTPToOthers: false");
            bwriter.newLine();
            bwriter.write("nonAdminsCanTPOthers: false");
            bwriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bwriter != null) {
                    bwriter.close();
                }
                if (fwriter != null)
                    fwriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
