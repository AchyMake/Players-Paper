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

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class RespondCommand implements CommandExecutor, TabCompleter {
    private final Userdata userdata;
    private final Message message;
    private final Server server;
    public RespondCommand(Players plugin) {
        userdata = plugin.getUserdata();
        message = plugin.getMessage();
        server = plugin.getServer();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (!userdata.isMuted(player)) {
                if (args.length > 0) {
                    if (userdata.getConfig(player).isString("last-whisper")) {
                        String uuidString = userdata.getConfig(player).getString("last-whisper");
                        UUID uuid = UUID.fromString(uuidString);
                        Player target = player.getServer().getPlayer(uuid);
                        if (target != null) {
                            StringBuilder stringBuilder = new StringBuilder();
                            for (String words : args) {
                                stringBuilder.append(words);
                                stringBuilder.append(" ");
                            }
                            String builder = stringBuilder.toString().strip();
                            message.send(player, "&7You > " + target.getName() + ": " + builder);
                            message.send(target, "&7" + player.getName() + " > You: " + builder);
                            userdata.setString(target, "last-whisper", player.getUniqueId().toString());
                            for (Player players : server.getOnlinePlayers()) {
                                if (players.hasPermission("players.notify.whispers")) {
                                    message.send(players, "&7" + player.getName() + " > " + target.getName() + ": " + builder);
                                }
                            }
                        }
                    }
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
