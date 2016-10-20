package com.Drekryan.OreGen.util;

import com.Drekryan.OreGen.OreGen;

enum LogLevel {
    INFO,
    WARNING,
    ERROR
}

public class LogHelper {
    private OreGen plugin;

    public LogHelper(OreGen plugin) {
        this.plugin = plugin;
    }

    public void log(LogLevel logLevel, String message) {
        switch (logLevel) {
            case INFO:
                this.info(message);
            case WARNING:
                this.warning(message);
            case ERROR:
                this.error(message);
        }
    }

    public void info(String message) {
        plugin.getLogger().info(message);
    }

    public void warning(String message) {
        plugin.getLogger().warning(message);
    }

    public void error(String message) {
        plugin.getLogger().severe(message);
    }
}
