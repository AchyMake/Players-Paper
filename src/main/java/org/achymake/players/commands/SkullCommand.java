package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SkullCommand implements CommandExecutor, TabCompleter {
    private final Message message;
    private final Server server;
    public SkullCommand(Players plugin) {
        message = plugin.getMessage();
        server = plugin.getServer();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                message.send(player, "&cUsage:&f /skull offlinePlayer");
            }
            if (args.length == 1) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                if (Arrays.asList(player.getInventory().getStorageContents()).contains(null)) {
                    player.getInventory().addItem(getOfflinePlayerHead(offlinePlayer, 1));
                } else {
                    player.getWorld().dropItem(player.getLocation(), getOfflinePlayerHead(offlinePlayer, 1));
                }
                message.send(player, "&6You received&f " + offlinePlayer.getName() + "&6's skull");
            }
        }
        return true;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> commands = new ArrayList<>();
        if (sender instanceof Player) {
            if (args.length == 1) {
                for (Player players : server.getOnlinePlayers()) {
                    commands.add(players.getName());
                }
            }
        }
        return commands;
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