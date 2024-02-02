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

public class MuteCommand implements CommandExecutor, TabCompleter {
    private final Userdata userdata;
    private final Message message;
    private final Server server;
    public MuteCommand(Players plugin) {
        userdata = plugin.getUserdata();
        message = plugin.getMessage();
        server = plugin.getServer();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                Player target = server.getPlayerExact(args[0]);
                if (target == player) {
                    userdata.setBoolean(target, "settings.muted", !userdata.isMuted(target));
                    if (userdata.isMuted(target)) {
                        message.send(player, "&6You muted your self");
                    } else {
                        message.send(player, "&6You unmuted your self");
                    }
                } else {
                    if (target != null) {
                        if (!target.hasPermission("players.command.mute.exempt")) {
                            userdata.setBoolean(target, "settings.muted", !userdata.isMuted(target));
                            if (userdata.isMuted(target)) {
                                message.send(player, "&6You muted&f " + target.getName());
                            } else {
                                message.send(player, "&6You unmuted&f " + target.getName());
                            }
                        }
                    } else {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                        if (userdata.exist(offlinePlayer)) {
                            userdata.setBoolean(offlinePlayer, "settings.muted", !userdata.isMuted(offlinePlayer));
                            if (userdata.isMuted(offlinePlayer)) {
                                message.send(player, "&6You muted&f " + offlinePlayer.getName());
                            } else {
                                message.send(player, "&6You unmuted&f " + offlinePlayer.getName());
                            }
                        } else {
                            message.send(player,offlinePlayer.getName() + "&c has never joined");
                        }
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                Player target = server.getPlayerExact(args[0]);
                if (target != null) {
                    userdata.setBoolean(target, "settings.muted", !userdata.isMuted(target));
                    if (userdata.isMuted(target)) {
                        message.send(consoleCommandSender, "You muted " + target.getName());
                    } else {
                        message.send(consoleCommandSender, "You unmuted " + target.getName());
                    }
                } else {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                    if (userdata.exist(offlinePlayer)) {
                        userdata.setBoolean(offlinePlayer, "settings.muted", !userdata.isMuted(offlinePlayer));
                        if (userdata.isMuted(offlinePlayer)) {
                            message.send(consoleCommandSender, "You muted " + offlinePlayer.getName());
                        } else {
                            message.send(consoleCommandSender, "You unmuted " + offlinePlayer.getName());
                        }
                    } else {
                        message.send(consoleCommandSender,offlinePlayer.getName() + " has never joined");
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
                for (Player players : server.getOnlinePlayers()) {
                    if (!players.hasPermission("players.command.mute.exempt")) {
                        commands.add(players.getName());
                    }
                }
                commands.add(player.getName());
            }
        }
        return commands;
    }
}
