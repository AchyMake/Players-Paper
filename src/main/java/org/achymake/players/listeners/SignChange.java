package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChange implements Listener {
    private final Message message;
    public SignChange(Players plugin) {
        message = plugin.getMessage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onSignChange(SignChangeEvent event) {
        for (int i = 0; i < event.getLines().length; i++) {
            if (!event.getLine(i).contains("&"))return;
            if (!event.getPlayer().hasPermission("players.event.sign.color"))return;
            event.setLine(i, message.addColor(event.getLine(i)));
        }
    }
}