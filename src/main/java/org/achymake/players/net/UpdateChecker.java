package org.achymake.players.net;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.logging.Level;

public class UpdateChecker {
    private final Players plugin;
    private final PluginDescriptionFile description;
    private final FileConfiguration config;
    private final Server server;
    private final Message message;
    public UpdateChecker(Players plugin) {
        this.plugin = plugin;
        description = plugin.getDescription();
        config = plugin.getConfig();
        server = plugin.getServer();
        message = plugin.getMessage();
    }
    public void sendUpdate(Player player) {
        if (notifyUpdate()) {
            if (player.hasPermission("players.event.join.update")) {
                getLatest((latest) -> {
                    if (!description.getVersion().equals(latest)) {
                        message.send(player,"&6" + description.getName() + " Update:&f " + latest);
                        message.send(player,"&6Current Version: &f" + description.getVersion());
                    }
                });
            }
        }
    }
    public void sendUpdate() {
        if (notifyUpdate()) {
            server.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                @Override
                public void run() {
                    getLatest((latest) -> {
                        message.sendLog(Level.INFO, "Checking latest release");
                        if (description.getVersion().equals(latest)) {
                            message.sendLog(Level.INFO, "You are using the latest version");
                        } else {
                            message.sendLog(Level.INFO, "New Update: " + latest);
                            message.sendLog(Level.INFO, "Current Version: " + description.getVersion());
                        }
                    });
                }
            });
        }
    }
    private void getLatest(Consumer<String> consumer) {
        try {
            InputStream inputStream = (new URL("https://api.spigotmc.org/legacy/update.php?resource=" + 110266)).openStream();
            Scanner scanner = new Scanner(inputStream);
            if (scanner.hasNext()) {
                consumer.accept(scanner.next());
                scanner.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            message.sendLog(Level.WARNING, e.getMessage());
        }
    }
    private boolean notifyUpdate() {
        return config.getBoolean("notify-update");
    }
}
