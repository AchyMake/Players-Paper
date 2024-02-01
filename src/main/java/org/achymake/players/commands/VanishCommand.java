package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class VanishCommand implements CommandExecutor, TabCompleter {
    private final Userdata userdata;
    private final Message message;
    private final Server server;
    public VanishCommand(Players plugin) {
        userdata = plugin.getUserdata();
        message = plugin.getMessage();
        server = plugin.getServer();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                userdata.setVanish(player, !userdata.isVanished(player));
            }
            if (args.length == 1) {
                if (player.hasPermission("players.command.vanish.others")) {
                    Player target = server.getPlayerExact(args[0]);
                    if (target != null) {
                        if (target == player) {
                            userdata.setVanish(target, !userdata.isVanished(target));
                        } else {
                            if (!target.hasPermission("players.command.vanish.exempt")) {
                                userdata.setVanish(target, !userdata.isVanished(target));
                                if (userdata.isVanished(target)) {
                                    message.send(target, player.getName() + "&6 made you vanish");
                                    message.send(player, target.getName() + "&6 is now vanished");
                                } else {
                                    message.send(target, player.getName() + "&6 made you no longer vanish");
                                    message.send(player, target.getName() + "&6 is no longer vanished");
                                }
                            }
                        }
                    } else {
                        OfflinePlayer offlinePlayer = server.getOfflinePlayer(args[0]);
                        if (userdata.exist(offlinePlayer)) {
                            userdata.setVanish(offlinePlayer, !userdata.isVanished(offlinePlayer));
                            if (userdata.isVanished(offlinePlayer)) {
                                message.send(player, offlinePlayer.getName() + "&6 is now vanished");
                            } else {
                                message.send(player, offlinePlayer.getName() + "&6 is no longer vanished");
                            }
                        } else {
                            message.send(player, offlinePlayer.getName() + "&c has never joined");
                        }
                    }
                }
            }
            if (args.length == 2) {
                Player target = server.getPlayerExact(args[0]);
                boolean value = Boolean.valueOf(args[1]);
                if (value) {
                    if (target != null) {
                        if (!userdata.isVanished(target)) {
                            if (target == player) {
                                userdata.setVanish(target, true);
                            } else {
                                if (!target.hasPermission("players.command.vanish.exempt")) {
                                    userdata.setVanish(target, true);
                                    message.send(target, player.getName() + "&6 made you vanish");
                                    message.send(player, target.getName() + "&6 is now vanished");
                                }
                            }
                        }
                    } else {
                        OfflinePlayer offlinePlayer = server.getOfflinePlayer(args[0]);
                        if (userdata.exist(offlinePlayer)) {
                            if (!userdata.isVanished(offlinePlayer)) {
                                userdata.setVanish(offlinePlayer, true);
                                if (userdata.isVanished(offlinePlayer)) {
                                    message.send(player, offlinePlayer.getName() + "&6 is now vanished");
                                } else {
                                    message.send(player, offlinePlayer.getName() + "&6 is no longer vanished");
                                }
                            }
                        } else {
                            message.send(player, offlinePlayer.getName() + "&c has never joined");
                        }
                    }
                } else {
                    if (target != null) {
                        if (userdata.isVanished(target)) {
                            if (target == player) {
                                userdata.setVanish(target, false);
                            } else {
                                if (!target.hasPermission("players.command.vanish.exempt")) {
                                    userdata.setVanish(target, false);
                                    message.send(target, player.getName() + "&6 made you no longer vanish");
                                    message.send(player, target.getName() + "&6 is no longer vanished");
                                }
                            }
                        }
                    } else {
                        OfflinePlayer offlinePlayer = server.getOfflinePlayer(args[0]);
                        if (userdata.exist(offlinePlayer)) {
                            if (userdata.isVanished(offlinePlayer)) {
                                userdata.setVanish(offlinePlayer, false);
                                if (userdata.isVanished(offlinePlayer)) {
                                    message.send(player, offlinePlayer.getName() + "&6 is now vanished");
                                } else {
                                    message.send(player, offlinePlayer.getName() + "&6 is no longer vanished");
                                }
                            }
                        } else {
                            message.send(player, offlinePlayer.getName() + "&c has never joined");
                        }
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                Player target = server.getPlayerExact(args[0]);
                if (target != null) {
                    userdata.setVanish(target, !userdata.isVanished(target));
                    if (userdata.isVanished(target)) {
                        message.send(consoleCommandSender, target.getName() + " is now vanished");
                    } else {
                        message.send(consoleCommandSender, target.getName() + " is no longer vanished");
                    }
                } else {
                    OfflinePlayer offlinePlayer = server.getOfflinePlayer(args[0]);
                    if (userdata.exist(offlinePlayer)) {
                        userdata.setVanish(offlinePlayer, !userdata.isVanished(offlinePlayer));
                        if (userdata.isVanished(offlinePlayer)) {
                            message.send(consoleCommandSender, offlinePlayer.getName() + " is now vanished");
                        } else {
                            message.send(consoleCommandSender, offlinePlayer.getName() + " is no longer vanished");
                        }
                    } else {
                        message.send(consoleCommandSender, offlinePlayer.getName() + " has never joined");
                    }
                }
            }
            if (args.length == 2) {
                Player target = server.getPlayerExact(args[0]);
                boolean value = Boolean.valueOf(args[1]);
                if (value) {
                    if (target != null) {
                        if (!userdata.isVanished(target)) {
                            userdata.setVanish(target, true);
                            message.send(consoleCommandSender, target.getName() + " is now vanished");
                        }
                    } else {
                        OfflinePlayer offlinePlayer = server.getOfflinePlayer(args[0]);
                        if (userdata.exist(offlinePlayer)) {
                            if (!userdata.isVanished(offlinePlayer)) {
                                userdata.setVanish(offlinePlayer, true);
                                if (userdata.isVanished(offlinePlayer)) {
                                    message.send(consoleCommandSender, offlinePlayer.getName() + " is now vanished");
                                } else {
                                    message.send(consoleCommandSender, offlinePlayer.getName() + " is no longer vanished");
                                }
                            }
                        } else {
                            message.send(consoleCommandSender, offlinePlayer.getName() + " has never joined");
                        }
                    }
                } else {
                    if (target != null) {
                        if (userdata.isVanished(target)) {
                            userdata.setVanish(target, false);
                            message.send(consoleCommandSender, target.getName() + " is no longer vanished");
                        }
                    } else {
                        OfflinePlayer offlinePlayer = server.getOfflinePlayer(args[0]);
                        if (userdata.exist(offlinePlayer)) {
                            if (userdata.isVanished(offlinePlayer)) {
                                userdata.setVanish(offlinePlayer, false);
                                if (userdata.isVanished(offlinePlayer)) {
                                    message.send(consoleCommandSender, offlinePlayer.getName() + " is now vanished");
                                } else {
                                    message.send(consoleCommandSender, offlinePlayer.getName() + " is no longer vanished");
                                }
                            }
                        } else {
                            message.send(consoleCommandSender, offlinePlayer.getName() + " has never joined");
                        }
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
                if (player.hasPermission("players.command.vanish.others")) {
                    for (Player players : server.getOnlinePlayers()) {
                        if (!players.hasPermission("players.command.vanish.exempt")) {
                            commands.add(players.getName());
                        }
                    }
                    commands.add(player.getName());
                }
            }
            if (args.length == 2) {
                if (player.hasPermission("players.command.vanish.others")) {
                    commands.add("true");
                    commands.add("false");
                }
            }
        }
        return commands;
    }
}
