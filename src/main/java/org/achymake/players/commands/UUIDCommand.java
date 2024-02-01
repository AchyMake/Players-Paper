package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class UUIDCommand implements CommandExecutor, TabCompleter {
    private final Message message;
    private final Server server;
    public UUIDCommand(Players plugin) {
        message = plugin.getMessage();
        server = plugin.getServer();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                message.send(player, offlinePlayer.getName() + ":&a " + offlinePlayer.getUniqueId());
            }
        }
        return true;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> commands = new ArrayList<>();
        if (sender instanceof Player) {
            if (args.length == 1) {
                for (OfflinePlayer offlinePlayer : server.getOfflinePlayers()) {
                    commands.add(offlinePlayer.getName());
                }
            }
        }
        return commands;
    }
}