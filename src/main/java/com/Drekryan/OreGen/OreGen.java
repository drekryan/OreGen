package com.Drekryan.OreGen;

import com.Drekryan.OreGen.util.ConfigManager;
import com.Drekryan.OreGen.util.LogHelper;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class OreGen extends JavaPlugin {
    private ConfigManager configManager;
    private BlockListener blockListener;
    private LogHelper logHelper;

    @Override
    public void onEnable() {
        logHelper = new LogHelper(this);
        configManager = new ConfigManager(this);

        //Generate Configuration Files
        if (!configManager.configExists())
            configManager.generateDefaultConfig();

        //Register BlockListener
        blockListener = new BlockListener(this);
        logHelper.info("Successfully Enabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!label.toLowerCase().equals("oregen")) return false;

        if (args.length > 0 && args[0].toLowerCase().equals("reload")) {
            if (configManager != null) {
                if (!configManager.configExists())
                    configManager.generateDefaultConfig();

                if (configManager.loadConfig(true))
                    sender.sendMessage(ChatColor.GOLD + "[OreGen] " + ChatColor.GREEN + "Configuration files have been reloaded...");
                else
                    sender.sendMessage(ChatColor.GOLD + "[OreGen] " + ChatColor.RED + "Configuration files failed to reload!");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Usage: /oregen <reload>");
        }
        return true;
    }

    @Override
    public void onDisable() {
        //TODO: Better cleanup for onDisable
        logHelper.info("OreGen disabled");
    }

    ConfigManager getConfigManager() {
        return this.configManager;
    }

    public BlockListener getBlockListener() {
        return this.blockListener;
    }

    LogHelper getLogHelper() {
        return this.logHelper;
    }
}