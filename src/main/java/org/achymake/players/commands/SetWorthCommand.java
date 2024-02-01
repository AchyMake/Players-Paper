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

public class SetWorthCommand implements CommandExecutor, TabCompleter {
    private final Worth worth;
    private final Economy economy;
    private final Message message;
    public SetWorthCommand(Players plugin) {
        worth = plugin.getWorth();
        economy = plugin.getEconomy();
        message = plugin.getMessage();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (player.getInventory().getItemInMainHand().isEmpty()) {
                    message.send(player, "&cYou have to hold an item");
                } else {
                    double value = Double.parseDouble(args[0]);
                    Material material = player.getInventory().getItemInMainHand().getType();
                    if (value > 0) {
                        worth.setWorth(material, value);
                        message.send(player, material + "&6 is now worth&a " + economy.getCurrency() + economy.getFormat(value));
                    } else {
                        worth.setWorth(material, value);
                        message.send(player, material + "&6 is now worthless");
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
            if (!player.getInventory().getItemInMainHand().isEmpty()) {
                if (args.length == 1) {
                    commands.add("0.0625");
                    commands.add("0.125");
                    commands.add("0.25");
                    commands.add("0.50");
                    commands.add("0.75");
                }
            }
        }
        return commands;
    }
}
