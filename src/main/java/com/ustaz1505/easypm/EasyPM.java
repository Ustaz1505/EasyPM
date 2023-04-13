package com.ustaz1505.easypm;

import com.ustaz1505.easypm.commands.EasyPMCommand;
import com.ustaz1505.easypm.commands.PMCommand;
import com.ustaz1505.easypm.tab_completers.EasyPMTabCompleter;
import com.ustaz1505.easypm.tab_completers.PMTabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;
import java.util.logging.Logger;

public final class EasyPM extends JavaPlugin {
    public static Plugin epm;
    public static FileConfiguration config;
    public static String logPrefix;
    public static String msgPrefix;
    public static String notPlayerError;
    public static String notOnlinePlayer;
    public static Logger logger;

    public static File customConfigFile;
    public static FileConfiguration customConfig;

    @Override
    public void onEnable() {
        // Plugin startup logic = this;

        this.saveDefaultConfig();
        createMessagesConfig();

        epm = this;
        config = epm.getConfig();
        logPrefix = config.getString("log-prefix") + " ";
        msgPrefix = config.getString("msg-prefix") + " ";
        notPlayerError = getMessagesConfig().getString("not-player-err");
        notOnlinePlayer = getMessagesConfig().getString("not-online-player");
        logger = epm.getLogger();

        Objects.requireNonNull(this.getCommand("pm")).setExecutor(new PMCommand());
        Objects.requireNonNull(this.getCommand("pm")).setTabCompleter(new PMTabCompleter());
        Objects.requireNonNull(this.getCommand("easypm")).setExecutor(new EasyPMCommand());
        Objects.requireNonNull(this.getCommand("easypm")).setTabCompleter(new EasyPMTabCompleter());
        this.getLogger().info( logPrefix + "The plugin successfully started!");
    }

    @Override
    public void onDisable() {
        this.getLogger().info( logPrefix + "The plugin successfully disabled.");
        this.getLogger().info( logPrefix + "Goodbye!");
        // Plugin shutdown logic
    }

    // Message Config

    public static FileConfiguration getMessagesConfig() {
        return customConfig;
    }

    public void createMessagesConfig() {
        customConfigFile = new File(getDataFolder(), "messages.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            saveResource("messages.yml", false);
        }

        customConfig = new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (Exception e) {
            logger.info("Caught an exception" + e);
        }
    }

}
