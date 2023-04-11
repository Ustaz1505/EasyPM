package com.ustaz1505.easypm;

import com.ustaz1505.easypm.commands.PMCommand;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        Objects.requireNonNull(this.getCommand("easypm")).setExecutor(new CommandEasyPM());
        Objects.requireNonNull(this.getCommand("easypm")).setTabCompleter(new EasyPMTabCompleter());
        this.getLogger().info( logPrefix + "The plugin successfully started!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public class EasyPMTabCompleter implements TabCompleter {


        @Override
        public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
            if (args.length == 1) {
                return List.of(
                        "reload"
                );
            }
            return null;
        }
    }

    public class CommandEasyPM implements CommandExecutor {
        @Override
        public boolean onCommand(@NotNull CommandSender sender, @ NotNull Command command, @NotNull String label, String[] args) {
            if (Objects.equals(args[0], "reload")) {
                if (sender instanceof Player) {
                    reloadConfig();
                    config = getConfig();
                    sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(msgPrefix + config.getString("reload-msg")));
                    return true;
                } else {
                    reloadConfig();
                    config = getConfig();
                    logger.info(logPrefix + config.getString("reload-msg"));
                    return true;
                }
            }
            return true;
        }
    }
}


