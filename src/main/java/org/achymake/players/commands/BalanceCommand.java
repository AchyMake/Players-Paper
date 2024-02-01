package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Economy;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BalanceCommand implements CommandExecutor, TabCompleter {
    private final Userdata userdata;
    private final Economy economy;
    private final Message message;
    public BalanceCommand(Players plugin) {
        userdata = plugin.getUserdata();
        economy = plugin.getEconomy();
        message = plugin.getMessage();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                message.send(player, "&6Balance:&a " + economy.getCurrency() + economy.getFormat(economy.getEconomy(player)));
            }
            if (args.length == 1) {
                if (player.hasPermission("players.command.balance.others")) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                    if (userdata.exist(offlinePlayer)) {
                        message.send(player, offlinePlayer.getName() + "&6's balance:&a " + economy.getCurrency() + economy.getFormat(economy.getEconomy(offlinePlayer)));
                    } else {
                        message.send(player, offlinePlayer.getName() + "&c has never joined");
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                if (userdata.exist(offlinePlayer)) {
                    message.send(consoleCommandSender, offlinePlayer.getName() + "'s " + economy.getCurrency() + economy.getFormat(economy.getEconomy(offlinePlayer)));
                } else {
                    message.send(consoleCommandSender, offlinePlayer.getName() + " has never joined");
                }
            }
        }
        return true;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> commands = new ArrayList<>();
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (player.hasPermission("players.command.balance.others")) {
                    for (OfflinePlayer offlinePlayer : player.getServer().getOfflinePlayers()) {
                        if (userdata.exist(offlinePlayer)) {
                            commands.add(offlinePlayer.getName());
                        }
                    }
                }
            }
        }
        return commands;
    }
}
