package org.achymake.players.api;

import net.milkbowl.vault.economy.EconomyResponse;
import org.achymake.players.Players;
import org.achymake.players.data.Economy;
import org.achymake.players.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

public class VaultEconomyProvider implements net.milkbowl.vault.economy.Economy {
    private final boolean enabled;
    private final String name;
    private final FileConfiguration config;
    private final Userdata userdata;
    private final Economy economy;
    private final Server server;
    public VaultEconomyProvider(Players plugin) {
        enabled = plugin.isEnabled();
        name = plugin.getName();
        config = plugin.getConfig();
        userdata = plugin.getUserdata();
        economy = plugin.getEconomy();
        server = plugin.getServer();
    }
    public boolean isEnabled() {
        return enabled;
    }
    public String getName() {
        return name;
    }
    public boolean hasBankSupport() {
        return false;
    }
    public int fractionalDigits() {
        return -1;
    }
    public String format(double amount) {
        return new DecimalFormat(config.getString("economy.format")).format(amount);
    }
    public String currencyNamePlural() {
        return currencyNameSingular();
    }
    public String currencyNameSingular() {
        return config.getString("economy.currency");
    }
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return userdata.exist(offlinePlayer);
    }
    public boolean hasAccount(String playerName) {
        return userdata.exist(server.getOfflinePlayer(playerName));
    }
    public boolean hasAccount(String playerName, String worldName) {
        return hasAccount(playerName);
    }
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return hasAccount(player);
    }
    public double getBalance(OfflinePlayer offlinePlayer) {
        return economy.getEconomy(offlinePlayer);
    }
    public double getBalance(String playerName) {
        return economy.getEconomy(server.getOfflinePlayer(playerName));
    }
    public double getBalance(String playerName, String world) {
        return getBalance(playerName);
    }
    public double getBalance(OfflinePlayer player, String world) {
        return getBalance(player);
    }
    public boolean has(OfflinePlayer offlinePlayer, double amount) {
        return economy.getEconomy(offlinePlayer) >= amount;
    }
    public boolean has(String playerName, double amount) {
        return economy.getEconomy(server.getOfflinePlayer(playerName)) >= amount;
    }
    public boolean has(String playerName, String worldName, double amount) {
        return has(playerName, amount);
    }

    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return has(player, amount);
    }
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double amount) {
        if (offlinePlayer == null) {
            return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Player cannot be null!");
        } else if (amount < 0.0) {
            return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Cannot withdraw negative funds!");
        } else {
            economy.removeEconomy(offlinePlayer, amount);
            return new EconomyResponse(amount, getBalance(offlinePlayer), EconomyResponse.ResponseType.SUCCESS, null);
        }
    }
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        if (playerName == null) {
            return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Player name cannot be null!");
        } else if (amount < 0.0) {
            return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Cannot withdraw negative funds!");
        } else {
            economy.removeEconomy(server.getOfflinePlayer(playerName), amount);
            return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.SUCCESS, null);
        }
    }
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdrawPlayer(playerName, amount);
    }
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return withdrawPlayer(player, amount);
    }
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double amount) {
        if (offlinePlayer == null) {
            return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Player can not be null.");
        } else if (amount < 0.0) {
            return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Cannot deposit negative funds");
        } else {
            economy.addEconomy(offlinePlayer, amount);
            return new EconomyResponse(amount, getBalance(offlinePlayer), EconomyResponse.ResponseType.SUCCESS, null);
        }
    }
    public EconomyResponse depositPlayer(String playerName, double amount) {
        if (playerName == null) {
            return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Player name can not be null.");
        } else if (amount < 0.0) {
            return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Cannot deposit negative funds");
        } else {
            economy.addEconomy(server.getOfflinePlayer(playerName), amount);
            return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.SUCCESS, null);
        }
    }
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(playerName, amount);
    }
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return depositPlayer(player, amount);
    }
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        userdata.setup(offlinePlayer);
        return true;
    }
    public boolean createPlayerAccount(String playerName) {
        userdata.setup(server.getOfflinePlayer(playerName));
        return true;
    }
    public boolean createPlayerAccount(String playerName, String worldName) {
        return createPlayerAccount(playerName);
    }
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return createPlayerAccount(player);
    }
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy does not support bank accounts!");
    }
    public EconomyResponse createBank(String name, String player) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy does not support bank accounts!");
    }
    public EconomyResponse deleteBank(String name) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy does not support bank accounts!");
    }
    public EconomyResponse bankBalance(String name) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy does not support bank accounts!");
    }
    public EconomyResponse bankHas(String name, double amount) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy does not support bank accounts!");
    }
    public EconomyResponse bankWithdraw(String name, double amount) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy does not support bank accounts!");
    }
    public EconomyResponse bankDeposit(String name, double amount) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy does not support bank accounts!");
    }
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy does not support bank accounts!");
    }
    public EconomyResponse isBankOwner(String name, String playerName) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy does not support bank accounts!");
    }
    public EconomyResponse isBankMember(String name, String playerName) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy does not support bank accounts!");
    }
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy does not support bank accounts!");
    }
    public List<String> getBanks() {
        return Collections.emptyList();
    }
}
