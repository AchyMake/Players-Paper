package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.achymake.players.data.Warps;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DelHomeCommand implements CommandExecutor, TabCompleter {
    private final Userdata userdata;
    private final Message message;
    public DelHomeCommand(Players plugin) {
        userdata = plugin.getUserdata();
        message = plugin.getMessage();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                message.send(player, "&cUsage:&f /delhome homeName");
            }
            if (args.length == 1) {
                if (userdata.homeExist(player, args[0])) {
                    userdata.setString(player, "homes." + args[0], null);
                    message.send(player, args[0] + "&6 has been deleted");
                } else {
                    message.send(player, args[0] + "&c does not exist");
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
                commands.addAll(userdata.getHomes(player));
            }
        }
        return commands;
    }
}
