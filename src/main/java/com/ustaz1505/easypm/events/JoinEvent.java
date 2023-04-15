package com.ustaz1505.easypm.events;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static com.ustaz1505.easypm.EasyPM.*;

public class JoinEvent implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(msgPrefix + getMessagesConfig().getString("join-message")));
    }
}
