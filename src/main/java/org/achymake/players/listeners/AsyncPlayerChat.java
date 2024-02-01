package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.achymake.players.net.Discord;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChat implements Listener {
    private final Userdata userdata;
    private final Message message;
    private final Discord discord;
    public AsyncPlayerChat(Players plugin) {
        userdata = plugin.getUserdata();
        discord = plugin.getDiscord();
        message = plugin.getMessage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (userdata.isMuted(player)) {
            event.setCancelled(true);
        } else {
            String prefix = userdata.prefix(player);
            String displayName = userdata.getDisplayName(player);
            String suffix = userdata.suffix(player);
            String output = event.getMessage();
            if (event.getPlayer().hasPermission("players.event.chat.color")) {
                event.setMessage(message.addColor(output));
            }
            discord.send(player.getName(), output);
            event.setFormat(prefix + displayName + suffix + ChatColor.WHITE + ": " + output);
        }
    }
}
