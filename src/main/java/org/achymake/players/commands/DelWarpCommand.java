package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Warps;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DelWarpCommand implements CommandExecutor, TabCompleter {
    private final Warps warps;
    private final Message message;
    public DelWarpCommand(Players plugin) {
        warps = plugin.getWarps();
        message = plugin.getMessage();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (warps.locationExist(args[0])) {
                    warps.delWarp(args[0]);
                    message.send(player, args[0] + "&6 has been deleted");
                }
            }
        }
        if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 0) {
                message.send(consoleCommandSender, "Usage: /delwarp warpName");
            }
            if (args.length == 1) {
                if (warps.locationExist(args[0])) {
                    warps.delWarp(args[0]);
                    message.send(consoleCommandSender, args[0] + " has been deleted");
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
                commands.addAll(warps.getWarps());
            }
        }
        return commands;
    }
}
