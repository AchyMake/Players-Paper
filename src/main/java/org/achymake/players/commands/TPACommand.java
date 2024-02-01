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
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;

public class TPACommand implements CommandExecutor, TabCompleter {
    private final Players plugin;
    private final Userdata userdata;
    private final Message message;
    private final Server server;
    private final BukkitScheduler scheduler;
    public TPACommand(Players plugin) {
        this.plugin = plugin;
        userdata = plugin.getUserdata();
        message = plugin.getMessage();
        server = plugin.getServer();
        scheduler = server.getScheduler();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                Player player = (Player) sender;
                message.send(player, "&cUsage:&f /tpa target");
            }
            if (args.length == 1) {
                Player player = (Player) sender;
                if (userdata.isFrozen(player) || userdata.isJailed(player)) {
                    return false;
                } else {
                    Player target = server.getPlayerExact(args[0]);
                    if (target == null) {
                        message.send(player, args[0] + "&c is currently offline");
                    } else if (target == player) {
                        message.send(player, "&cYou can't send request to your self");
                    } else if (userdata.getConfig(player).isString("tpa.sent")) {
                        message.send(player, "&cYou already sent tpa request");
                        message.send(player, "&cYou can type&f /tpcancel");
                    } else {
                        int taskID = scheduler.runTaskLater(plugin, new Runnable() {
                            @Override
                            public void run() {
                                userdata.setString(target, "tpa.from", null);
                                userdata.setString(player, "tpa.sent", null);
                                userdata.setString(player, "task.tpa", null);
                                message.send(player, "&cTeleport request has expired");
                                message.send(target, "&cTeleport request has expired");
                            }
                        }, 300).getTaskId();
                        userdata.setString(target, "tpa.from", player.getUniqueId().toString());
                        userdata.setString(player, "tpa.sent", target.getUniqueId().toString());
                        userdata.setInt(player, "task.tpa", taskID);
                        message.send(target, player.getName() + "&6 has sent you a tpa request");
                        message.send(target, "&6You can type&a /tpaccept&6 or&c /tpdeny");
                        message.send(player, "&6You have sent a tpa request to&f " + target.getName());
                        message.send(player, "&6You can type&c /tpcancel");
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
                for (Player players : server.getOnlinePlayers()) {
                    commands.add(players.getName());
                }
            }
        }
        return commands;
    }
}
