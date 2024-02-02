package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.bukkit.Server;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FlySpeedCommand implements CommandExecutor, TabCompleter {
    private final Message message;
    private final Server server;
    public FlySpeedCommand(Players plugin) {
        message = plugin.getMessage();
        server = plugin.getServer();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                float value = Float.parseFloat(args[0]);
                player.setFlySpeed(value);
                message.send(player, "&6You're fly speed has changed to&f " + value);
            }
            if (args.length == 2) {
                if (player.hasPermission("players.command.flyspeed.others")) {
                    float value = Float.parseFloat(args[0]);
                    Player target = server.getPlayerExact(args[1]);
                    if (target != null) {
                        if (target.hasPermission("players.command.flyspeed.exempt")) {
                            message.send(player, "&6You are not allowed to change&f " + target.getName() + " &6fly speed");
                        } else {
                            target.setFlySpeed(value);
                            message.send(player, "&6You changed&f " + target.getName() + " &6fly speed to&f " + value);
                        }
                    } else {
                        message.send(player, args[1] + "&c is currently offline");
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
                commands.add("0.1");
            }
            if (args.length == 2) {
                if (player.hasPermission("players.command.flyspeed.others")) {
                    for (Player players : server.getOnlinePlayers()) {
                        if (!players.hasPermission("players.command.flyspeed.exempt")) {
                            commands.add(players.getName());
                        }
                    }
                }
            }
        }
        return commands;
    }
}
