package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.data.Userdata;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockReceiveGameEvent;

public class BlockReceiveGame implements Listener {
    private final Userdata userdata;
    public BlockReceiveGame(Players plugin) {
        userdata = plugin.getUserdata();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockReceiveGame(BlockReceiveGameEvent event) {
        if (!(event.getEntity() instanceof Player player))return;
        if (!(userdata.isFrozen(player) || userdata.isJailed(player) || userdata.isVanished(player)))return;
        event.setCancelled(true);
    }
}