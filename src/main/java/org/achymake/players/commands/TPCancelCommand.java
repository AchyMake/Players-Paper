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

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class TPCancelCommand implements CommandExecutor, TabCompleter {
    private final Players plugin;
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private Server getServer() {
        return plugin.getServer();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    private BukkitScheduler getScheduler() {
        return Bukkit.getScheduler();
    }
    public TPCancelCommand(Players plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (getUserdata().getConfig(player).isString("tpa.sent")) {
                    String uuidString = getUserdata().getConfig(player).getString("tpa.sent");
                    UUID uuid = UUID.fromString(uuidString);
                    Player target = getServer().getPlayer(uuid);
                    if (target != null) {
                        int taskID = getUserdata().getConfig(player).getInt("task.tpa");
                        if (getScheduler().isQueued(taskID)) {
                            getScheduler().cancelTask(taskID);
                            getMessage().send(target, player.getName() + "&6 cancelled tpa request");
                            getMessage().send(player, "&6You cancelled tpa request");
                            getUserdata().setString(target, "tpa.from", null);
                            getUserdata().setString(player, "task.tpa", null);
                            getUserdata().setString(player, "tpa.sent", null);
                        }
                    }
                } else {
                    getMessage().send(player, "&cYou haven't sent any tpa request");
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
