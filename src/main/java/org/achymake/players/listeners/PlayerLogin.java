package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.data.Userdata;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLogin implements Listener {
    private final Userdata userdata;
    private final Server server;
    public PlayerLogin(Players plugin) {
        userdata = plugin.getUserdata();
        server = plugin.getServer();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if (server.getOnlinePlayers().size() >= server.getMaxPlayers()) {
            if (player.hasPermission("players.event.login.full-server")) {
                if (userdata.exist(player)) {
                    if (userdata.isBanned(player)) {
                        event.disallow(PlayerLoginEvent.Result.KICK_BANNED, userdata.getBanReason(player));
                    } else {
                        event.allow();
                        userdata.setup(player);
                    }
                } else {
                    event.allow();
                    userdata.setup(player);
                }
            }
        } else {
            if (userdata.exist(player)) {
                if (userdata.isBanned(player)) {
                    event.disallow(PlayerLoginEvent.Result.KICK_BANNED, userdata.getBanReason(player));
                } else {
                    event.allow();
                    userdata.setup(player);
                }
            } else {
                event.allow();
                userdata.setup(player);
            }
        }
    }
}