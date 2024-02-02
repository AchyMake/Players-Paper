package org.achymake.players.data;

import org.achymake.players.Players;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class Spawn {
    private final File dataFolder;
    private final Message message;
    private final Server server;
    public Spawn(Players plugin) {
        dataFolder = plugin.getDataFolder();
        message = plugin.getMessage();
        server = plugin.getServer();
    }
    public boolean exist() {
        return getFile().exists();
    }
    private File getFile() {
        return new File(dataFolder, "spawn.yml");
    }
    public FileConfiguration getConfig() {
        return YamlConfiguration.loadConfiguration(getFile());
    }
    public boolean locationExist() {
        return getConfig().isConfigurationSection("spawn");
    }
    public void setLocation(Location location) {
        File file = getFile();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("spawn.world", location.getWorld().getName());
        config.set("spawn.x", location.getX());
        config.set("spawn.y", location.getY());
        config.set("spawn.z", location.getZ());
        config.set("spawn.yaw", location.getYaw());
        config.set("spawn.pitch", location.getPitch());
        try {
            config.save(file);
        } catch (IOException e) {
            message.sendLog(Level.WARNING, e.getMessage());
        }
    }
    public Location getLocation() {
        if (locationExist()) {
            String worldName = getConfig().getString("spawn.world");
            double x = getConfig().getDouble("spawn.x");
            double y = getConfig().getDouble("spawn.y");
            double z = getConfig().getDouble("spawn.z");
            float yaw = getConfig().getLong("spawn.yaw");
            float pitch = getConfig().getLong("spawn.pitch");
            return new Location(server.getWorld(worldName), x, y, z, yaw, pitch);
        } else {
            return null;
        }
    }
    public void reload() {
        File file = getFile();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (exist()) {
            try {
                config.load(file);
            } catch (IOException | InvalidConfigurationException e) {
                message.sendLog(Level.WARNING, e.getMessage());
            }
        } else {
            config.options().copyDefaults(true);
            try {
                config.save(file);
            } catch (IOException e) {
                message.sendLog(Level.WARNING, e.getMessage());
            }
        }
    }
}
