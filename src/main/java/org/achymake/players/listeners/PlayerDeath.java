package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.data.Userdata;
import org.achymake.players.net.Discord;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Random;

public class PlayerDeath implements Listener {
    private final FileConfiguration config;
    private final Userdata userdata;
    private final Discord discord;
    public PlayerDeath(Players plugin) {
        config = plugin.getConfig();
        userdata = plugin.getUserdata();
        discord = plugin.getDiscord();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        userdata.setLocation(player, "death");
        discord.send(player.getName(), event.getDeathMessage());
        if (!(config.getInt("deaths.drop-player-head.chance") > new Random().nextInt(100)))return;
        if (config.getBoolean("deaths.drop-player-head.enable")) {
            event.getDrops().add(getOfflinePlayerHead(player, 1));
        } else if (player.hasPermission("players.death-player-head")) {
            event.getDrops().add(getOfflinePlayerHead(player, 1));
        }
    }
    private ItemStack getOfflinePlayerHead(OfflinePlayer offlinePlayer, int amount) {
        if (offlinePlayer == null) {
            return new ItemStack(Material.PLAYER_HEAD, amount);
        } else {
            ItemStack skullItem = new ItemStack(Material.PLAYER_HEAD, amount);
            SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();
            skullMeta.setOwningPlayer(offlinePlayer);
            skullItem.setItemMeta(skullMeta);
            return skullItem;
        }
    }
}
