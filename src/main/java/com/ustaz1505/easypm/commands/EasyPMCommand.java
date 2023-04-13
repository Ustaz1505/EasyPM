package com.ustaz1505.easypm.commands;

import com.ustaz1505.easypm.EasyPM;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.ustaz1505.easypm.EasyPM.*;

import java.util.Objects;

public class EasyPMCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @ NotNull Command command, @NotNull String label, String[] args) {
        if (Objects.equals(args[0], "reload")) {
            if (sender instanceof Player) {
                // Reload message config
                customConfig = new YamlConfiguration();
                try {
                    customConfig.load(customConfigFile);
                } catch (Exception e) {
                    logger.info("Caught an exception" + e);
                }
                //

                epm.reloadConfig();

                config = epm.getConfig();

                logPrefix = config.getString("log-prefix") + " ";
                msgPrefix = config.getString("msg-prefix") + " ";

                notPlayerError = getMessagesConfig().getString("not-player-err");
                notOnlinePlayer = getMessagesConfig().getString("not-online-player");

                sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(msgPrefix + getMessagesConfig().getString("reload-msg")));
                return true;
            } else {
                // Reload message config
                customConfig = new YamlConfiguration();
                try {
                    customConfig.load(customConfigFile);
                } catch (Exception e) {
                    logger.info("Caught an exception" + e);
                }
                //

                epm.reloadConfig();

                config = epm.getConfig();

                logPrefix = config.getString("log-prefix") + " ";
                msgPrefix = config.getString("msg-prefix") + " ";

                notPlayerError = getMessagesConfig().getString("not-player-err");
                notOnlinePlayer = getMessagesConfig().getString("not-online-player");
                logger.info(logPrefix + getMessagesConfig().getString("reload-msg"));
                return true;
            }
        }
        return true;
    }
}