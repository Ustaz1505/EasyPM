package com.ustaz1505.easypm.commands;

import com.ustaz1505.easypm.database.Database;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.ustaz1505.easypm.EasyPM.*;
import static org.bukkit.Bukkit.*;


public class PMCommand implements CommandExecutor{

    private Database db;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @ NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            getLogger().info(logPrefix + notPlayerError);
            return true;
        }
        Player player = (Player) sender;
        if (args.length < 2) {
            player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(msgPrefix + getMessagesConfig().getString("pm-usage-err")));
            return true;
        }
        Player target = getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(msgPrefix + notPlayerError));
            return true;
        }

        StringBuilder message = new StringBuilder();

        for(int i = 1; i < args.length - 1; i++){ //loop threw all the arguments
            String arg = args[i] + " "; //get the argument, and add a space so that the words get spaced out
            message.append(arg); //add the argument to myString
        }
        message.append(args[args.length - 1]);


        try {
            db = new Database();
        }
        catch(Exception e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(epm);
            return true;
        }

        String senderMsgFormat = getMessagesConfig().getString("pm-sender-message");
        String receiverMsgFormat = getMessagesConfig().getString("pm-receiver-message");


        assert receiverMsgFormat != null;
        if (config.getBoolean("enable-notification")) {
            target.playSound(target, "minecraft:entity.experience_orb.pickup", Float.parseFloat(Objects.requireNonNull(config.getString("notification-volume"))), Float.parseFloat(Objects.requireNonNull(config.getString("notification-pitch"))));
        }
        target.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize( config.getString("msg-prefix") + " " + receiverMsgFormat.replace("{receiver-name}", target.getName()).replace("{sender-name}", player.getName()).replace("{message}", message)));
        assert senderMsgFormat != null;
        player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize( config.getString("msg-prefix") + " " + senderMsgFormat.replace("{receiver-name}", target.getName()).replace("{sender-name}", player.getName()).replace("{message}", message)));

        db.addMessage(String.valueOf(message), sender.getName(), target.getName());

        return true;
    }
}
