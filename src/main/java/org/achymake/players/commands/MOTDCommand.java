package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.bukkit.Server;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MOTDCommand implements CommandExecutor, TabCompleter {
    private final FileConfiguration config;
    private final Message message;
    private final Server server;
    public MOTDCommand(Players plugin) {
        config = plugin.getConfig();
        message = plugin.getMessage();
        server = plugin.getServer();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                sendMotd(player, "welcome");
            }
            if (args.length == 1) {
                sendMotd(player, args[0]);
            }
        }
        if (args.length == 2) {
            if (sender.hasPermission("players.command.motd.others")) {
                Player target = server.getPlayerExact(args[1]);
                if (target != null) {
                    sendMotd(target, args[0]);
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
                commands.addAll(config.getConfigurationSection("message-of-the-day").getKeys(false));
            }
            if (args.length == 2) {
                if (player.hasPermission("players.command.motd.others")) {
                    for (Player players : server.getOnlinePlayers()) {
                        commands.add(players.getName());
                    }
                }
            }
        }
        return commands;
    }
    private void sendMotd(Player player, String motd) {
        if (config.isList("message-of-the-day." + motd)) {
            for (String messages : config.getStringList("message-of-the-day." + motd)) {
                message.send(player, messages.replaceAll("%player%", player.getName()));
            }
        }
        if (config.isString("message-of-the-day." + motd)) {
            message.send(player, config.getString("message-of-the-day." + motd).replaceAll("%player%", player.getName()));
        }
    }
}
