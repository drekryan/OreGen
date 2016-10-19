package com.Drekryan.OreGen;

import com.Drekryan.OreGen.util.ConfigManager;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class OreGen extends JavaPlugin {
    private ConsoleCommandSender consoleSender;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        consoleSender = this.getServer().getConsoleSender();
        if (consoleSender != null) {
            consoleSender.sendMessage("[OreGen] Successfully Enabled!");
        }

        //Register Config Manager
        configManager = new ConfigManager(this);
        configManager.generateDefaultConfig();

        //Register Block Listener
        new BlockListener(this);
    }

    @Override
    public void onDisable() {
        if (consoleSender != null) {
            consoleSender.sendMessage("[OreGen] OreGen disabled");
        }
    }

    ConfigManager getConfigManager() {
        return this.configManager;
    }
}