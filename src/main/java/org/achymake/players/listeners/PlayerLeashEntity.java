package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerLeashEntityEvent;

public class PlayerLeashEntity implements Listener {
    private final Userdata userdata;
    private final Message message;
    public PlayerLeashEntity(Players plugin) {
        userdata = plugin.getUserdata();
        message = plugin.getMessage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerLeashEntity(PlayerLeashEntityEvent event) {
        Player player = event.getPlayer();
        if (userdata.isFrozen(player)) {
            event.setCancelled(true);
            message.send(player, "&c&lHey!&7 Sorry, but you are not allowed to do that while frozen");
        } else if (userdata.isJailed(player)) {
            event.setCancelled(true);
            message.send(player, "&c&lHey!&7 Sorry, but you are not allowed to do that while jailed");
        }
    }
}
