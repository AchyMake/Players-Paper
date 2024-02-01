package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SetHomeCommand implements CommandExecutor, TabCompleter {
    private final Userdata userdata;
    private final Message message;
    public SetHomeCommand(Players plugin) {
        userdata = plugin.getUserdata();
        message = plugin.getMessage();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (userdata.setHome(player, "home")) {
                    message.send(player, "home&6 has been set");
                } else {
                    message.send(player, "&cYou have reach your limit of&f " + userdata.getHomes(player).size() + "&c homes");
                }
            }
            if (args.length == 1) {
                String homeName = args[0];
                if (homeName.equalsIgnoreCase("bed")) {
                    message.send(player, "&cYou can't set home for&f " + homeName);
                } else {
                    if (userdata.setHome(player, homeName)) {
                        message.send(player, homeName + "&6 has been set");
                    } else {
                        message.send(player, "&cYou have reach your limit of&f " + userdata.getHomes(player).size() + "&c homes");
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
                commands.addAll(userdata.getHomes(player));
            }
        }
        return commands;
    }
}
