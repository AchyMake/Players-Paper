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
    private final Players plugin;
    private FileConfiguration getConfig() {
        return plugin.getConfig();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    private Server getServer() {
        return plugin.getServer();
    }
    public HelpCommand(Players plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
        }
        if (args.length == 1) {
            if (sender.hasPermission("players.command.rules.others")) {
                Player target = getServer().getPlayerExact(args[0]);
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
                    for (Player players : getServer().getOnlinePlayers()) {
                        commands.add(players.getName());
                    }
                }
            }
        }
        return commands;
    }
    private void sendHelp(CommandSender sender) {
        if (sender instanceof Player player) {
            if (getConfig().isList("help")) {
                for (String messages : getConfig().getStringList("help")) {
                    getMessage().send(player, messages.replaceAll("%player%", player.getName()));
                }
            } else if (getConfig().isString("help")) {
                getMessage().send(player, getConfig().getString("help").replaceAll("%player%", player.getName()));
            }
        }
        if (sender instanceof ConsoleCommandSender commandSender) {
            if (getConfig().isList("help")) {
                for (String messages : getConfig().getStringList("help")) {
                    getMessage().send(commandSender, messages.replaceAll("%player%", commandSender.getName()));
                }
            } else if (getConfig().isString("help")) {
                getMessage().send(commandSender, getConfig().getString("help").replaceAll("%player%", commandSender.getName()));
            }
        }
    }
}
