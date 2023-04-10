package com.ustaz1505.easypm;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class EasyPM extends JavaPlugin {

    String logPrefix = this.getConfig().getString("log-prefix");

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.saveDefaultConfig();
        Objects.requireNonNull(this.getCommand("pm")).setExecutor(new CommandPM());
        this.getLogger().info( logPrefix + " The plugin successfully started!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public class CommandPM implements CommandExecutor {

        // This method is called, when somebody uses our command
        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length < 2) {
                    player.sendMessage("Используй /pm <Ник> <Сообщение>!");
                    return true;
                }
                Player target = getServer().getPlayer(args[0]);
                if (target == null) {
                    player.sendMessage("Игрок не в сети!");
                    return true;
                }

                StringBuilder message = new StringBuilder();

                for(int i = 1; i < args.length - 1; i++){ //loop threw all the arguments
                    String arg = args[i] + " "; //get the argument, and add a space so that the words get spaced out
                    message.append(arg); //add the argument to myString
                }
                message.append(args[args.length - 1]);

                target.sendMessage("Игрок " + player.getName() + " отправил сообщение \"" + message + "\"");
            }
            // If the player (or console) uses our command correct, we can return true
            return true;
        }
    }
}


