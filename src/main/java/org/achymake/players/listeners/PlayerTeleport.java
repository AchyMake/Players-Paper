package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.data.Userdata;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerTeleport implements Listener {
    private final Userdata userdata;
    public PlayerTeleport(Players plugin) {
        userdata = plugin.getUserdata();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        PlayerTeleportEvent.TeleportCause teleportCause = event.getCause();
        if (!(teleportCause.equals(PlayerTeleportEvent.TeleportCause.COMMAND) || teleportCause.equals(PlayerTeleportEvent.TeleportCause.PLUGIN)))return;
        userdata.setLocation(event.getPlayer(), "recent");
    }
}
