package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Userdata;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BackCommand implements CommandExecutor, TabCompleter {
    private final Userdata userdata;
    public BackCommand(Players plugin) {
        userdata = plugin.getUserdata();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (userdata.isFrozen(player) || userdata.isJailed(player)) {
                return false;
            } else {
                if (args.length == 0) {
                    String worldName = userdata.getLocation(player, "recent").getWorld().getName();
                    if (player.hasPermission("players.command.back.world." + worldName)) {
                        userdata.teleport(player, "recent", userdata.getLocation(player, "recent"));
                    }
                }
                if (args.length == 1) {
                    if (player.hasPermission("players.command.back.others")) {
                        Player target = player.getServer().getPlayerExact(args[0]);
                        if (target != null) {
                            if (target == player) {
                                userdata.teleportBack(target);
                            } else {
                                if (!target.hasPermission("players.command.back.exempt")) {
                                    userdata.teleportBack(target);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                Player target = consoleCommandSender.getServer().getPlayerExact(args[0]);
                if (userdata.isFrozen(target) || userdata.isJailed(target)) {
                    return false;
                } else {
                    if (target != null) {
                        userdata.teleportBack(target);
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
                if (player.hasPermission("players.command.back.others")) {
                    for (Player players : player.getServer().getOnlinePlayers()) {
                        if (!players.hasPermission("players.command.back.exempt")) {
                            commands.add(players.getName());
                        }
                    }
                }
            }
        }
        return commands;
    }
}
