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

public class FreezeCommand implements CommandExecutor, TabCompleter {
    private final Userdata userdata;
    private final Message message;
    private final Server server;
    public FreezeCommand(Players plugin) {
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
                    userdata.setBoolean(target, "settings.frozen", !userdata.isFrozen(target));
                    if (userdata.isFrozen(target)) {
                        message.send(player, "&6You froze&f " + target.getName());
                    } else {
                        message.send(player, "&6You unfroze&f " + target.getName());
                    }
                } else {
                    if (target != null) {
                        if (!target.hasPermission("players.command.freeze.exempt")) {
                            userdata.setBoolean(target, "settings.frozen", !userdata.isFrozen(target));
                            if (userdata.isFrozen(target)) {
                                message.send(player, "&6You froze&f " + target.getName());
                            } else {
                                message.send(player, "&6You unfroze&f " + target.getName());
                            }
                        }
                    } else {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                        if (userdata.exist(offlinePlayer)) {
                            userdata.setBoolean(offlinePlayer, "settings.frozen", !userdata.isFrozen(offlinePlayer));
                            if (userdata.isFrozen(offlinePlayer)) {
                                message.send(player, "&6You froze&f " + offlinePlayer.getName());
                            } else {
                                message.send(player, "&6You unfroze&f " + offlinePlayer.getName());
                            }
                        } else {
                            message.send(player, offlinePlayer.getName() + "&c has never joined");
                        }
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                Player target = server.getPlayerExact(args[0]);
                if (target != null) {
                    userdata.setBoolean(target, "settings.frozen", !userdata.isFrozen(target));
                    if (userdata.isFrozen(target)) {
                        message.send(consoleCommandSender, "You froze " + target.getName());
                    } else {
                        message.send(consoleCommandSender, "You unfroze " + target.getName());
                    }
                } else {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                    if (userdata.exist(offlinePlayer)) {
                        userdata.setBoolean(offlinePlayer, "settings.frozen", !userdata.isFrozen(offlinePlayer));
                        if (userdata.isFrozen(offlinePlayer)) {
                            message.send(consoleCommandSender, "You froze " + offlinePlayer.getName());
                        } else {
                            message.send(consoleCommandSender, "You unfroze " + offlinePlayer.getName());
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
                for (Player players : server.getOnlinePlayers()) {
                    if (!players.hasPermission("players.command.freeze.exempt")) {
                        commands.add(players.getName());
                    }
                }
                commands.add(player.getName());
            }
        }
        return commands;
    }
}
