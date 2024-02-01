package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.data.Userdata;
import org.achymake.players.data.Spawn;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class PlayerSpawnLocation implements Listener {
    private final Userdata userdata;
    private final Spawn spawn;
    public PlayerSpawnLocation(Players plugin) {
        userdata = plugin.getUserdata();
        spawn = plugin.getSpawn();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerSpawnLocation(PlayerSpawnLocationEvent event) {
        Player player = event.getPlayer();
        if (userdata.exist(player))return;
        if (!spawn.locationExist())return;
        event.setSpawnLocation(spawn.getLocation());
    }
}
