package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitScheduler;

public class PlayerMove implements Listener {
    private final Userdata userdata;
    private final Message message;
    private final BukkitScheduler scheduler = Bukkit.getScheduler();
    public PlayerMove(Players plugin) {
        userdata = plugin.getUserdata();
        message = plugin.getMessage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (event.hasChangedOrientation())return;
        if (userdata.isFrozen(player)) {
            event.setCancelled(true);
        } else if (userdata.isVanished(player)) {
            message.sendActionBar(player, "&6&lVanish:&a Enabled");
        }
        if (userdata.hasTaskID(player, "teleport")) {
            message.sendActionBar(player, "&cYou moved before teleporting!");
            scheduler.cancelTask(userdata.getTaskID(player, "teleport"));
            userdata.removeTaskID(player, "teleport");
        }
    }
}
