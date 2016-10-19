package com.Drekryan.OreGen.util;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConfigManager {
    private Plugin plugin;
    private FileConfiguration config;

    public ConfigManager(Plugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    public void generateDefaultConfig() {
        try {
            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdirs();
            }
            File file = new File(plugin.getDataFolder(), "config.yml");
            if (!file.exists()) {
                plugin.getLogger().info("Config.yml not found, creating!");
                config.options().header("OreGen Plugin\n");
                ConfigurationSection blockValuesSection = config.createSection("Block Values");
                blockValuesSection.set("COBBLESTONE", 90);
                blockValuesSection.set("COAL_ORE", 10);

                config.options().copyDefaults(true);
                plugin.saveConfig();
            } else {
                plugin.getLogger().info("Config.yml found, loading!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, Integer> getBlockValues() {
        Map<String, Integer> map = new HashMap<>();

        try {
            config.load(plugin.getDataFolder() + File.separator + "config.yml");
        } catch (FileNotFoundException e) {
            this.generateDefaultConfig();

            try {
                config.load(plugin.getDataFolder() + File.separator + "config.yml");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ConfigurationSection blockValuesSection = config.getConfigurationSection("Block Values");
        Set<String> keys = blockValuesSection.getKeys(false);

        for(String key : keys) {
            int value = blockValuesSection.getInt(key);
            if (key != null) {
                System.out.println("[OreGen] Loaded: " + key + " | " + value);
                map.put(key, value);
            } else {
                System.out.println("[OreGen] Error: Got invalid key or value!");
            }
        }

        return map;
    }
}
