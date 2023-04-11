package com.ustaz1505.easypm;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public final class EasyPM extends JavaPlugin {
    FileConfiguration config = this.getConfig();
    String logPrefix = config.getString("log-prefix") + " ";
    String msgPrefix = config.getString("msg-prefix") + " ";
    Logger logger = this.getLogger();
    Plugin epm;

    @Override
    public void onEnable() {
        // Plugin startup logic = this;
        epm = this;
        this.saveDefaultConfig();
        Objects.requireNonNull(this.getCommand("pm")).setExecutor(new CommandPM());
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

    public class CommandPM implements CommandExecutor {

        // This method is called, when somebody uses our command
        @Override
        public boolean onCommand(@NotNull CommandSender sender, @ NotNull Command command, @NotNull String label, String[] args) {
            if (!(sender instanceof Player)) {
                logger.info(logPrefix + "Команда может быть выполнена только пользователем!");
                return true;
            }
            Player player = (Player) sender;
            if (args.length < 2) {
                player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(logPrefix + "Используй /pm <Ник> <Сообщение>!"));
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

            String senderMsgFormat = config.getString("pm-sender-message");
            String receiverMsgFormat = config.getString("pm-receiver-message");

            assert receiverMsgFormat != null;
            target.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize( config.getString("msg-prefix") + " " + receiverMsgFormat.replace("{receiver-name}", target.getName()).replace("{sender-name}", player.getName()).replace("{message}", message)));
            assert senderMsgFormat != null;
            player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize( config.getString("msg-prefix") + " " + senderMsgFormat.replace("{receiver-name}", target.getName()).replace("{sender-name}", player.getName()).replace("{message}", message)));
            return true;
        }
    }
}


