package com.ustaz1505.easypm.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.ustaz1505.easypm.EasyPM.*;

public class HistoryCommand  implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        int page = 0;
        if (args.length == 0) {
            page = 1;
        } else {
            try {
                page = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                if (!(commandSender instanceof Player)) {
                    logger.info("Use:");
                    return false;
                }
                commandSender.sendMessage("Use:");
                return false;
            }
        }



        return true;
    }
}
