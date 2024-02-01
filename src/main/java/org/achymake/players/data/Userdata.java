package org.achymake.players.data;

import me.clip.placeholderapi.PlaceholderAPI;
import org.achymake.players.Players;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

public class Userdata {
    private final Players plugin;
    private final File dataFolder;
    private final FileConfiguration config;
    private final Message message;
    private final Server server;
    private final BukkitScheduler scheduler = Bukkit.getScheduler();
    private final List<Player> vanished = new ArrayList<>();
    private final HashMap<String, Long> commandCooldown = new HashMap<>();
    public Userdata(Players plugin) {
        this.plugin = plugin;
        dataFolder = plugin.getDataFolder();
        config = plugin.getConfig();
        message = plugin.getMessage();
        server = plugin.getServer();
    }
    public boolean exist(OfflinePlayer offlinePlayer) {
        return new File(dataFolder, "userdata/" + offlinePlayer.getUniqueId() + ".yml").exists();
    }
    public FileConfiguration getConfig(OfflinePlayer offlinePlayer) {
        return YamlConfiguration.loadConfiguration(new File(dataFolder, "userdata/" + offlinePlayer.getUniqueId() + ".yml"));
    }
    public void setup(OfflinePlayer offlinePlayer) {
        if (exist(offlinePlayer)) {
            if (!getConfig(offlinePlayer).getString("name").equals(offlinePlayer.getName())) {
                File file = new File(dataFolder, "userdata/" + offlinePlayer.getUniqueId() + ".yml");
                FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(file);
                playerConfig.set("name", offlinePlayer.getName());
                try {
                    playerConfig.save(file);
                    message.sendLog(Level.INFO, offlinePlayer.getName() + " has changed name");
                } catch (IOException e) {
                    message.sendLog(Level.WARNING, e.getMessage());
                }
            }
        } else {
            File file = new File(dataFolder, "userdata/" + offlinePlayer.getUniqueId() + ".yml");
            FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(file);
            playerConfig.set("name", offlinePlayer.getName());
            playerConfig.set("display-name", offlinePlayer.getName());
            playerConfig.set("account", config.getDouble("economy.starting-balance"));
            playerConfig.set("settings.pvp", true);
            playerConfig.set("settings.frozen", false);
            playerConfig.set("settings.jailed", false);
            playerConfig.createSection("homes");
            playerConfig.createSection("locations");
            try {
                playerConfig.save(file);
                message.sendLog(Level.INFO, offlinePlayer.getName() + ".yml has been created");
            } catch (IOException e) {
                message.sendLog(Level.WARNING, e.getMessage());
            }
        }
    }
    public void setInt(OfflinePlayer offlinePlayer, String path, int value) {
        File file = new File(dataFolder, "userdata/" + offlinePlayer.getUniqueId() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            message.sendLog(Level.WARNING, e.getMessage());
        }
    }
    public void setDouble(OfflinePlayer offlinePlayer, String path, double value) {
        File file = new File(dataFolder, "userdata/" + offlinePlayer.getUniqueId() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            message.sendLog(Level.WARNING, e.getMessage());
        }
    }
    public void setFloat(OfflinePlayer offlinePlayer, String path, float value) {
        File file = new File(dataFolder, "userdata/" + offlinePlayer.getUniqueId() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            message.sendLog(Level.WARNING, e.getMessage());
        }
    }
    public void setString(OfflinePlayer offlinePlayer, String path, String value) {
        File file = new File(dataFolder, "userdata/" + offlinePlayer.getUniqueId() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            message.sendLog(Level.WARNING, e.getMessage());
        }
    }
    public void setStringList(OfflinePlayer offlinePlayer, String path, List<Object> value) {
        File file = new File(dataFolder, "userdata/" + offlinePlayer.getUniqueId() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            message.sendLog(Level.WARNING, e.getMessage());
        }
    }
    public void setBoolean(OfflinePlayer offlinePlayer, String path, boolean value) {
        File file = new File(dataFolder, "userdata/" + offlinePlayer.getUniqueId() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            message.sendLog(Level.WARNING, e.getMessage());
        }
    }
    public void addTaskID(Player player, String path, int value) {
        setInt(player, "tasks." + path, value);
    }
    public boolean hasTaskID(Player player, String path) {
        return getConfig(player).isInt("tasks." + path);
    }
    public int getTaskID(Player player, String path) {
        return getConfig(player).getInt("tasks." + path);
    }
    public void removeTaskID(Player player, String path) {
        setString(player, "tasks." + path, null);
    }
    public void teleport(Player player, String string, Location location) {
        if (getConfig(player).isInt("tasks.teleport")) {
            message.sendActionBar(player, "&cYou cannot teleport twice you have to wait");
        } else {
            location.getChunk().load();
            message.sendActionBar(player, "&6Teleporting in&f " + config.getInt("teleport.delay") + "&6 seconds");
            int taskID = scheduler.runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    message.sendActionBar(player, "&6Teleporting to&f " + string);
                    player.teleport(location);
                    setString(player, "tasks.teleport", null);
                }
            },config.getInt("teleport.delay") * 20L).getTaskId();
            setInt(player, "tasks.teleport", taskID);
        }
    }
    public boolean hasCooldown(Player player, String command) {
        if (commandCooldown.containsKey(command + "-" + player.getUniqueId())) {
            Long timeElapsed = System.currentTimeMillis() - commandCooldown.get(command + "-" + player.getUniqueId());
            String cooldownTimer = config.getString("commands.cooldown." + command);
            Integer integer = Integer.valueOf(cooldownTimer.replace(cooldownTimer, cooldownTimer + "000"));
            return timeElapsed < integer;
        } else {
            return false;
        }
    }
    public void addCooldown(Player player, String command) {
        if (commandCooldown.containsKey(command + "-" + player.getUniqueId())) {
            Long timeElapsed = System.currentTimeMillis() - commandCooldown.get(command + "-" + player.getUniqueId());
            String cooldownTimer = config.getString("commands.cooldown." + command);
            Integer integer = Integer.valueOf(cooldownTimer.replace(cooldownTimer, cooldownTimer + "000"));
            if (timeElapsed > integer) {
                commandCooldown.put(command + "-" + player.getUniqueId(), System.currentTimeMillis());
            }
        } else {
            commandCooldown.put(command + "-" + player.getUniqueId(), System.currentTimeMillis());
        }
    }
    public String getCooldown(Player player, String command) {
        if (commandCooldown.containsKey(command + "-" + player.getUniqueId())) {
            Long timeElapsed = System.currentTimeMillis() - commandCooldown.get(command + "-" + player.getUniqueId());
            String cooldownTimer = config.getString("commands.cooldown." + command);
            Integer integer = Integer.valueOf(cooldownTimer.replace(cooldownTimer, cooldownTimer + "000"));
            if (timeElapsed < integer) {
                long timer = (integer-timeElapsed);
                return String.valueOf(timer).substring(0, String.valueOf(timer).length() - 3);
            }
        } else {
            return "0";
        }
        return "0";
    }
    public boolean homeExist(OfflinePlayer offlinePlayer, String homeName) {
        return getConfig(offlinePlayer).getConfigurationSection("homes").contains(homeName);
    }
    public List<String> getHomes(OfflinePlayer offlinePlayer) {
        return new ArrayList<>(getConfig(offlinePlayer).getConfigurationSection("homes").getKeys(false));
    }
    public boolean setHome(Player player, String homeName) {
        if (homeExist(player, homeName)) {
            setString(player, "homes." + homeName + ".world", player.getWorld().getName());
            setDouble(player, "homes." + homeName + ".x", player.getLocation().getX());
            setDouble(player, "homes." + homeName + ".y", player.getLocation().getY());
            setDouble(player, "homes." + homeName + ".z", player.getLocation().getZ());
            setFloat(player, "homes." + homeName + ".yaw", player.getLocation().getYaw());
            setFloat(player, "homes." + homeName + ".pitch", player.getLocation().getPitch());
            return true;
        } else {
            for (String rank : config.getConfigurationSection("homes").getKeys(false)) {
                if (player.hasPermission("players.command.sethome.multiple." + rank)) {
                    if (config.getInt("homes." + rank) > getHomes(player).size()) {
                        setString(player, "homes." + homeName + ".world", player.getWorld().getName());
                        setDouble(player, "homes." + homeName + ".x", player.getLocation().getX());
                        setDouble(player, "homes." + homeName + ".y", player.getLocation().getY());
                        setDouble(player, "homes." + homeName + ".z", player.getLocation().getZ());
                        setFloat(player, "homes." + homeName + ".yaw", player.getLocation().getYaw());
                        setFloat(player, "homes." + homeName + ".pitch", player.getLocation().getPitch());
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }
        return false;
    }
    public Location getHome(OfflinePlayer offlinePlayer, String homeName) {
        String worldName = getConfig(offlinePlayer).getString("homes." + homeName + ".world");
        double x = getConfig(offlinePlayer).getDouble("homes." + homeName + ".x");
        double y = getConfig(offlinePlayer).getDouble("homes." + homeName + ".y");
        double z = getConfig(offlinePlayer).getDouble("homes." + homeName + ".z");
        float yaw = getConfig(offlinePlayer).getLong("homes." + homeName + ".yaw");
        float pitch = getConfig(offlinePlayer).getLong("homes." + homeName + ".pitch");
        return new Location(server.getWorld(worldName), x, y, z, yaw, pitch);
    }
    public boolean locationExist(OfflinePlayer offlinePlayer, String locationName) {
        return getConfig(offlinePlayer).getConfigurationSection("locations").contains(locationName);
    }
    public void setLocation(Player player, String locationName) {
        setString(player, "locations." + locationName + ".world", player.getWorld().getName());
        setDouble(player, "locations." + locationName + ".x", player.getLocation().getX());
        setDouble(player, "locations." + locationName + ".y", player.getLocation().getY());
        setDouble(player, "locations." + locationName + ".z", player.getLocation().getZ());
        setFloat(player, "locations." + locationName + ".yaw", player.getLocation().getYaw());
        setFloat(player, "locations." + locationName + ".pitch", player.getLocation().getPitch());
    }
    public void setLocation(OfflinePlayer offlinePlayer, String locationName, Location location) {
        setString(offlinePlayer, "locations." + locationName + ".world", location.getWorld().getName());
        setDouble(offlinePlayer, "locations." + locationName + ".x", location.getX());
        setDouble(offlinePlayer, "locations." + locationName + ".y", location.getY());
        setDouble(offlinePlayer, "locations." + locationName + ".z", location.getZ());
        setFloat(offlinePlayer, "locations." + locationName + ".yaw", location.getYaw());
        setFloat(offlinePlayer, "locations." + locationName + ".pitch", location.getPitch());
    }
    public Location getLocation(OfflinePlayer offlinePlayer, String locationName) {
        String worldName = getConfig(offlinePlayer).getString("locations." + locationName + ".world");
        double x = getConfig(offlinePlayer).getDouble("locations." + locationName + ".x");
        double y = getConfig(offlinePlayer).getDouble("locations." + locationName + ".y");
        double z = getConfig(offlinePlayer).getDouble("locations." + locationName + ".z");
        float yaw = getConfig(offlinePlayer).getLong("locations." + locationName + ".yaw");
        float pitch = getConfig(offlinePlayer).getLong("locations." + locationName + ".pitch");
        return new Location(server.getWorld(worldName), x, y, z, yaw, pitch);
    }
    public void hideVanished(Player player) {
        for (Player vanished : vanished) {
            player.hidePlayer(plugin, vanished);
        }
    }
    public void setVanish(OfflinePlayer offlinePlayer, boolean value) {
        if (value) {
            setBoolean(offlinePlayer,"settings.vanished", true);
            if (offlinePlayer.isOnline()) {
                Player player = offlinePlayer.getPlayer();
                getVanished().add(player);
                if (getConfig(player).getBoolean("settings.coordinates")) {
                    setBoolean(player, "settings.coordinates", false);
                }
                for (Player onlinePlayers : server.getOnlinePlayers()) {
                    onlinePlayers.hidePlayer(plugin, player);
                }
                player.setAllowFlight(true);
                player.setInvulnerable(true);
                player.setSleepingIgnored(true);
                player.setCollidable(false);
                player.setSilent(true);
                player.setCanPickupItems(false);
                for (Player vanishedPlayers : getVanished()) {
                    vanishedPlayers.showPlayer(plugin, player);
                    player.showPlayer(plugin, vanishedPlayers);
                }
                resetTabList();
                message.sendActionBar(player, "&6&lVanish:&a Enabled");
            }
        } else {
            setBoolean(offlinePlayer,"settings.vanished", false);
            if (offlinePlayer.isOnline()) {
                Player player = offlinePlayer.getPlayer();
                getVanished().remove(player);
                for (Player onlinePlayers : server.getOnlinePlayers()) {
                    onlinePlayers.showPlayer(plugin, player);
                }
                if (!player.hasPermission("players.command.fly")) {
                    player.setAllowFlight(false);
                }
                player.setInvulnerable(false);
                player.setSleepingIgnored(false);
                player.setCollidable(true);
                player.setSilent(false);
                player.setCanPickupItems(true);
                for (Player vanishedPlayers : getVanished()) {
                    player.hidePlayer(plugin, vanishedPlayers);
                }
                resetTabList();
                message.sendActionBar(player, "&6&lVanish:&c Disabled");
            }
        }
    }
    public String prefix(Player player) {
        if (PlaceholderAPI.isRegistered("vault")) {
            return message.addColor(PlaceholderAPI.setPlaceholders(player, "%vault_prefix%"));
        } else {
            return "";
        }
    }
    public String getDisplayName(Player player) {
        return message.addColor(getConfig(player).getString("display-name"));
    }
    public String suffix(Player player) {
        if (PlaceholderAPI.isRegistered("vault")) {
            return message.addColor(PlaceholderAPI.setPlaceholders(player, "%vault_suffix%"));
        } else {
            return "";
        }
    }
    public void resetTabList() {
        if (config.getBoolean("tablist.enable")) {
            for (Player players : server.getOnlinePlayers()) {
                players.setPlayerListHeader(message.addColor(config.getString("tablist.header")));
                players.setPlayerListName(prefix(players) + getDisplayName(players) + suffix(players));
                players.setPlayerListFooter(message.addColor(MessageFormat.format(config.getString("tablist.footer"), server.getOnlinePlayers().size() - getVanished().size(), server.getMaxPlayers())));
            }
        }
    }
    public void teleportBack(Player player) {
        if (locationExist(player, "death")) {
            getLocation(player, "death").getChunk().load();
            message.sendActionBar(player, "&6Teleporting to&f death location");
            player.teleport(getLocation(player, "death"));
            setString(player, "locations.death", null);
        } else if (locationExist(player, "recent")) {
            getLocation(player, "recent").getChunk().load();
            message.sendActionBar(player, "&6Teleporting to&f recent location");
            player.teleport(getLocation(player, "recent"));
        } else {
            message.send(player, "&cRecent location either removed or has never been set");
        }
    }
    public Block highestRandomBlock() {
        String worldName = config.getString("commands.rtp.world");
        int x = new Random().nextInt(0, config.getInt("commands.rtp.spread"));
        int z = new Random().nextInt(0, config.getInt("commands.rtp.spread"));
        return server.getWorld(worldName).getHighestBlockAt(x, z);
    }
    public Location randomLocation() {
        Block block = highestRandomBlock();
        if (block.isLiquid()) {
            return randomLocation();
        } else {
            return block.getLocation().add(0.5, 1, 0.5);
        }
    }
    public boolean hasJoined(OfflinePlayer offlinePlayer) {
        if (exist(offlinePlayer)) {
            return getConfig(offlinePlayer).isConfigurationSection("locations.quit");
        } else {
            return false;
        }
    }
    public boolean isPVP(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getBoolean("settings.pvp");
    }
    public boolean isMuted(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getBoolean("settings.muted");
    }
    public boolean isFrozen(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getBoolean("settings.frozen");
    }
    public boolean isJailed(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getBoolean("settings.jailed");
    }
    public boolean isVanished(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getBoolean("settings.vanished");
    }
    public boolean isBanned(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getBoolean("settings.banned");
    }
    public String getBanReason(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getString("settings.ban-reason");
    }
    public ItemStack getOfflinePlayerHead(OfflinePlayer offlinePlayer, int amount) {
        if (offlinePlayer == null) {
            return new ItemStack(Material.PLAYER_HEAD, amount);
        } else {
            ItemStack skullItem = new ItemStack(Material.PLAYER_HEAD, amount);
            SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();
            skullMeta.setOwningPlayer(offlinePlayer);
            skullItem.setItemMeta(skullMeta);
            return skullItem;
        }
    }
    public void reload(OfflinePlayer[] offlinePlayers) {
        for (OfflinePlayer offlinePlayer : offlinePlayers) {
            if (exist(offlinePlayer)) {
                File file = new File(dataFolder, "userdata/" + offlinePlayer.getUniqueId() + ".yml");
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                try {
                    config.load(file);
                } catch (IOException | InvalidConfigurationException e) {
                    message.sendLog(Level.WARNING, e.getMessage());
                }
            }
        }
    }
    public HashMap<String, Long> getCommandCooldown() {
        return commandCooldown;
    }
    public List<Player> getVanished() {
        return vanished;
    }
}
