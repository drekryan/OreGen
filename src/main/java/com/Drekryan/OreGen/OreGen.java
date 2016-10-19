package com.Drekryan.OreGen;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class OreGen extends JavaPlugin {
    private ConsoleCommandSender consoleSender;

    @Override
    public void onEnable() {
        consoleSender = this.getServer().getConsoleSender();
        if (consoleSender != null) {
            consoleSender.sendMessage("[OreGen] Successfully Enabled!");
        }

        //Register Block Listener
        getServer().getPluginManager().registerEvents(new BlockListener(this), this);
    }

    @Override
    public void onDisable() {
        if (consoleSender != null) {
            consoleSender.sendMessage("[OreGen] OreGen disabled...");
        }
    }
}