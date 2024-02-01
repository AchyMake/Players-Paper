package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.achymake.players.data.Warps;
import org.bukkit.Server;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WarpCommand implements CommandExecutor, TabCompleter {
    private final Userdata userdata;
    private final Warps warps;
    private final Message message;
    private final Server server;
    public WarpCommand(Players players) {
        userdata = players.getUserdata();
        warps = players.getWarps();
        message = players.getMessage();
        server = players.getServer();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (userdata.isFrozen(player) || userdata.isJailed(player)) {
                    return false;
                } else {
                    if (warps.getWarps().isEmpty()) {
                        message.send(player, "&cWarps is currently empty");
                    } else {
                        message.send(player, "&6Warps:");
                        for (String warps : warps.getWarps()) {
                            message.send(player, "- " + warps);
                        }
                    }
                }
            }
            if (args.length == 1) {
                if (userdata.isFrozen(player) || userdata.isJailed(player)) {
                    return false;
                } else {
                    if (player.hasPermission("players.command.warp." + args[0])) {
                        if (warps.locationExist(args[0])) {
                            userdata.teleport(player, args[0], warps.getLocation(args[0]));
                        } else {
                            message.send(player, args[0] + "&c does not exist");
                        }
                    }
                }
            }
            if (args.length == 2) {
                if (player.hasPermission("players.command.warp.others")) {
                    Player target = server.getPlayerExact(args[1]);
                    if (target != null) {
                        if (userdata.isFrozen(target) || userdata.isJailed(target)) {
                            return false;
                        } else {
                            if (warps.locationExist(args[0])) {
                                userdata.teleport(target, args[0], warps.getLocation(args[0]));
                            } else {
                                message.send(player, args[0] + "&c does not exist");
                            }
                        }
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 2) {
                Player target = server.getPlayerExact(args[1]);
                if (target != null) {
                    if (userdata.isFrozen(target) || userdata.isJailed(target)) {
                        return false;
                    } else {
                        if (warps.locationExist(args[0])) {
                            userdata.teleport(target, args[0], warps.getLocation(args[0]));
                        } else {
                            message.send(consoleCommandSender, args[0] + " does not exist");
                        }
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
                for (String warps : warps.getWarps()) {
                    if (player.hasPermission("players.command.warp." + warps)) {
                        commands.add(warps);
                    }
                }
            }
            if (args.length == 2) {
                if (player.hasPermission("players.command.warp.others")) {
                    for (Player players : server.getOnlinePlayers()) {
                        commands.add(players.getName());
                    }
                }
            }
        }
        return commands;
    }
}
