package com.Drekryan.OreGen.util;

import com.Drekryan.OreGen.OreGen;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConfigManager {
    private OreGen plugin;
    private FileConfiguration config;
    private Map<String, Double> blockChances;
    private static final String CONFIG_VERSION = "0.1";

    public ConfigManager(OreGen plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    public boolean configExists() {
        if (!plugin.getDataFolder().exists()) return false;

        File file = new File(plugin.getDataFolder(), "config.yml");
        return file.exists();
    }

    public boolean loadConfig(boolean isReload) {
        if (this.configExists()) {
            plugin.getLogger().info(isReload ? "Reloading Config.yml..." : "Config.yml found, loading!");

            File file = new File(plugin.getDataFolder(), "config.yml");
            try {
                config.load(file);

                if (isReload) {
                    this.loadBlockChances();
                    plugin.getBlockListener().updateBlockValues();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }
        return false;
    }

    public boolean generateDefaultConfig() {
        try {
            if (!plugin.getDataFolder().exists()) {
                if (!plugin.getDataFolder().mkdirs()) return false;
            }
            File file = new File(plugin.getDataFolder(), "config.yml");
            if (!file.exists()) {
                plugin.getLogger().info("Config.yml not found, creating!");
                config.options().header("OreGen by Drekryan" +
                        "\nA plugin that replaces Cobble Generation with Ore Generation" +
                        "\nConfig Version: " + CONFIG_VERSION + " #DO NOT EDIT\n");
                ConfigurationSection blockChancesSection = config.createSection("Block Chances");
                blockChancesSection.set("COBBLESTONE", 90);
                blockChancesSection.set("COAL_ORE", 10);

                config.options().copyDefaults(true);
                plugin.saveConfig();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public boolean loadBlockChances() {
        //TODO: Make this work with permission groups in the future
        if (!configExists()) return false;
        this.blockChances = new HashMap<>();

        ConfigurationSection blockChancesSection = config.getConfigurationSection("Block Chances");
        Set<String> keys = blockChancesSection.getKeys(false);

        for(String key : keys) {
            double value = blockChancesSection.getDouble(key);
            if (key != null) {
                System.out.println("[OreGen] Loaded: " + key + " | " + value);
                this.blockChances.put(key, value);
            } else {
                System.out.println("[OreGen] Error: Invalid key or value!");
            }
        }

        MapUtil.sortByValue(this.blockChances);
        return true;
    }

    public Map<String, Double> getBlockChances() {
        this.isValid();
        return this.blockChances;
    }

    public boolean isValid() {
        if (!configExists() || this.blockChances == null) return false;

        double totalPercent = 0;
        for (String blockName : this.blockChances.keySet()) {
            totalPercent += this.blockChances.get(blockName);
        }

        if (totalPercent != 100) {
            this.blockChances = null;
        }

        return (totalPercent == 100);
    }

    public boolean isDebug() {
        return false;
    }
}
