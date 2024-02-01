package org.achymake.players.data;

import org.achymake.players.Players;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class Worth {
    private final File dataFolder;
    private final Message message;
    public Worth(Players plugin) {
        dataFolder = plugin.getDataFolder();
        message = plugin.getMessage();
    }
    public boolean exist() {
        return getFile().exists();
    }
    public File getFile() {
        return new File(dataFolder, "worth.yml");
    }
    public FileConfiguration getConfig() {
        return YamlConfiguration.loadConfiguration(getFile());
    }
    public List<String> getList() {
        return new ArrayList<>(getConfig().getKeys(false));
    }
    public boolean isSellable(Material material) {
        return getConfig().getKeys(false).contains(material.toString());
    }
    public double getWorth(Material material) {
        return getConfig().getDouble(material.toString());
    }
    public void setWorth(Material material, double value) {
        File file = getFile();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (value > 0.00) {
            config.set(material.toString(), value);
            try {
                config.save(file);
            } catch (IOException e) {
                message.sendLog(Level.WARNING, e.getMessage());
            }
        } else {
            config.set(material.toString(), null);
            try {
                config.save(file);
            } catch (IOException e) {
                message.sendLog(Level.WARNING, e.getMessage());
            }
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
