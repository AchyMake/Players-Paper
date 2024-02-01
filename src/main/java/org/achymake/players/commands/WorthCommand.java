package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Economy;
import org.achymake.players.data.Message;
import org.achymake.players.data.Worth;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WorthCommand implements CommandExecutor, TabCompleter {
    private final Worth worth;
    private final Message message;
    private final Economy economy;
    public WorthCommand(Players plugin) {
        worth = plugin.getWorth();
        economy = plugin.getEconomy();
        message = plugin.getMessage();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                Material material = Material.valueOf(args[0].toUpperCase());
                if (worth.isSellable(material)) {
                    message.send(player, material + "&6 is worth:&a " + economy.getCurrency() + economy.getFormat(worth.getWorth(material)));
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
                for (String worthList : worth.getList()) {
                    commands.add(worthList.toLowerCase());
                }
            }
        }
        return commands;
    }
}