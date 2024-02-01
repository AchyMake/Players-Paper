package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HomeCommand implements CommandExecutor, TabCompleter {
    private final Userdata userdata;
    private final Message message;
    public HomeCommand(Players plugin) {
        userdata = plugin.getUserdata();
        message = plugin.getMessage();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (userdata.isFrozen(player) || userdata.isJailed(player)) {
                return false;
            } else {
                if (args.length == 0) {
                    if (userdata.homeExist(player, "home")) {
                        userdata.teleport(player, "home", userdata.getHome(player, "home"));
                    } else {
                        message.send(player, "home&c does not exist");
                    }
                }
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("bed")) {
                        if (player.hasPermission("players.command.home.bed")) {
                            if (player.getBedSpawnLocation() != null) {
                                userdata.teleport(player, "bed", player.getBedSpawnLocation());
                            } else {
                                message.send(player, args[0] + "&c does not exist");
                            }
                        }
                    } else {
                        if (userdata.homeExist(player, args[0])) {
                            userdata.teleport(player, args[0], userdata.getHome(player, args[0]));
                        } else {
                            message.send(player, args[0] + "&c does not exist");
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
                if (player.hasPermission("players.commands.home.bed")) {
                    commands.add("bed");
                }
                commands.addAll(userdata.getHomes(player));
            }
        }
        return commands;
    }
}
