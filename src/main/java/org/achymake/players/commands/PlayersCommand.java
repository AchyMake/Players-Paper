package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.ArrayList;
import java.util.List;

public class PlayersCommand implements CommandExecutor, TabCompleter {
    private final PluginDescriptionFile description;
    private final Players plugin;
    private final Message message;
    public PlayersCommand(Players plugin) {
        this.plugin = plugin;
        description = plugin.getDescription();
        message = plugin.getMessage();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                message.send(player, "&6" + description.getName() + " " + description.getVersion());
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    plugin.reload();
                    message.send(player, "&6Players:&f files reloaded");
                }
                if (args[0].equalsIgnoreCase("discord")) {
                    message.send(player, "&9Developers Discord");
                    message.send(player, "https://discord.com/invite/aMtQFeJKyB");
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 0) {
                message.send(consoleCommandSender, description.getName() + " " + description.getVersion());
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    plugin.reload();
                    message.send(consoleCommandSender, "Players: config files reloaded");
                }
                if (args[0].equalsIgnoreCase("discord")) {
                    message.send(consoleCommandSender, "Developers Discord");
                    message.send(consoleCommandSender, "https://discord.com/invite/aMtQFeJKyB");
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
                commands.add("reload");
                commands.add("discord");
            }
        }
        return commands;
    }
}
