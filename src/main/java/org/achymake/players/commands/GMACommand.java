package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GMACommand implements CommandExecutor, TabCompleter {
    private final Message message;
    private final Server server;
    public GMACommand(Players plugin) {
        message = plugin.getMessage();
        server = plugin.getServer();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (player.getGameMode().equals(GameMode.ADVENTURE)) {
                    message.send(player, "&cYou are already in&f Adventure&c mode");
                } else {
                    player.setGameMode(GameMode.ADVENTURE);
                    message.send(player, "&6You changed gamemode to&f Adventure");
                }
            }
            if (args.length == 1) {
                if (player.hasPermission("players.command.gamemode.others")) {
                    Player target = server.getPlayerExact(args[0]);
                    if (target == player) {
                        if (!target.getGameMode().equals(GameMode.ADVENTURE)) {
                            target.setGameMode(GameMode.ADVENTURE);
                            message.send(target, player.getName() + "&6 has changed your gamemode to&f Adventure");
                            message.send(player, "&6You changed&f " + target.getName() + "&6 gamemode to&f Adventure");
                        }
                    } else {
                        if (target != null) {
                            if (target.hasPermission("players.command.gamemode.exempt")) {
                                message.send(player, "&cYou are not allowed to change gamemode of&f " + target.getName());
                            } else {
                                if (target.getGameMode().equals(GameMode.ADVENTURE)) {
                                    message.send(player, target.getName() + "&c is already in&f Adventure&c mode");
                                } else {
                                    target.setGameMode(GameMode.ADVENTURE);
                                    message.send(target, player.getName() + "&6 has changed your gamemode to&f Adventure");
                                    message.send(player, "&6You changed&f " + target.getName() + "&6 gamemode to&f Adventure");
                                }
                            }
                        }
                    }
                } else {
                    message.send(player, "&cError:&7 You do not have the permissions to execute the command");
                }
            }
        }
        if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                Player target = server.getPlayerExact(args[0]);
                if (target != null) {
                    if (!target.getGameMode().equals(GameMode.ADVENTURE)) {
                        target.setGameMode(GameMode.ADVENTURE);
                        message.send(target, "&6Your gamemode has changed to&f Adventure");
                        message.send(consoleCommandSender, "You changed " + target.getName() + " gamemode to Adventure");
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
                if (player.hasPermission("players.command.gamemode.others")) {
                    for (Player players : server.getOnlinePlayers()) {
                        if (!players.hasPermission("players.command.gamemode.exempt")) {
                            commands.add(players.getName());
                        }
                    }
                    commands.add(player.getName());
                }
            }
        }
        return commands;
    }
}
