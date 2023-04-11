package com.ustaz1505.easypm.commands;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.ustaz1505.easypm.EasyPM.epm;
import static com.ustaz1505.easypm.EasyPM.logPrefix;
import static com.ustaz1505.easypm.EasyPM.config;
import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getServer;


public class PMCommand implements CommandExecutor{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @ NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            getLogger().info(epm.getConfig() + "Команда может быть выполнена только пользователем!");
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
