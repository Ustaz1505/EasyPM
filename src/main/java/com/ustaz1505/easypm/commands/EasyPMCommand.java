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
        } else if (Objects.equals(args[0].toLowerCase(), "language") || Objects.equals(args[0].toLowerCase(), "lang")) {
            if (sender instanceof Player) {
                if (args.length != 2) {
                    sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(msgPrefix + Objects.requireNonNull(getMessagesConfig().getString("lang-change-usage-err")).replace("{alias}", label).replace("{arg0}", args[0])));
                    return true;
                }
                String langCode = args[1];
                File file = new File(epm.getDataFolder()+"/languages/"+"messages_"+langCode+".yml");
                if (!file.exists()) {
                    sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(msgPrefix + getMessagesConfig().getString("lang-change-code-err")));
                    return true;
                }
                config.set("messages-file", "messages_"+langCode+".yml");
                epm.saveConfig();
                reloadConfigs();
                sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(msgPrefix + getMessagesConfig().getString("lang-changed")));
                return true;
            } else {
                if (args.length != 2) {
                    logger.info(logPrefix + Objects.requireNonNull(getMessagesConfig().getString("lang-change-usage-err")).replace("{alias}", label).replace("{arg1}", args[1]));
                    return true;
                }
                String langCode = args[1];
                File file = new File(epm.getDataFolder()+"/languages/"+"messages_"+langCode+".yml");
                if (!file.exists()) {
                    logger.info(logPrefix + getMessagesConfig().getString("lang-change-code-err"));
                    return true;
                }
                config.set("messages-file", "messages_"+langCode+".yml");
                epm.saveConfig();
                reloadConfigs();
                logger.info(logPrefix + getMessagesConfig().getString("lang-changed"));
                return true;
            }
        } else if (Objects.equals(args[0].toLowerCase(), "join-alert") || Objects.equals(args[0].toLowerCase(), "ja")) {
            if (sender instanceof Player) {
                if (args.length != 2) {
                    sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(msgPrefix + Objects.requireNonNull(getMessagesConfig().getString("join-set-err")).replace("{alias}", label)));
                    return true;
                }
                if (args[1].equalsIgnoreCase("true")) {
                    config.set("join-alert", true);
                    sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(msgPrefix + Objects.requireNonNull(getMessagesConfig().getString("join-set-complete")).replace("{value}", "true")));
                } else if (args[1].equalsIgnoreCase("false")) {
                    config.set("join-alert", false);
                    sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(msgPrefix + Objects.requireNonNull(getMessagesConfig().getString("join-set-complete")).replace("{value}", "false")));
                } else {
                    sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(msgPrefix + Objects.requireNonNull(getMessagesConfig().getString("join-set-err")).replace("{alias}", label)));
                }
                epm.saveConfig();
                reloadConfigs();
                return true;
            } else {
                if (args.length != 2) {
                    logger.info(logPrefix + Objects.requireNonNull(getMessagesConfig().getString("join-set-err")).replace("{alias}", label));
                    return true;
                }
                if (args[1].equalsIgnoreCase("true")) {
                    config.set("join-alert", true);
                    logger.info(logPrefix + Objects.requireNonNull(getMessagesConfig().getString("join-set-complete")).replace("{value}", "true"));
                } else if (args[1].equalsIgnoreCase("false")) {
                    config.set("join-alert", false);
                    logger.info(logPrefix + Objects.requireNonNull(getMessagesConfig().getString("join-set-complete")).replace("{value}", "false"));
                } else {
                    logger.info(logPrefix + Objects.requireNonNull(getMessagesConfig().getString("join-set-err")).replace("{alias}", label));
                }
                epm.saveConfig();
                reloadConfigs();
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
