package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.data.*;
import org.achymake.players.net.*;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.text.MessageFormat;

public class PlayerJoin implements Listener {
    private final FileConfiguration config;
    private final Userdata userdata;
    private final Message message;
    private final Server server;
    private final UpdateChecker updateChecker;
    private final Discord discord;
    public PlayerJoin(Players plugin) {
        updateChecker = plugin.getUpdateChecker();
        config = plugin.getConfig();
        userdata = plugin.getUserdata();
        message = plugin.getMessage();
        server = plugin.getServer();
        discord = plugin.getDiscord();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (userdata.isVanished(player)) {
            userdata.setVanish(player, true);
            message.send(player, "&6You joined back vanished");
            event.setJoinMessage(null);
        } else {
            userdata.hideVanished(player);
            if (config.getBoolean("connection.join.enable")) {
                sendSound();
                event.setJoinMessage(joinMessage(player));
            } else {
                if (player.hasPermission("players.event.join.message")) {
                    sendSound();
                    event.setJoinMessage(joinMessage(player));
                } else {
                    event.setJoinMessage(null);
                }
            }
            if (userdata.hasJoined(player)) {
                sendMotd(player, "welcome-back");
            } else {
                sendMotd(player, "welcome");
            }
            userdata.resetTabList();
        }
        updateChecker.sendUpdate(player);
        discord.send(player.getName(), event.getJoinMessage());
    }
    private String joinMessage(Player player) {
        return message.addColor(MessageFormat.format(config.getString("connection.join.message"), player.getName()));
    }
    private void sendSound() {
        if (config.getBoolean("connection.join.sound.enable")) {
            String soundType = config.getString("connection.join.sound.type");
            long soundVolume = config.getLong("connection.join.sound.volume");
            long soundPitch = config.getLong("connection.join.sound.pitch");
            for (Player players : server.getOnlinePlayers()) {
                players.playSound(players, Sound.valueOf(soundType), soundVolume, soundPitch);
            }
        }
    }
    private void sendMotd(Player player, String motd) {
        if (config.isList("message-of-the-day." + motd)) {
            for (String messages : config.getStringList("message-of-the-day." + motd)) {
                message.send(player, messages.replaceAll("%player%", player.getName()));
            }
        } else if (config.isString("message-of-the-day." + motd)) {
            message.send(player, config.getString("message-of-the-day." + motd).replaceAll("%player%", player.getName()));
        }
    }
}
