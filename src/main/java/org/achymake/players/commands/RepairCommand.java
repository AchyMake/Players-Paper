package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.ArrayList;
import java.util.List;

public class RepairCommand implements CommandExecutor, TabCompleter {
    private final Userdata userdata;
    private final Message message;
    public RepairCommand(Players plugin) {
        userdata = plugin.getUserdata();
        message = plugin.getMessage();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                if (itemStack.getType().isAir()) {
                    message.send(player,"&cYou have to hold an item");
                } else {
                    if (userdata.hasCooldown(player, "repair")) {
                        message.sendActionBar(player, "&cYou have to wait&f " + userdata.getCooldown(player, "repair") + "&c seconds");
                    } else {
                        Damageable damageable = (Damageable) itemStack.getItemMeta();
                        if (damageable.hasDamage()) {
                            damageable.setDamage(0);
                            itemStack.setItemMeta(damageable);
                            message.send(player, "&6You repaired&f " + itemStack.getType());
                            userdata.addCooldown(player, "repair");
                        } else {
                            message.send(player, "&cThe item is fully repaired");
                        }
                    }
                }
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("force")) {
                    if (player.hasPermission("players.command.repair.force")) {
                        ItemStack itemStack = player.getInventory().getItemInMainHand();
                        if (itemStack.getType().isAir()) {
                            message.send(player,"&cYou have to hold an item");
                        } else {
                            Damageable damageable = (Damageable) itemStack.getItemMeta();
                            if (damageable.hasDamage()) {
                                damageable.setDamage(0);
                                itemStack.setItemMeta(damageable);
                                message.send(player, "&6You repaired&f " + itemStack.getType());
                            } else {
                                message.send(player, "&cThe item is fully repaired");
                            }
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
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (player.hasPermission("players.command.repair.force")) {
                    commands.add("force");
                }
            }
        }
        return commands;
    }
}
