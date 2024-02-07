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
    private final Players plugin;
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    public SetHomeCommand(Players plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (getUserdata().setHome(player, "home")) {
                    getMessage().send(player, "home&6 has been set");
                } else {
                    getMessage().send(player, "&cYou have reach your limit of&f " + getUserdata().getHomes(player).size() + "&c homes");
                }
            }
            if (args.length == 1) {
                String homeName = args[0];
                if (homeName.equalsIgnoreCase("bed")) {
                    getMessage().send(player, "&cYou can't set home for&f " + homeName);
                } else {
                    if (getUserdata().setHome(player, homeName)) {
                        getMessage().send(player, homeName + "&6 has been set");
                    } else {
                        getMessage().send(player, "&cYou have reach your limit of&f " + getUserdata().getHomes(player).size() + "&c homes");
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
                commands.addAll(getUserdata().getHomes(player));
            }
        }
        return commands;
    }
}
