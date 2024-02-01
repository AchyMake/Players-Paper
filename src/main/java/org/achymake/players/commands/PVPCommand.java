package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PVPCommand implements CommandExecutor, TabCompleter {
    private final Userdata userdata;
    private final Message message;
    private final Server server;
    public PVPCommand(Players plugin) {
        userdata = plugin.getUserdata();
        message = plugin.getMessage();
        server = plugin.getServer();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                userdata.setBoolean(player, "settings.pvp", !userdata.isPVP(player));
                if (userdata.isPVP(player)) {
                    message.sendActionBar(player, "&6&lPVP:&a Enabled");
                } else {
                    message.sendActionBar(player, "&6&lPVP:&c Disabled");
                }
            }
            if (args.length == 1) {
                if (player.hasPermission("players.command.pvp.others")) {
                    Player target = server.getPlayerExact(args[0]);
                    if (target == player) {
                        userdata.setBoolean(target, "settings.pvp", !userdata.isPVP(target));
                        if (userdata.isPVP(target)) {
                            message.sendActionBar(target, "&6&lPVP:&a Enabled");
                        } else {
                            message.sendActionBar(target, "&6&lPVP:&c Disabled");
                        }
                    } else {
                        if (target != null) {
                            if (!target.hasPermission("players.command.pvp.exempt")) {
                                userdata.setBoolean(target, "settings.pvp", !userdata.isPVP(target));
                                if (userdata.isPVP(target)) {
                                    message.send(target, player.getName() + "&6 enabled pvp for you");
                                    message.sendActionBar(target, "&6&lPVP:&a Enabled");
                                    message.send(player, "&6You enabled pvp for&f " + target.getName());
                                } else {
                                    message.send(target, player.getName() + "&6 disabled pvp for you");
                                    message.sendActionBar(target, "&6&lPVP:&c Disabled");
                                    message.send(player, "&6You disabled pvp for&f " + target.getName());
                                }
                            }
                        } else {
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                            if (userdata.exist(offlinePlayer)) {
                                userdata.setBoolean(offlinePlayer, "settings.pvp", !userdata.isPVP(offlinePlayer));
                                if (userdata.isPVP(offlinePlayer)) {
                                    message.send(player, "&6You enabled pvp for&f " + offlinePlayer.getName());
                                } else {
                                    message.send(player, "&6You disabled pvp for&f " + offlinePlayer.getName());
                                }
                            } else {
                                message.send(player, offlinePlayer.getName() + "&c has never joined");
                            }
                        }
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                Player target = server.getPlayerExact(args[0]);
                if (target != null) {
                    userdata.setBoolean(target, "settings.pvp", !userdata.isPVP(target));
                    if (userdata.isPVP(target)) {
                        message.send(target, consoleCommandSender.getName() + "&6 enabled pvp for you");
                        message.sendActionBar(target, "&6&lPVP:&a Enabled");
                        message.send(consoleCommandSender, "You enabled pvp for " + target.getName());
                    } else {
                        message.send(target, consoleCommandSender.getName() + "&6 disabled pvp for you");
                        message.sendActionBar(target, "&6&lPVP:&c Disabled");
                        message.send(consoleCommandSender, "You disabled pvp for " + target.getName());
                    }
                } else {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                    if (userdata.exist(offlinePlayer)) {
                        userdata.setBoolean(offlinePlayer, "settings.pvp", !userdata.isPVP(offlinePlayer));
                        if (userdata.isPVP(offlinePlayer)) {
                            message.send(consoleCommandSender, "You enabled pvp for " + offlinePlayer.getName());
                        } else {
                            message.send(consoleCommandSender, "You disabled pvp for " + offlinePlayer.getName());
                        }
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
                if (player.hasPermission("players.command.pvp.others")) {
                    for (Player players : server.getOnlinePlayers()) {
                        if (!players.hasPermission("players.command.pvp.exempt")) {
                            commands.add(players.getName());
                        }
                    }
                    commands.add(player.getName());
                }
            }
        }
        return commands;
    }
}
