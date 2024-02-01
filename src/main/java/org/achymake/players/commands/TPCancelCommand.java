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

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class TPCancelCommand implements CommandExecutor, TabCompleter {
    private final Userdata userdata;
    private final Message message;
    private final Server server;
    private final BukkitScheduler scheduler;
    public TPCancelCommand(Players plugin) {
        userdata = plugin.getUserdata();
        message = plugin.getMessage();
        server = plugin.getServer();
        scheduler = server.getScheduler();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (userdata.getConfig(player).isString("tpa.sent")) {
                    String uuidString = userdata.getConfig(player).getString("tpa.sent");
                    UUID uuid = UUID.fromString(uuidString);
                    Player target = server.getPlayer(uuid);
                    if (target != null) {
                        int taskID = userdata.getConfig(player).getInt("task.tpa");
                        if (scheduler.isQueued(taskID)) {
                            scheduler.cancelTask(taskID);
                            message.send(target, player.getName() + "&6 cancelled tpa request");
                            message.send(player, "&6You cancelled tpa request");
                            userdata.setString(target, "tpa.from", null);
                            userdata.setString(player, "task.tpa", null);
                            userdata.setString(player, "tpa.sent", null);
                        }
                    }
                } else {
                    message.send(player, "&cYou haven't sent any tpa request");
                }
            }
        }
        return true;
    }
    @Override
    public List onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.EMPTY_LIST;
    }
}
