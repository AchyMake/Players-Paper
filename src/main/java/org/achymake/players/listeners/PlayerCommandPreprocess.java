package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandPreprocess implements Listener {
    private final FileConfiguration config;
    public PlayerCommandPreprocess(Players plugin) {
        config = plugin.getConfig();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.getPlayer().hasPermission("players.event.command.exempt"))return;
        String message = event.getMessage();
        for (String disabled : config.getStringList("commands.disable")) {
            if (message.startsWith("/" + disabled)) {
                event.setCancelled(true);
            }
        }
    }
}
