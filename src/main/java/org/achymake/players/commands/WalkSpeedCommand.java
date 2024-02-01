package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WalkSpeedCommand implements CommandExecutor, TabCompleter {
    private final Message message;
    public WalkSpeedCommand(Players plugin) {
        message = plugin.getMessage();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                float value = Float.parseFloat(args[0]);
                player.setWalkSpeed(value);
                message.send(player, "&6You're walk speed has changed to " + value);
            }
        }
        return true;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> commands = new ArrayList<>();
        if (sender instanceof Player) {
            if (args.length == 1) {
                commands.add("0.2");
            }
        }
        return commands;
    }
}