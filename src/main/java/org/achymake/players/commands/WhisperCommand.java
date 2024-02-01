package org.achymake.players.commands;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WhisperCommand implements CommandExecutor, TabCompleter {
    private final Userdata userdata;
    private final Message message;
    private final Server server;
    public WhisperCommand(Players players) {
        userdata = players.getUserdata();
        message = players.getMessage();
        server = players.getServer();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (userdata.isMuted(player) || userdata.isJailed(player)) {
                return false;
            }
            if (args.length > 1) {
                Player target = server.getPlayerExact(args[0]);
                if (target != null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for(int i = 1; i < args.length; i++) {
                        stringBuilder.append(args[i]);
                        stringBuilder.append(" ");
                    }
                    String builder = stringBuilder.toString().strip();
                    message.send(player, "&7You > " + target.getName() + ": " + builder);
                    message.send(target, "&7" + player.getName() + " > You: " + builder);
                    userdata.setString(target, "last-whisper", target.getUniqueId().toString());
                    for (Player players : server.getOnlinePlayers()) {
                        if (players.hasPermission("players.notify.whispers")) {
                            message.send(players, "&7" + player.getName() + " > " + target.getName() + ": " + builder);
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
                for (Player players : server.getOnlinePlayers()) {
                    commands.add(players.getName());
                }
            }
        }
        return commands;
    }
}
