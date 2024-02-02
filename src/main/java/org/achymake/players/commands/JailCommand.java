package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Jail;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.Server;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class JailCommand implements CommandExecutor, TabCompleter {
    private final Userdata userdata;
    private final Jail jail;
    private final Message message;
    private final Server server;
    public JailCommand(Players plugin) {
        userdata = plugin.getUserdata();
        jail = plugin.getJail();
        message = plugin.getMessage();
        server = plugin.getServer();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                Player target = server.getPlayerExact(args[0]);
                if (target != null) {
                    if (jail.locationExist()) {
                        if (target == player) {
                            if (userdata.isJailed(target)) {
                                userdata.getLocation(target, "jail").getChunk().load();
                                target.teleport(userdata.getLocation(target, "jail"));
                                userdata.setBoolean(target, "jailed", false);
                                message.send(target, "&cYou got free by&f " + player.getName());
                                message.send(player, "&6You freed&f " + target.getName());
                                userdata.setString(target, "locations.jail", null);
                            } else {
                                jail.getLocation().getChunk().load();
                                userdata.setLocation(target, "jail");
                                target.teleport(jail.getLocation());
                                userdata.setBoolean(target, "jailed", true);
                                message.send(target, "&cYou got jailed by&f " + player.getName());
                                message.send(player, "&6You jailed&f " + target.getName());
                            }
                        } else if (!target.hasPermission("players.command.jail.exempt")) {
                            if (userdata.isJailed(target)) {
                                userdata.getLocation(target, "jail").getChunk().load();
                                target.teleport(userdata.getLocation(target, "jail"));
                                userdata.setBoolean(target, "jailed", false);
                                message.send(target, "&cYou got free by&f " + player.getName());
                                message.send(player, "&6You freed&f " + target.getName());
                                userdata.setString(target, "locations.jail", null);
                            } else {
                                jail.getLocation().getChunk().load();
                                userdata.setLocation(target, "jail");
                                target.teleport(jail.getLocation());
                                userdata.setBoolean(target, "jailed", true);
                                message.send(target, "&cYou got jailed by&f " + player.getName());
                                message.send(player, "&6You jailed&f " + target.getName());
                            }
                        }
                    }
                } else {
                    message.send(player, args[0] + "&c is currently offline");
                }
            }
        }
        if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                Player target = server.getPlayerExact(args[0]);
                if (target != null) {
                    if (jail.locationExist()) {
                        if (userdata.isJailed(target)) {
                            userdata.getLocation(target, "jail").getChunk().load();
                            target.teleport(userdata.getLocation(target, "jail"));
                            userdata.setBoolean(target, "jailed", false);
                            message.send(target, "&cYou got free by&f " + consoleCommandSender.getName());
                            message.send(consoleCommandSender, "&6You freed&f " + target.getName());
                            userdata.setString(target, "locations.jail", null);
                        } else {
                            jail.getLocation().getChunk().load();
                            userdata.setLocation(target, "jail");
                            target.teleport(jail.getLocation());
                            userdata.setBoolean(target, "jailed", true);
                            message.send(target, "&cYou got jailed by&f " + consoleCommandSender.getName());
                            message.send(consoleCommandSender, "&6You jailed&f " + target.getName());
                        }
                    }
                } else {
                    message.send(consoleCommandSender, args[0] + " is currently offline");
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
                    if (!players.hasPermission("players.command.jail.exempt")) {
                        commands.add(players.getName());
                    }
                }
                commands.add(player.getName());
            }
        }
        return commands;
    }
}
