package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;

public class TPACommand implements CommandExecutor, TabCompleter {
    private final Players plugin;
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    private Server getServer() {
        return plugin.getServer();
    }
    private BukkitScheduler getScheduler() {
        return Bukkit.getScheduler();
    }
    public TPACommand(Players plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (getUserdata().isFrozen(player) || getUserdata().isJailed(player)) {
                    return false;
                } else {
                    Player target = getServer().getPlayerExact(args[0]);
                    if (target == null) {
                        getMessage().send(player, args[0] + "&c is currently offline");
                    } else if (target == player) {
                        getMessage().send(player, "&cYou can't send request to your self");
                    } else if (getUserdata().getConfig(player).isString("tpa.sent")) {
                        getMessage().send(player, "&cYou already sent tpa request");
                        getMessage().send(player, "&cYou can type&f /tpcancel");
                    } else {
                        int taskID = getScheduler().runTaskLater(plugin, new Runnable() {
                            @Override
                            public void run() {
                                getUserdata().setString(target, "tpa.from", null);
                                getUserdata().setString(player, "tpa.sent", null);
                                getUserdata().setString(player, "task.tpa", null);
                                getMessage().send(player, "&cTeleport request has expired");
                                getMessage().send(target, "&cTeleport request has expired");
                            }
                        }, 300).getTaskId();
                        getUserdata().setString(target, "tpa.from", player.getUniqueId().toString());
                        getUserdata().setString(player, "tpa.sent", target.getUniqueId().toString());
                        getUserdata().setInt(player, "task.tpa", taskID);
                        getMessage().send(target, player.getName() + "&6 has sent you a tpa request");
                        getMessage().send(target, "&6You can type&a /tpaccept&6 or&c /tpdeny");
                        getMessage().send(player, "&6You have sent a tpa request to&f " + target.getName());
                        getMessage().send(player, "&6You can type&c /tpcancel");
                    }
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
                for (Player players : getServer().getOnlinePlayers()) {
                    commands.add(players.getName());
                }
            }
        }
        return commands;
    }
}
