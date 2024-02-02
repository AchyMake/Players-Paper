package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Economy;
import org.achymake.players.data.Message;
import org.achymake.players.data.Worth;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SellCommand implements CommandExecutor, TabCompleter {
    private final Worth worth;
    private final Economy economy;
    private final Message message;
    private final Server server;
    public SellCommand(Players plugin) {
        worth = plugin.getWorth();
        economy = plugin.getEconomy();
        message = plugin.getMessage();
        server = plugin.getServer();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                if (itemStack.isEmpty()) {
                    message.send(player, "&cYou have to hold an item");
                } else {
                    if (worth.isSellable(itemStack.getType())) {
                        double value = worth.getWorth(itemStack.getType());
                        economy.addEconomy(player, value);
                        message.send(player, "&6You sold&f 1 " + itemStack.getType() + "&6 for&a " + economy.getCurrency() + economy.getFormat(worth.getWorth(itemStack.getType())));
                        itemStack.setAmount(itemStack.getAmount() - 1);
                    } else {
                        message.send(player, itemStack.getType() + "&c is not able to sell");
                    }
                }
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("all")) {
                    ItemStack[] itemStacks = player.getInventory().getStorageContents();
                    for (ItemStack itemStack : itemStacks) {
                        if (itemStack != null) {
                            if (worth.isSellable(itemStack.getType())) {
                                double value = worth.getWorth(itemStack.getType()) * itemStack.getAmount();
                                economy.addEconomy(player, value);
                                message.send(player, "&6You sold&f " + itemStack.getAmount() + " " + itemStack.getType() + "&6 for&a " + economy.getCurrency() + economy.getFormat(worth.getWorth(itemStack.getType()) * itemStack.getAmount()));
                                itemStack.setAmount(0);
                            } else {
                                message.send(player, itemStack.getType() + "&c is not able to sell");
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("hand")) {
                    ItemStack itemStack = player.getInventory().getItemInMainHand();
                    if (itemStack.isEmpty()) {
                        message.send(player, "&cYou have to hold an item");
                    } else {
                        if (worth.isSellable(itemStack.getType())) {
                            double value = worth.getWorth(itemStack.getType()) * itemStack.getAmount();
                            economy.addEconomy(player, value);
                            message.send(player, "&6You sold&f " + itemStack.getAmount() + " " + itemStack.getType() + "&6 for&a " + economy.getCurrency() + economy.getFormat(worth.getWorth(itemStack.getType()) * itemStack.getAmount()));
                            itemStack.setAmount(0);
                        } else {
                            message.send(player, itemStack.getType() + "&c is not able to sell");
                        }
                    }
                } else {
                    int amount = Integer.parseInt(args[0]);
                    ItemStack itemStack = player.getInventory().getItemInMainHand();
                    if (itemStack.isEmpty()) {
                        message.send(player, "&cYou have to hold an item");
                    } else {
                        if (worth.isSellable(itemStack.getType())) {
                            if (itemStack.getAmount() >= amount) {
                                double value = worth.getWorth(itemStack.getType()) * amount;
                                int newAmount = itemStack.getAmount() - amount;
                                economy.addEconomy(player, value);
                                message.send(player, "&6You sold&f " + amount + " " + itemStack.getType() + " for&a " + economy.getCurrency() + economy.getFormat(value));
                                itemStack.setAmount(newAmount);
                            } else {
                                message.send(player, "&cYou don't have enough&f " + itemStack.getType());
                            }
                        } else {
                            message.send(player, itemStack.getType() + "&c is not able to sell");
                        }
                    }
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
                commands.add("all");
                commands.add("hand");
                commands.add("8");
                commands.add("16");
                commands.add("32");
                commands.add("64");
                for (Player players : server.getOnlinePlayers()) {
                    commands.add(players.getName());
                }
            }
        }
        return commands;
    }
}
