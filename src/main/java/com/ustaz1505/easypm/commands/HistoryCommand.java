package com.ustaz1505.easypm.commands;

import com.ustaz1505.easypm.database.Database;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

import static com.ustaz1505.easypm.EasyPM.*;

public class HistoryCommand  implements CommandExecutor {

    private Database db;

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        int page;
        int messagesInPage = config.getInt("history-messages-in-page");

        if (!(commandSender instanceof Player)) {
            logger.info(logPrefix + getMessagesConfig().getString("not-player-err"));
            return true;
        }


        if (args.length == 0) {
            page = 1;
        } else {
            try {
                page = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                commandSender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(msgPrefix + getMessagesConfig().getString("history-usage-err")));
                return false;
            }
        }

        try {
            db = new Database();
        }
        catch(Exception e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(epm);
            return true;
        }


        List<List<? extends Serializable>> messages = db.getAllMessages(commandSender.getName());
        if (messages.size() == 0) {
            commandSender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(msgPrefix + getMessagesConfig().getString("history-none-err")));
            return true;
        }


        if (page > div_up(messages.size(), messagesInPage)) {
            commandSender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(msgPrefix + getMessagesConfig().getString("history-no-such-page-err")));
            return true;
        }

        for (int i = messagesInPage * (page - 1); i <= (messagesInPage * page) - 1; i++) {
            try {
                List<?extends Serializable> message = messages.get(i);
                long unix = (long) message.get(0);
                int fromID = (int) message.get(1);
                int toID = (int) message.get(2);
                String messageContent = (String) message.get(3);
                SimpleDateFormat DateFormat = new SimpleDateFormat("dd.MM.yyyy" /*see https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html*/);
                SimpleDateFormat TimeFormat = new SimpleDateFormat("HH:mm" /*see https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html*/);
                String date = DateFormat.format(unix*1000);
                String time = TimeFormat.format(unix*1000);
                if (fromID == toID) {
                    commandSender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(msgPrefix + Objects.requireNonNull(getMessagesConfig().getString("history-msg-self-sent-title")).replace("{date}", date).replace("{time}", time).replace("{receiver-name}", db.getUserByID(toID)).replace("{sender-name}", db.getUserByID(fromID))));
                    commandSender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(messageContent));
                } else if (db.getUserID(commandSender.getName()) == fromID) {
                    commandSender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(msgPrefix + Objects.requireNonNull(getMessagesConfig().getString("history-msg-sent-title")).replace("{date}", date).replace("{time}", time).replace("{receiver-name}", db.getUserByID(toID)).replace("{sender-name}", db.getUserByID(fromID))));
                    commandSender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(messageContent));
                } else if (db.getUserID(commandSender.getName()) == toID) {
                    commandSender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(msgPrefix + Objects.requireNonNull(getMessagesConfig().getString("history-msg-received-title")).replace("{date}", date).replace("{time}", time).replace("{receiver-name}", db.getUserByID(toID)).replace("{sender-name}", db.getUserByID(fromID))));
                    commandSender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(messageContent));
                }
            } catch (IndexOutOfBoundsException e) {
                return true;
            }
        }

        return true;
    }

    int div_up(int x, int y)
    {
        return (x - 1) / y + 1;
    }
}
