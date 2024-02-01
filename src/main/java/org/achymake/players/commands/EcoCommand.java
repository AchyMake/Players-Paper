package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Economy;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EcoCommand implements CommandExecutor, TabCompleter {
    private final FileConfiguration config;
    private final Userdata userdata;
    private final Economy economy;
    private final Message message;
    public EcoCommand(Players plugin) {
        config = plugin.getConfig();
        userdata = plugin.getUserdata();
        economy = plugin.getEconomy();
        message = plugin.getMessage();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                message.send(player, "&cUsage:&f /eco add target amount");
            }
            if (args.length == 2) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                if (args[0].equalsIgnoreCase("reset")) {
                    if (userdata.exist(offlinePlayer)) {
                        economy.resetEconomy(offlinePlayer);
                        message.send(player, "&6You reset&f " + offlinePlayer.getName() + "&6 account to&a " + economy.getCurrency() + economy.getFormat(config.getDouble("economy.starting-balance")));
                    } else {
                        message.send(player, offlinePlayer.getName() + "&c has never joined");
                    }
                }
            }
            if (args.length == 3) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                double value = Double.parseDouble(args[2]);
                if (args[0].equalsIgnoreCase("add")) {
                    if (userdata.exist(offlinePlayer)) {
                        economy.addEconomy(offlinePlayer, value);
                        message.send(player, "&6You added&a " + economy.getCurrency() + economy.getFormat(value) + "&6 to&f " + offlinePlayer.getName() + "&6 account");
                    } else {
                        message.send(player, offlinePlayer.getName() + "&c has never joined");
                    }
                }
                if (args[0].equalsIgnoreCase("remove")) {
                    if (userdata.exist(offlinePlayer)) {
                        economy.removeEconomy(offlinePlayer, value);
                        message.send(player, "&6You removed&a " + economy.getCurrency() + economy.getFormat(value) + "&6 from&f " + offlinePlayer.getName() + "&6 account");
                    } else {
                        message.send(player, offlinePlayer.getName() + "&c has never joined");
                    }
                }
                if (args[0].equalsIgnoreCase("set")) {
                    if (userdata.exist(offlinePlayer)) {
                        economy.setEconomy(offlinePlayer, value);
                        message.send(player, "&6You set&a " + economy.getCurrency() + economy.getFormat(value) + "&6 to&f " + offlinePlayer.getName() + "&6 account");
                    } else {
                        message.send(player, offlinePlayer.getName() + "&c has never joined");
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 0) {
                message.send(consoleCommandSender, "Usage: /eco add target amount");
            }
            if (args.length == 2) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                if (args[0].equalsIgnoreCase("reset")) {
                    if (userdata.exist(offlinePlayer)) {
                        economy.resetEconomy(offlinePlayer);
                        message.send(consoleCommandSender, "You reset " + offlinePlayer.getName() + " account to " + economy.getCurrency() + economy.getFormat(config.getDouble("economy.starting-balance")));
                    } else {
                        message.send(consoleCommandSender, offlinePlayer.getName() + " has never joined");
                    }
                }
            }
            if (args.length == 3) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                double value = Double.parseDouble(args[2]);
                if (args[0].equalsIgnoreCase("add")) {
                    if (userdata.exist(offlinePlayer)) {
                        economy.addEconomy(offlinePlayer, value);
                        message.send(consoleCommandSender, "You added " + economy.getCurrency() + economy.getFormat(value) + " to " + offlinePlayer.getName() + " account");
                    } else {
                        message.send(consoleCommandSender, offlinePlayer.getName() + " has never joined");
                    }
                }
                if (args[0].equalsIgnoreCase("remove")) {
                    if (userdata.exist(offlinePlayer)) {
                        economy.removeEconomy(offlinePlayer, value);
                        message.send(consoleCommandSender, "You removed " + economy.getCurrency() + economy.getFormat(value) + " from " + offlinePlayer.getName() + " account");
                    } else {
                        message.send(consoleCommandSender, offlinePlayer.getName() + " has never joined");
                    }
                }
                if (args[0].equalsIgnoreCase("set")) {
                    if (userdata.exist(offlinePlayer)) {
                        economy.setEconomy(offlinePlayer, value);
                        message.send(consoleCommandSender, "You set " + economy.getCurrency() + economy.getFormat(value) + " to " + offlinePlayer.getName() + " account");
                    } else {
                        message.send(consoleCommandSender, offlinePlayer.getName() + " has never joined");
                    }
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
                commands.add("add");
                commands.add("remove");
                commands.add("reset");
                commands.add("set");
            }
            if (args.length == 2) {
                for (OfflinePlayer players : player.getServer().getOfflinePlayers()) {
                    commands.add(players.getName());
                }
            }
            if (args.length == 3) {
                if (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("set")) {
                    commands.add("100");
                    commands.add("500");
                    commands.add("1000");
                }
            }
        }
        return commands;
    }
}
