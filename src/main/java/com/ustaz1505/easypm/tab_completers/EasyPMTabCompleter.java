package com.ustaz1505.easypm.tab_completers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ustaz1505.easypm.EasyPM.msgFolder;

public class EasyPMTabCompleter implements TabCompleter {


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of(
                    "reload",
                    "language",
                    "join-alert"
            );
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("lang") || args[0].equalsIgnoreCase("language")) {
                List<String> a = new ArrayList<>();
                for (final File fileEntry : Objects.requireNonNull(msgFolder.listFiles())) {
                    a.add(fileEntry.getName().substring(9, fileEntry.getName().length() - 4));
                }
                return a;
            } else if (args[0].equalsIgnoreCase("join-alert") || args[0].equalsIgnoreCase("ja")) {
                return List.of(
                        "true",
                        "false"
                );
            }
        }
        return null;
    }
}
