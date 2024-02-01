package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.text.MessageFormat;

public class BlockBreak implements Listener {
    private final FileConfiguration config;
    private final Userdata userdata;
    private final Message message;
    private final Server server;
    public BlockBreak(Players plugin) {
        config = plugin.getConfig();
        userdata = plugin.getUserdata();
        message = plugin.getMessage();
        server = plugin.getServer();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (userdata.isFrozen(player) || userdata.isJailed(player)) {
            event.setCancelled(true);
        } else {
            if (!config.getBoolean("notification.enable"))return;
            Block block = event.getBlock();
            if (!config.getStringList("notification.block-break").contains(block.getType().toString()))return;
            String worldName = block.getWorld().getName();
            int x = block.getX();
            int y = block.getY();
            int z = block.getZ();
            for (Player players : server.getOnlinePlayers()) {
                if (!players.hasPermission("players.event.block-break.notify"))return;
                for (String messages : config.getStringList("notification.message")) {
                    players.sendMessage(message.addColor(MessageFormat.format(messages, player.getName(), block.getType().toString(), worldName, x, y, z)));
                }
            }
        }
    }
}
