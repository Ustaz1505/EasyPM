package com.ustaz1505.easypm.events;

import com.ustaz1505.easypm.database.Database;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static com.ustaz1505.easypm.EasyPM.*;

public class JoinEvent implements Listener {
    private Database db;
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        try {
            db = new Database();
        }
        catch(Exception e) {
            e.printStackTrace();
            event.getPlayer().kickPlayer(getMessagesConfig().getString("db-conn-failed"));
        }
        switch (db.countUsers(event.getPlayer().getName())) {
            case -1:
                event.getPlayer().kickPlayer(getMessagesConfig().getString("db-search-failed"));
            case 0:
                db.addUser(event.getPlayer().getName(), event.getPlayer().getUniqueId().toString());
        }
        if (config.getBoolean("join-alert")) {
            Player player = event.getPlayer();
            player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(msgPrefix + getMessagesConfig().getString("join-message")));
        }
    }
}
