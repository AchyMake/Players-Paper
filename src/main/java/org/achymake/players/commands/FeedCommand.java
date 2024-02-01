package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.Server;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FeedCommand implements CommandExecutor, TabCompleter {
    private final Userdata userdata;
    private final Message message;
    private final Server server;
    public FeedCommand(Players plugin) {
        userdata = plugin.getUserdata();
        message = plugin.getMessage();
        server = plugin.getServer();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (userdata.hasCooldown(player, "feed")) {
                    message.sendActionBar(player, "&cYou have to wait&f " + userdata.getCooldown(player, "feed") + "&c seconds");
                } else {
                    player.setFoodLevel(20);
                    message.sendActionBar(player, "&6Your starvation has been satisfied");
                    userdata.addCooldown(player, "feed");
                    message.send(player, "&6You satisfied&f " + player.getName() + "&6's starvation");
                }
            }
            if (args.length == 1) {
                if (player.hasPermission("players.command.feed.others")) {
                    Player target = server.getPlayerExact(args[0]);
                    if (target != null) {
                        target.setFoodLevel(20);
                        message.sendActionBar(target, "&6Your starvation has been satisfied");
                        message.send(player, "&6You satisfied&f " + target.getName() + "&6's starvation");
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                Player target = server.getPlayerExact(args[0]);
                if (target != null) {
                    target.setFoodLevel(20);
                    message.sendActionBar(target, "&6Your starvation has been satisfied");
                    message.send(consoleCommandSender, "You satisfied " + target.getName() + "'s starvation");
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
                if (player.hasPermission("players.command.feed.others")) {
                    for (Player players : server.getOnlinePlayers()) {
                        commands.add(players.getName());
                    }
                }
            }
        }
        return commands;
    }
}
