package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

import java.text.MessageFormat;

public class PlayerBucketEmpty implements Listener {
    private final FileConfiguration config;
    private final Userdata userdata;
    private final Message message;
    private final Server server;
    public PlayerBucketEmpty(Players plugin) {
        config = plugin.getConfig();
        userdata = plugin.getUserdata();
        message = plugin.getMessage();
        server = plugin.getServer();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        if (userdata.isFrozen(player) || userdata.isJailed(player)) {
            event.setCancelled(true);
        } else {
            if (!config.getBoolean("notification.enable"))return;
            String material = event.getBucket().toString();
            if (!config.getStringList("notification.bucket-empty").contains(material))return;
            String worldName = event.getBlock().getWorld().getName();
            int x = event.getBlock().getX();
            int y = event.getBlock().getY();
            int z = event.getBlock().getZ();
            for (Player players : server.getOnlinePlayers()) {
                if (!players.hasPermission("players.event.bucket-empty.notify"))return;
                for (String messages : config.getStringList("notification.message")) {
                    players.sendMessage(message.addColor(MessageFormat.format(messages, player.getName(), material, worldName, x, y, z)));
                }
            }
        }
    }
}
