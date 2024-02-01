package org.achymake.players.data;

import org.achymake.players.Players;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;

import java.text.DecimalFormat;

public class Economy {
    private final Userdata userdata;
    private final FileConfiguration config;
    public Economy(Players plugin) {
        userdata = plugin.getUserdata();
        config = plugin.getConfig();
    }
    public double getEconomy(OfflinePlayer offlinePlayer) {
        return userdata.getConfig(offlinePlayer).getDouble("account");
    }
    public void addEconomy(OfflinePlayer offlinePlayer, double amount) {
        userdata.setDouble(offlinePlayer, "account", amount + getEconomy(offlinePlayer));
    }
    public void removeEconomy(OfflinePlayer offlinePlayer, double amount) {
        userdata.setDouble(offlinePlayer, "account", getEconomy(offlinePlayer) - amount);
    }
    public void setEconomy(OfflinePlayer offlinePlayer, double value) {
        userdata.setDouble(offlinePlayer, "account", value);
    }
    public void resetEconomy(OfflinePlayer offlinePlayer) {
        userdata.setDouble(offlinePlayer, "account", config.getDouble("economy.starting-balance"));
    }
    public String getFormat(double amount) {
        return new DecimalFormat(config.getString("economy.format")).format(amount);
    }
    public String getCurrency() {
        return config.getString("economy.currency");
    }
}
