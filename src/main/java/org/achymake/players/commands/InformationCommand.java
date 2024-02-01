package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class InformationCommand implements CommandExecutor, TabCompleter {
    private final Userdata userdata;
    private final Message message;
    private final Server server;
    public InformationCommand(Players plugin) {
        userdata = plugin.getUserdata();
        message = plugin.getMessage();
        server = plugin.getServer();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                if (userdata.exist(offlinePlayer)) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                    message.send(player, "&6Information:&f " + offlinePlayer.getName());
                    message.send(player, "&6last online:&f " + simpleDateFormat.format(offlinePlayer.getLastSeen()));
                    message.send(player, "&6homes:&f " + userdata.getHomes(offlinePlayer).size());
                    message.send(player, "&6muted:&f " + userdata.isMuted(offlinePlayer));
                    message.send(player, "&6frozen:&f " + userdata.isFrozen(offlinePlayer));
                    message.send(player, "&6jailed:&f " + userdata.isJailed(offlinePlayer));
                    message.send(player, "&6pvp:&f " + userdata.isPVP(offlinePlayer));
                    message.send(player, "&6banned:&f " + userdata.isBanned(offlinePlayer));
                    message.send(player, "&6ban-reason:&f " + userdata.getBanReason(offlinePlayer));
                    message.send(player, "&6vanished:&f " + userdata.isVanished(offlinePlayer));
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
        if (sender instanceof Player) {
            if (args.length == 1) {
                for (OfflinePlayer offlinePlayer : server.getOfflinePlayers()) {
                    commands.add(offlinePlayer.getName());
                }
            }
        }
        return commands;
    }
}