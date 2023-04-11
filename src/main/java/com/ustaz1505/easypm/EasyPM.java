package com.ustaz1505.easypm;

import com.ustaz1505.easypm.commands.EasyPMCommand;
import com.ustaz1505.easypm.commands.PMCommand;
import com.ustaz1505.easypm.tab_completers.EasyPMTabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public final class EasyPM extends JavaPlugin {
    public static Plugin epm;
    public static FileConfiguration config;
    public static String logPrefix;
    public static String msgPrefix;
    public static Logger logger;

    @Override
    public void onEnable() {
        // Plugin startup logic = this;

        this.saveDefaultConfig();

        epm = this;
        config = epm.getConfig();
        logPrefix = config.getString("log-prefix") + " ";
        msgPrefix = config.getString("msg-prefix") + " ";
        logger = epm.getLogger();

        Objects.requireNonNull(this.getCommand("pm")).setExecutor(new PMCommand());
        Objects.requireNonNull(this.getCommand("easypm")).setExecutor(new EasyPMCommand());
        Objects.requireNonNull(this.getCommand("easypm")).setTabCompleter(new EasyPMTabCompleter());
        this.getLogger().info( logPrefix + "The plugin successfully started!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }




}


