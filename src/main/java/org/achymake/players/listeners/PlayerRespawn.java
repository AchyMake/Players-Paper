package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.data.Userdata;
import org.achymake.players.data.Message;
import org.achymake.players.data.Spawn;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawn implements Listener {
    private final Userdata userdata;
    private final Spawn spawn;
    private final Message message;
    public PlayerRespawn(Players plugin) {
        userdata = plugin.getUserdata();
        spawn = plugin.getSpawn();
        message = plugin.getMessage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (!event.getRespawnReason().equals(PlayerRespawnEvent.RespawnReason.DEATH))return;
        Player player = event.getPlayer();
        if (player.hasPermission("players.event.death.location")) {
            Location location = userdata.getLocation(player,"death");
            String world = location.getWorld().getEnvironment().toString().toLowerCase();
            int x = location.getBlockX();
            int y = location.getBlockY();
            int z = location.getBlockZ();
            message.send(player, "&6Death location:");
            message.send(player, "&6World:&f " + world + "&6 X:&f " + x + "&6 Y:&f " + y + "&6 Z:&f " + z);
        }
        userdata.setBoolean(player, "settings.dead", false);
        if (event.isAnchorSpawn())return;
        if (event.isBedSpawn())return;
        if (userdata.locationExist(player, "home")) {
            event.setRespawnLocation(userdata.getLocation(player, "home"));
        } else if (spawn.locationExist()) {
            event.setRespawnLocation(spawn.getLocation());
        }
    }
}
