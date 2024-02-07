package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.bukkit.Server;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RulesCommand implements CommandExecutor, TabCompleter {
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
    public RulesCommand(Players plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendRules(sender);
        }
        if (args.length == 1) {
            if (sender.hasPermission("players.command.rules.others")) {
                Player target = getServer().getPlayerExact(args[0]);
                if (target != null) {
                    sendRules(target);
                }
            }
        }
        return true;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> commands = new ArrayList<>();
        if (sender instanceof Player) {
            if (args.length == 1) {
                if (sender.hasPermission("players.command.rules.others")) {
                    for (Player players : sender.getServer().getOnlinePlayers()) {
                        commands.add(players.getName());
                    }
                }
            }
        }
        return commands;
    }
    private void sendRules(CommandSender sender) {
        if (sender instanceof Player player) {
            if (getConfig().isList("rules")) {
                for (String messages : getConfig().getStringList("rules")) {
                    getMessage().send(player, messages.replaceAll("%player%", player.getName()));
                }
            } else if (getConfig().isString("rules")) {
                getMessage().send(player, getConfig().getString("rules").replaceAll("%player%", player.getName()));
            }
        }
        if (sender instanceof ConsoleCommandSender commandSender) {
            if (getConfig().isList("rules")) {
                for (String messages : getConfig().getStringList("rules")) {
                    getMessage().send(commandSender, messages.replaceAll("%player%", commandSender.getName()));
                }
            } else if (getConfig().isString("rules")) {
                getMessage().send(commandSender, getConfig().getString("rules").replaceAll("%player%", commandSender.getName()));
            }
        }
    }
}
