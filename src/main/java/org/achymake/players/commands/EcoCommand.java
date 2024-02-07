package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Economy;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EcoCommand implements CommandExecutor, TabCompleter {
    private final Players plugin;
    private FileConfiguration getConfig() {
        return plugin.getConfig();
    }
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private Economy getEconomy() {
        return plugin.getEconomy();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    private Server getServer() {
        return plugin.getServer();
    }
    public EcoCommand(Players plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 2) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                if (args[0].equalsIgnoreCase("reset")) {
                    if (getUserdata().exist(offlinePlayer)) {
                        getEconomy().reset(offlinePlayer);
                        getMessage().send(player, "&6You reset&f " + offlinePlayer.getName() + "&6 account to&a " + getEconomy().currency() + getEconomy().format(getConfig().getDouble("economy.starting-balance")));
                    } else {
                        getMessage().send(player, offlinePlayer.getName() + "&c has never joined");
                    }
                }
            }
            if (args.length == 3) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                double value = Double.parseDouble(args[2]);
                if (args[0].equalsIgnoreCase("add")) {
                    if (getUserdata().exist(offlinePlayer)) {
                        getEconomy().add(offlinePlayer, value);
                        getMessage().send(player, "&6You added&a " + getEconomy().currency() + getEconomy().format(value) + "&6 to&f " + offlinePlayer.getName());
                    } else {
                        getMessage().send(player, offlinePlayer.getName() + "&c has never joined");
                    }
                }
                if (args[0].equalsIgnoreCase("remove")) {
                    if (getUserdata().exist(offlinePlayer)) {
                        if (getEconomy().has(offlinePlayer, value)) {
                            getEconomy().remove(offlinePlayer, value);
                            getMessage().send(player, "&6You removed&a " + getEconomy().currency() + getEconomy().format(value) + "&6 from&f " + offlinePlayer.getName());
                        } else {
                            getMessage().send(player, offlinePlayer.getName() + "&c does not have&a " + getEconomy().currency() + getEconomy().format(value));
                        }
                    } else {
                        getMessage().send(player, offlinePlayer.getName() + "&c has never joined");
                    }
                }
                if (args[0].equalsIgnoreCase("set")) {
                    if (getUserdata().exist(offlinePlayer)) {
                        getEconomy().set(offlinePlayer, value);
                        getMessage().send(player, "&6You set&a " + getEconomy().currency() + getEconomy().format(value) + "&6 to&f " + offlinePlayer.getName());
                    } else {
                        getMessage().send(player, offlinePlayer.getName() + "&c has never joined");
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 2) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                if (args[0].equalsIgnoreCase("reset")) {
                    if (getUserdata().exist(offlinePlayer)) {
                        getEconomy().reset(offlinePlayer);
                        getMessage().send(consoleCommandSender, "You reset " + offlinePlayer.getName() + " account to " + getEconomy().currency() + getEconomy().format(getConfig().getDouble("economy.starting-balance")));
                    } else {
                        getMessage().send(consoleCommandSender, offlinePlayer.getName() + " has never joined");
                    }
                }
            }
            if (args.length == 3) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                double value = Double.parseDouble(args[2]);
                if (args[0].equalsIgnoreCase("add")) {
                    if (getUserdata().exist(offlinePlayer)) {
                        getEconomy().add(offlinePlayer, value);
                        getMessage().send(consoleCommandSender, "You added " + getEconomy().currency() + getEconomy().format(value) + " to " + offlinePlayer.getName());
                    } else {
                        getMessage().send(consoleCommandSender, offlinePlayer.getName() + " has never joined");
                    }
                }
                if (args[0].equalsIgnoreCase("remove")) {
                    if (getUserdata().exist(offlinePlayer)) {
                        getEconomy().remove(offlinePlayer, value);
                        getMessage().send(consoleCommandSender, "You removed " + getEconomy().currency() + getEconomy().format(value) + " from " + offlinePlayer.getName());
                    } else {
                        getMessage().send(consoleCommandSender, offlinePlayer.getName() + " has never joined");
                    }
                }
                if (args[0].equalsIgnoreCase("set")) {
                    if (getUserdata().exist(offlinePlayer)) {
                        getEconomy().set(offlinePlayer, value);
                        getMessage().send(consoleCommandSender, "You set " + getEconomy().currency() + getEconomy().format(value) + " to " + offlinePlayer.getName());
                    } else {
                        getMessage().send(consoleCommandSender, offlinePlayer.getName() + " has never joined");
                    }
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
                commands.add("add");
                commands.add("remove");
                commands.add("reset");
                commands.add("set");
            }
            if (args.length == 2) {
                for (OfflinePlayer players : getServer().getOfflinePlayers()) {
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
