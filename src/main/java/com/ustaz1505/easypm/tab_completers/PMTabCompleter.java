package com.ustaz1505.easypm.tab_completers;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PMTabCompleter implements TabCompleter {


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<Player> list = new ArrayList<>(Bukkit.getOnlinePlayers());
        List<String> variants = new ArrayList<>(List.of());
        for (int i = 0; i < list.toArray().length; i++) {
            variants.add(list.get(i).getName());
        }
        if (args.length == 1) {
            return variants;
        } if (args.length > 1) {
            return List.of();
        }
        return null;
    }
}
