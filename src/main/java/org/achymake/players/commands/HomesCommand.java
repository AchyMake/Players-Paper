package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

public class HomesCommand implements CommandExecutor, TabCompleter {
    private final Userdata userdata;
    private final Message message;
    private final Server server;
    public HomesCommand(Players plugin) {
        userdata = plugin.getUserdata();
        message = plugin.getMessage();
        server = plugin.getServer();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (userdata.getHomes(player).isEmpty()) {
                    message.send(player, "&cYou haven't set any homes yet");
                } else {
                    message.send(player, "&6Homes:");
                    for (String listedHomes : userdata.getHomes(player)) {
                        message.send(player, "- " + listedHomes);
                    }
                }
            }
            if (args.length == 3) {
                String arg0 = args[0];
                String target = args[1];
                String targetHome = args[2];
                if (arg0.equalsIgnoreCase("delete")) {
                    if (player.hasPermission("players.command.homes.delete")) {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(target);
                        if (userdata.exist(offlinePlayer)) {
                            if (userdata.getHomes(offlinePlayer).contains(targetHome)) {
                                userdata.setString(offlinePlayer, "homes." + targetHome, null);
                                message.send(player, "&6Deleted&f " + targetHome + "&6 of&f " + target);
                            } else {
                                message.send(player, target + "&c doesn't have&f " + targetHome);
                            }
                        } else {
                            message.send(player, target + "&c has never joined");
                        }
                    }
                }
                if (arg0.equalsIgnoreCase("teleport")) {
                    if (player.hasPermission("players.command.homes.teleport")) {
                        OfflinePlayer offlinePlayer = server.getOfflinePlayer(target);
                        if (userdata.exist(offlinePlayer)) {
                            if (targetHome.equalsIgnoreCase("bed")) {
                                if (offlinePlayer.getBedSpawnLocation() != null) {
                                    player.teleport(offlinePlayer.getBedSpawnLocation());
                                    message.send(player, "&6Teleporting&f " + targetHome + "&6 of&f " + target);
                                }
                            } else {
                                if (userdata.getHomes(offlinePlayer).contains(targetHome)) {
                                    userdata.getHome(offlinePlayer, targetHome).getChunk().load();
                                    message.send(player, "&6Teleporting&f " + targetHome + "&6 of&f " + target);
                                    player.teleport(userdata.getHome(offlinePlayer, targetHome));
                                } else {
                                    message.send(player, target + "&c doesn't have&f " + targetHome);
                                }
                            }
                        } else {
                            message.send(player, target + "&c has never joined");
                        }
                    }
                }
            }
        }
        return true;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> commands = new ArrayList<>();
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (player.hasPermission("players.command.homes.delete")) {
                    commands.add("delete");
                }
                if (player.hasPermission("players.command.homes.teleport")) {
                    commands.add("teleport");
                }
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("teleport")) {
                    if (player.hasPermission("players.command.homes.teleport")) {
                        for (OfflinePlayer offlinePlayers : server.getOfflinePlayers()) {
                            commands.add(offlinePlayers.getName());
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("delete")) {
                    if (player.hasPermission("players.command.homes.delete")) {
                        for (OfflinePlayer offlinePlayers : server.getOfflinePlayers()) {
                            commands.add(offlinePlayers.getName());
                        }
                    }
                }
            }
            if (args.length == 3) {
                if (player.hasPermission("players.command.homes.teleport")) {
                    OfflinePlayer offlinePlayer = server.getOfflinePlayer(args[1]);
                    if (userdata.exist(offlinePlayer)) {
                        commands.addAll(userdata.getHomes(offlinePlayer));
                    }
                }
            }
        }
        return commands;
    }
}
