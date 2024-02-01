package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.api.VaultEconomyProvider;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PayCommand implements CommandExecutor, TabCompleter {
    private final Userdata userdata;
    private final FileConfiguration config;
    private final VaultEconomyProvider economyProvider;
    private final Message message;
    private final Server server;
    public PayCommand(Players plugin) {
        userdata = plugin.getUserdata();
        config = plugin.getConfig();
        economyProvider = plugin.getEconomyProvider();
        message = plugin.getMessage();
        server = plugin.getServer();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0 || args.length == 1) {
                message.send(player, "&cUsage:&f /pay target amount");
            }
            if (args.length == 2) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                if (userdata.exist(offlinePlayer)) {
                    double amount = Double.parseDouble(args[1]);
                    if (amount >= config.getDouble("economy.minimum-payment")) {
                        if (economyProvider.has(player, amount)) {
                            economyProvider.withdrawPlayer(player, amount);
                            economyProvider.depositPlayer(offlinePlayer, amount);
                            message.send(player, "&6You paid&f " + offlinePlayer.getName() + "&a " + economyProvider.currencyNamePlural() + economyProvider.format(amount));
                        } else {
                            message.send(player, "&cYou don't have&a " + economyProvider.currencyNamePlural() + economyProvider.format(amount) + "&c to pay&f " + offlinePlayer.getName());
                        }
                    } else {
                        message.send(player, "&cYou have to pay at least&a " + economyProvider.currencyNamePlural() + economyProvider.format(config.getDouble("economy.minimum-payment")));
                    }
                } else {
                    message.send(player, offlinePlayer.getName() + "&c has never joined");
                }
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
}
