package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Spawn;
import org.achymake.players.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SetSpawnCommand implements CommandExecutor, TabCompleter {
    private final Userdata userdata;
    private final Spawn spawn;
    private final Message message;
    private final Server server;
    public SetSpawnCommand(Players plugin) {
        userdata = plugin.getUserdata();
        spawn = plugin.getSpawn();
        message = plugin.getMessage();
        server = plugin.getServer();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (spawn.locationExist()) {
                    spawn.setLocation(player.getLocation());
                    message.send(player, "&6Spawn relocated");
                } else {
                    spawn.setLocation(player.getLocation());
                    message.send(player, "&6Spawn has been set");
                }
            }
            if (args.length == 1) {
                OfflinePlayer offlinePlayer = server.getOfflinePlayer(args[0]);
                if (userdata.exist(offlinePlayer)) {
                    if (userdata.locationExist(offlinePlayer, "spawn")) {
                        userdata.setLocation(offlinePlayer, "spawn", player.getLocation());
                        message.send(player, offlinePlayer.getName() + "&6's spawn relocated");
                    } else {
                        userdata.setLocation(offlinePlayer, "spawn", player.getLocation());
                        message.send(player, offlinePlayer.getName() + "&6's spawn set");
                    }
                } else {
                    message.send(player, offlinePlayer.getName() + "&c has never joined");
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
                if (player.hasPermission("players.command.setspawn.others")) {
                    for (OfflinePlayer offlinePlayer : server.getOfflinePlayers()) {
                        commands.add(offlinePlayer.getName());
                    }
                }
            }
        }
        return commands;
    }
}
