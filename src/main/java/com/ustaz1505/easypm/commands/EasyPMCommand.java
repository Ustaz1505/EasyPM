package com.ustaz1505.easypm.commands;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.ustaz1505.easypm.EasyPM.epm;
import static com.ustaz1505.easypm.EasyPM.logPrefix;
import static com.ustaz1505.easypm.EasyPM.msgPrefix;
import static com.ustaz1505.easypm.EasyPM.config;
import static com.ustaz1505.easypm.EasyPM.logger;
import java.util.Objects;

public class EasyPMCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @ NotNull Command command, @NotNull String label, String[] args) {
        if (Objects.equals(args[0], "reload")) {
            if (sender instanceof Player) {
                epm.reloadConfig();
                config = epm.getConfig();
                sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(msgPrefix + config.getString("reload-msg")));
                return true;
            } else {
                epm.reloadConfig();
                config = epm.getConfig();
                logger.info(logPrefix + config.getString("reload-msg"));
                return true;
            }
        }
        return true;
    }
}