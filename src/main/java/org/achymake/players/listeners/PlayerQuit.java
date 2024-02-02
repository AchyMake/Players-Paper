package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.achymake.players.net.Discord;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.text.MessageFormat;
import java.util.UUID;

public class PlayerQuit implements Listener {
    private final FileConfiguration config;
    private final Userdata userdata;
    private final Message message;
    private final Server server;
    private final Discord discord;
    private final BukkitScheduler scheduler = Bukkit.getScheduler();
    public PlayerQuit(Players plugin) {
        config = plugin.getConfig();
        userdata = plugin.getUserdata();
        message = plugin.getMessage();
        server = plugin.getServer();
        discord = plugin.getDiscord();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        removeTeleportTask(player);
        removeTPAonQuit(player);
        userdata.setLocation(player, "quit");
        if (userdata.isVanished(player)) {
            userdata.getVanished().remove(player);
            event.setQuitMessage(null);
        } else {
            if (config.getBoolean("connection.quit.enable")) {
                event.setQuitMessage(quitMessage(player));
                playSound();
            } else {
                if (player.hasPermission("players.event.quit.message")) {
                    event.setQuitMessage(quitMessage(player));
                    playSound();
                } else {
                    event.setQuitMessage(null);
                }
            }
            userdata.resetTabList();
        }
        discord.send(player.getName(), "Left the Server");
    }
    private String quitMessage(Player player) {
        return message.addColor(MessageFormat.format(config.getString("connection.quit.message"), player.getName()));
    }
    private void removeTeleportTask(Player player) {
        if (userdata.hasTaskID(player, "teleport")) {
            scheduler.cancelTask(userdata.getTaskID(player, "teleport"));
            userdata.removeTaskID(player, "teleport");
        }
    }
    private void playSound() {
        if (config.getBoolean("connection.quit.sound.enable")) {
            String soundType = config.getString("connection.quit.sound.type");
            long soundVolume = config.getLong("connection.quit.sound.volume");
            long soundPitch = config.getLong("connection.quit.sound.pitch");
            for (Player players : server.getOnlinePlayers()) {
                players.playSound(players, Sound.valueOf(soundType), soundVolume, soundPitch);
            }
        }
    }
    private void removeTPAonQuit(Player player) {
        if (userdata.getConfig(player).isString("tpa.from")) {
            String uuidString = userdata.getConfig(player).getString("tpa.from");
            UUID uuid = UUID.fromString(uuidString);
            OfflinePlayer target = server.getOfflinePlayer(uuid);
            userdata.setString(target, "tpa.sent", null);
            int taskID = userdata.getConfig(target).getInt("task.tpa");
            if (server.getScheduler().isQueued(taskID)) {
                server.getScheduler().cancelTask(taskID);
                userdata.setString(player, "tpa.from", null);
            }
            userdata.setString(target, "task.tpa", null);
        } else if (userdata.getConfig(player).isString("tpa.sent")) {
            String uuidString = userdata.getConfig(player).getString("tpa.sent");
            UUID uuid = UUID.fromString(uuidString);
            OfflinePlayer target = server.getOfflinePlayer(uuid);
            userdata.setString(target, "tpa.from", null);
            int taskID = userdata.getConfig(player).getInt("task.tpa");
            if (server.getScheduler().isQueued(taskID)) {
                server.getScheduler().cancelTask(taskID);
                userdata.setString(player, "task.tpa", null);
            }
            userdata.setString(player, "tpa.sent", null);
        }
    }
}
