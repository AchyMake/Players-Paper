package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class NicknameCommand implements CommandExecutor, TabCompleter {
    private final Userdata userdata;
    private final Message message;
    private final Server server;
    public NicknameCommand(Players plugin) {
        userdata = plugin.getUserdata();
        message = plugin.getMessage();
        server = plugin.getServer();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            String name = userdata.getConfig(player).getString("name");
            String displayName = userdata.getDisplayName(player);
            if (args.length == 0) {
                if (!displayName.equals(name)) {
                    userdata.setString(player, "display-name", name);
                    player.setDisplayName(name);
                    player.setCustomName(name);
                    userdata.resetTabList();
                    message.send(player, "&6You reset your nickname");
                }
            }
            if (args.length == 1) {
                String rename = args[0];
                if (!displayName.equals(rename)) {
                    userdata.setString(player, "display-name", rename);
                    player.setDisplayName(rename);
                    userdata.resetTabList();
                    message.send(player, "&6You changed your nickname to&f " + rename);
                } else {
                    message.send(player, "&cYou already have&f " + rename + "&c as nickname");
                }
            }
            if (args.length == 2) {
                if (player.hasPermission("players.command.nickname.others")) {
                    String rename = args[0];
                    Player target = server.getPlayerExact(args[1]);
                    if (target != null) {
                        if (!userdata.getConfig(target).getString("display-name").equals(rename)) {
                            userdata.setString(target, "display-name", rename);
                            target.setDisplayName(rename);
                            target.setCustomName(rename);
                            userdata.resetTabList();
                            message.send(player, "&6You changed " + target.getName() + " nickname to&f " + args[0]);
                        } else {
                            message.send(player, target.getName() + "&c already have&f " + args[0] + "&c as nickname");
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
                for (Player players : server.getOnlinePlayers()) {
                    commands.add(players.getName());
                }
            }
            if (args.length == 2) {
                if (player.hasPermission("players.command.nickname.others")) {
                    for (Player players : server.getOnlinePlayers()) {
                        commands.add(players.getName());
                    }
                }
            }
        }
        return commands;
    }
}
