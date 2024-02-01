package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Spawn;
import org.achymake.players.data.Userdata;
import org.bukkit.Server;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SpawnCommand implements CommandExecutor, TabCompleter {
    private final Userdata userdata;
    private final Spawn spawn;
    private final Message message;
    private final Server server;
    public SpawnCommand(Players plugin) {
        userdata = plugin.getUserdata();
        spawn = plugin.getSpawn();
        message = plugin.getMessage();
        server = plugin.getServer();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (userdata.isFrozen(player) || userdata.isJailed(player)) {
                    return false;
                } else {
                    if (spawn.locationExist()) {
                        userdata.teleport(player, "spawn", spawn.getLocation());
                    } else {
                        message.send(player, "Spawn&c does not exist");
                    }
                }
            }
            if (args.length == 1) {
                if (player.hasPermission("players.command.spawn.others")) {
                    Player target = server.getPlayerExact(args[0]);
                    if (target != null) {
                        if (userdata.isFrozen(target) || userdata.isJailed(target)) {
                            return false;
                        } else {
                            if (spawn.locationExist()) {
                                userdata.teleport(player, "spawn", spawn.getLocation());
                            } else {
                                message.send(player, "Spawn&c does not exist");
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
                    if (userdata.isFrozen(target) || userdata.isJailed(target)) {
                        return false;
                    } else {
                        if (spawn.locationExist()) {
                            userdata.teleport(target, "spawn", spawn.getLocation());
                        } else {
                            message.send(consoleCommandSender, "Spawn&c does not exist");
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
                if (player.hasPermission("players.command.spawn.others")) {
                    for (Player players : server.getOnlinePlayers()) {
                        commands.add(players.getName());
                    }
                }
            }
        }
        return commands;
    }
}
