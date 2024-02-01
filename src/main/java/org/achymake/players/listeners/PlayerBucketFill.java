package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.data.Userdata;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketFillEvent;

public class PlayerBucketFill implements Listener {
    private final Userdata userdata;
    public PlayerBucketFill(Players plugin) {
        userdata = plugin.getUserdata();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerBucketFill(PlayerBucketFillEvent event) {
        if (!(userdata.isFrozen(event.getPlayer()) || userdata.isJailed(event.getPlayer())))return;
        event.setCancelled(true);
    }
}
