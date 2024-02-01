package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AnvilCommand implements CommandExecutor, TabCompleter {
    private final Message message;
    public AnvilCommand(Players plugin) {
        message = plugin.getMessage();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                player.openAnvil(player.getLocation(), true);
            }
            if (args.length == 1) {
                if (player.hasPermission("players.command.workbench.others")) {
                    Player target = player.getServer().getPlayerExact(args[0]);
                    if (target == player) {
                        target.openAnvil(target.getLocation(), true);
                    } else {
                        if (target != null) {
                            target.openAnvil(target.getLocation(), true);
                            message.send(target, player.getName() + "&6 opened anvil for you");
                            message.send(player, "&6You opened anvil for " + target.getName());
                        }
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender commandSender) {
            if (args.length == 1) {
                Player target = commandSender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    target.openWorkbench(target.getLocation(), true);
                    message.send(commandSender, "You opened anvil for " + target.getName());
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
                if (player.hasPermission("players.command.anvil.others")) {
                    for (Player players : player.getServer().getOnlinePlayers()) {
                        commands.add(players.getName());
                    }
                }
            }
        }
        return commands;
    }
}