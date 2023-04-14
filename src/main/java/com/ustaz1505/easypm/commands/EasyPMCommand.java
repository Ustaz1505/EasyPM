package com.ustaz1505.easypm.commands;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.ustaz1505.easypm.EasyPM.*;

import java.io.File;
import java.util.Objects;

public class EasyPMCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @ NotNull Command command, @NotNull String label, String[] args) {
        if (Objects.equals(args[0], "reload")) {
            if (sender instanceof Player) {

                reloadConfigs();

                sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(msgPrefix + getMessagesConfig().getString("reload-msg")));
                return true;
            } else {

                reloadConfigs();

                logger.info(logPrefix + getMessagesConfig().getString("reload-msg"));
                return true;
            }
        } else if (Objects.equals(args[0], "language") || Objects.equals(args[0], "lang")) {
            if (sender instanceof Player) {
                String langCode = args[1];
                config.set("messages-file", "messages_"+langCode+".yml");
                epm.saveConfig();
                reloadConfigs();
                sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(msgPrefix + getMessagesConfig().getString("lang-changed")));
                return true;
            } else {
                String langCode = args[1];
                config.set("messages-file", "messages_"+langCode+".yml");
                epm.saveConfig();
                reloadConfigs();
                logger.info(logPrefix + getMessagesConfig().getString("lang-changed"));
                return true;
            }
        }
        return true;
    }

    public static void reloadConfigs() {
        epm.reloadConfig();

        config = epm.getConfig();

        // Reload message config

        customConfig = new YamlConfiguration();
        customConfigFile = new File(msgFolder, Objects.requireNonNull(config.getString("messages-file")));
        try {
            customConfig.load(customConfigFile);
        } catch (Exception e) {
            logger.info("Caught an exception" + e);
        }

        //

        logPrefix = config.getString("log-prefix") + " ";
        msgPrefix = config.getString("msg-prefix") + " ";

        notPlayerError = getMessagesConfig().getString("not-player-err");
        notOnlinePlayer = getMessagesConfig().getString("not-online-player");
    }
}
