package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Warps;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SetWarpCommand implements CommandExecutor, TabCompleter {
    private final Warps warps;
    private final Message message;
    public SetWarpCommand(Players plugin) {
        warps = plugin.getWarps();
        message = plugin.getMessage();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (warps.locationExist(args[0])) {
                    warps.setLocation(args[0], player.getLocation());
                    message.send(player, args[0] + "&6 has been relocated");
                } else {
                    warps.setLocation(args[0], player.getLocation());
                    message.send(player, args[0] + "&6 has been set");
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
