package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.bukkit.Server;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand implements CommandExecutor, TabCompleter {
    private final FileConfiguration config;
    private final Message message;
    private final Server server;
    public HelpCommand(Players plugin) {
        config = plugin.getConfig();
        message = plugin.getMessage();
        server = plugin.getServer();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
        }
        if (args.length == 1) {
            if (sender.hasPermission("players.command.rules.others")) {
                Player target = server.getPlayerExact(args[0]);
                if (target != null) {
                    sendHelp(target);
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
                if (player.hasPermission("players.command.rules.others")) {
                    for (Player players : server.getOnlinePlayers()) {
                        commands.add(players.getName());
                    }
                }
            }
        }
        return commands;
    }
    private void sendHelp(CommandSender sender) {
        if (sender instanceof Player player) {
            if (config.isList("help")) {
                for (String messages : config.getStringList("help")) {
                    message.send(player, messages.replaceAll("%player%", player.getName()));
                }
            } else if (config.isString("help")) {
                message.send(player, config.getString("help").replaceAll("%player%", player.getName()));
            }
        }
        if (sender instanceof ConsoleCommandSender commandSender) {
            if (config.isList("help")) {
                for (String messages : config.getStringList("help")) {
                    message.send(commandSender, messages.replaceAll("%player%", commandSender.getName()));
                }
            } else if (config.isString("help")) {
                message.send(commandSender, config.getString("help").replaceAll("%player%", commandSender.getName()));
            }
        }
    }
}
