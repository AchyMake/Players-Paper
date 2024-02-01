package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Kits;
import org.achymake.players.data.Message;
import org.bukkit.Server;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class KitCommand implements CommandExecutor, TabCompleter {
    private final Kits kits;
    private final Message message;
    private final Server server;
    public KitCommand(Players plugin) {
        kits = plugin.getKits();
        message = plugin.getMessage();
        server = plugin.getServer();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                message.send(player, "&6Kits:");
                for (String kitNames : kits.getKits()) {
                    if (player.hasPermission("players.command.kit." + kitNames)) {
                        message.send(player, "- " + kitNames);
                    }
                }
            }
            if (args.length == 1) {
                String kitName = args[0].toLowerCase();
                if (player.hasPermission("players.command.kit." + kitName)) {
                    if (kits.hasCooldown(player, kitName)) {
                        message.sendActionBar(player, "&cYou have to wait&f " + kits.getCooldown(player, kitName) + "&c seconds");
                    } else {
                        kits.addCooldown(player, kitName);
                        message.send(player, "&6You received&f " + kitName);
                        kits.giveKit(player, kitName);
                    }
                }
            }
            if (args.length == 2) {
                if (player.hasPermission("players.command.kit.others")) {
                    Player target = server.getPlayerExact(args[1]);
                    if (target != null) {
                        kits.giveKit(target, args[0]);
                        message.send(target, "&6You received&f " + args[0] + "&6 kit");
                        message.send(player, "&6You dropped&f " + args[0] + "&6 kit to&f " + target.getName());
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 0) {
                message.send(consoleCommandSender, "Kits:");
                for (String kitNames : kits.getKits()) {
                    message.send(consoleCommandSender, "- " + kitNames);
                }
            }
            if (args.length == 2) {
                Player target = server.getPlayerExact(args[1]);
                if (target != null) {
                    kits.giveKit(target, args[0]);
                    message.send(target, "&6You received&f " + args[0] + "&6 kit");
                    message.send(consoleCommandSender, "You dropped " + args[0] + " kit to " + target.getName());
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
                for (String kitName : kits.getKits()) {
                    if (player.hasPermission("players.command.kit." + kitName)) {
                        commands.add(kitName);
                    }
                }
            }
            if (args.length == 2) {
                if (player.hasPermission("players.command.kit.others")) {
                    for (Player players : server.getOnlinePlayers()) {
                        commands.add(players.getName());
                    }
                }
            }
        }
        return commands;
    }
}
