package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RTPCommand implements CommandExecutor, TabCompleter {
    private final Userdata userdata;
    private final Message message;
    public RTPCommand(Players plugin) {
        userdata = plugin.getUserdata();
        message = plugin.getMessage();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (userdata.hasCooldown(player, "rtp")) {
                    message.sendActionBar(player, "&cYou have to wait&f " + userdata.getCooldown(player, "rtp") + "&c seconds");
                } else {
                    userdata.addCooldown(player, "rtp");
                    message.sendActionBar(player, "&6Finding safe locations...");
                    randomTeleport(player);
                }
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("force")) {
                    if (player.hasPermission("players.command.rtp.force")) {
                        message.sendActionBar(player, "&6Finding safe locations...");
                        randomTeleport(player);
                    }
                }
            }
        }
        return true;
    }
    public void randomTeleport(Player player) {
        Block block = userdata.highestRandomBlock();
        if (block.isLiquid()) {
            message.sendActionBar(player, "&cFinding new location due to liquid block");
            randomTeleport(player);
        } else {
            block.getChunk().load();
            message.sendActionBar(player, "&6Teleporting");
            player.teleport(block.getLocation().add(0.5,1,0.5));
        }
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> commands = new ArrayList<>();
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (player.hasPermission("players.command.rtp.force")) {
                    commands.add("force");
                }
            }
        }
        return commands;
    }
}
