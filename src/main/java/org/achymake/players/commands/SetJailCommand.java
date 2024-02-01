package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Jail;
import org.achymake.players.data.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class SetJailCommand implements CommandExecutor, TabCompleter {
    private final Jail jail;
    private final Message message;
    public SetJailCommand(Players plugin) {
        jail = plugin.getJail();
        message = plugin.getMessage();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (jail.locationExist()) {
                    jail.setLocation(player.getLocation());
                    message.send(player, "&6Jail relocated");
                } else {
                    jail.setLocation(player.getLocation());
                    message.send(player, "&6Jail has been set");
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
