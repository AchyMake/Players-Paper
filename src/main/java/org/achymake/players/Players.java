package org.achymake.players;

import org.achymake.players.api.VaultEconomyProvider;
import org.achymake.players.api.PlaceholderProvider;
import org.achymake.players.commands.*;
import org.achymake.players.data.*;
import org.achymake.players.listeners.*;
import org.achymake.players.net.Discord;
import org.achymake.players.net.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public final class Players extends JavaPlugin {
    private static Players instance;
    private static Userdata userdata;
    private static Economy economy;
    private static Jail jail;
    private static Kits kits;
    private static Spawn spawn;
    private static Warps warps;
    private static Worth worth;
    private static Message message;
    private static VaultEconomyProvider vaultEconomyProvider;
    private static PluginManager pluginManager;
    private static UpdateChecker updateChecker;
    private static Discord discord;
    @Override
    public void onEnable() {
        instance = this;
        message = new Message(this);
        discord = new Discord(this);
        userdata = new Userdata(this);
        economy = new Economy(this);
        jail = new Jail(this);
        kits = new Kits(this);
        spawn = new Spawn(this);
        warps = new Warps(this);
        worth = new Worth(this);
        pluginManager = getServer().getPluginManager();
        updateChecker = new UpdateChecker(this);
        if (getServer().getPluginManager().isPluginEnabled("Vault")) {
            vaultEconomyProvider = new VaultEconomyProvider(this);
            getServer().getServicesManager().register(net.milkbowl.vault.economy.Economy.class, getEconomyProvider(), this, ServicePriority.Normal);
            getMessage().sendLog(Level.INFO, "Hooked to 'Vault'");
        } else {
            getServer().getPluginManager().disablePlugin(this);
            getMessage().sendLog(Level.WARNING, "You have to install 'Vault'");
        }
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderProvider().register();
            getMessage().sendLog(Level.INFO, "Hooked to 'PlaceholderAPI'");
        } else {
            getServer().getPluginManager().disablePlugin(this);
            getMessage().sendLog(Level.WARNING, "You have to install 'PlaceholderAPI'");
        }
        registerCommands();
        registerListeners();
        reload();
        getDiscord().send(getServer().getVersion(), "Server has Started");
        getMessage().sendLog(Level.INFO, "Enabled " + getDescription().getName() + " " + getDescription().getVersion());
        getUpdateChecker().sendUpdate();
    }
    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        getDiscord().send(getServer().getVersion(), "Server has Stopped");
        getMessage().sendLog(Level.INFO, "Disabled " + getDescription().getName() + " " + getDescription().getVersion());
    }
    private void registerCommands() {
        getCommand("announcement").setExecutor(new AnnouncementCommand(this));
        getCommand("anvil").setExecutor(new AnvilCommand(this));
        getCommand("back").setExecutor(new BackCommand(this));
        getCommand("balance").setExecutor(new BalanceCommand(this));
        getCommand("color").setExecutor(new ColorCommand());
        getCommand("delhome").setExecutor(new DelHomeCommand(this));
        getCommand("delwarp").setExecutor(new DelWarpCommand(this));
        getCommand("eco").setExecutor(new EcoCommand(this));
        getCommand("enchant").setExecutor(new EnchantCommand(this));
        getCommand("enderchest").setExecutor(new EnderChestCommand(this));
        getCommand("feed").setExecutor(new FeedCommand(this));
        getCommand("fly").setExecutor(new FlyCommand(this));
        getCommand("flyspeed").setExecutor(new FlySpeedCommand(this));
        getCommand("freeze").setExecutor(new FreezeCommand(this));
        getCommand("gamemode").setExecutor(new GameModeCommand(this));
        getCommand("gma").setExecutor(new GMACommand(this));
        getCommand("gmc").setExecutor(new GMCCommand(this));
        getCommand("gms").setExecutor(new GMSCommand(this));
        getCommand("gmsp").setExecutor(new GMSPCommand(this));
        getCommand("hat").setExecutor(new HatCommand(this));
        getCommand("heal").setExecutor(new HealCommand(this));
        getCommand("help").setExecutor(new HelpCommand(this));
        getCommand("home").setExecutor(new HomeCommand(this));
        getCommand("homes").setExecutor(new HomesCommand(this));
        getCommand("information").setExecutor(new InformationCommand(this));
        getCommand("inventory").setExecutor(new InventoryCommand(this));
        getCommand("jail").setExecutor(new JailCommand(this));
        getCommand("kit").setExecutor(new KitCommand(this));
        getCommand("motd").setExecutor(new MOTDCommand(this));
        getCommand("mute").setExecutor(new MuteCommand(this));
        getCommand("nickname").setExecutor(new NicknameCommand(this));
        getCommand("pay").setExecutor(new PayCommand(this));
        getCommand("players").setExecutor(new PlayersCommand(this));
        getCommand("pvp").setExecutor(new PVPCommand(this));
        getCommand("repair").setExecutor(new RepairCommand(this));
        getCommand("respond").setExecutor(new RespondCommand(this));
        getCommand("rtp").setExecutor(new RTPCommand(this));
        getCommand("rules").setExecutor(new RulesCommand(this));
        getCommand("sell").setExecutor(new SellCommand(this));
        getCommand("sethome").setExecutor(new SetHomeCommand(this));
        getCommand("setjail").setExecutor(new SetJailCommand(this));
        getCommand("setspawn").setExecutor(new SetSpawnCommand(this));
        getCommand("setwarp").setExecutor(new SetWarpCommand(this));
        getCommand("setworth").setExecutor(new SetWorthCommand(this));
        getCommand("skull").setExecutor(new SkullCommand(this));
        getCommand("spawn").setExecutor(new SpawnCommand(this));
        getCommand("tpaccept").setExecutor(new TPAcceptCommand(this));
        getCommand("tpa").setExecutor(new TPACommand(this));
        getCommand("tpcancel").setExecutor(new TPCancelCommand(this));
        getCommand("tp").setExecutor(new TPCommand(this));
        getCommand("tpdeny").setExecutor(new TPDenyCommand(this));
        getCommand("tphere").setExecutor(new TPHereCommand(this));
        getCommand("uuid").setExecutor(new UUIDCommand(this));
        getCommand("vanish").setExecutor(new VanishCommand(this));
        getCommand("walkspeed").setExecutor(new WalkSpeedCommand(this));
        getCommand("warp").setExecutor(new WarpCommand(this));
        getCommand("whisper").setExecutor(new WhisperCommand(this));
        getCommand("workbench").setExecutor(new WorkbenchCommand(this));
        getCommand("worth").setExecutor(new WorthCommand(this));
    }
    private void registerListeners() {
        getPluginManager().registerEvents(new AsyncPlayerChat(this), this);
        getPluginManager().registerEvents(new BlockBreak(this), this);
        getPluginManager().registerEvents(new BlockFertilize(this), this);
        getPluginManager().registerEvents(new BlockPlace(this), this);
        getPluginManager().registerEvents(new BlockReceiveGame(this), this);
        getPluginManager().registerEvents(new EntityDamageByEntity(this), this);
        getPluginManager().registerEvents(new PlayerBucketEmpty(this), this);
        getPluginManager().registerEvents(new PlayerBucketEntity(this), this);
        getPluginManager().registerEvents(new PlayerBucketFill(this), this);
        getPluginManager().registerEvents(new PlayerCommandPreprocess(this), this);
        getPluginManager().registerEvents(new PlayerDeath(this), this);
        getPluginManager().registerEvents(new PlayerHarvestBlock(this), this);
        getPluginManager().registerEvents(new PlayerInteractPhysical(this), this);
        getPluginManager().registerEvents(new PlayerJoin(this), this);
        getPluginManager().registerEvents(new PlayerLeashEntity(this), this);
        getPluginManager().registerEvents(new PlayerLogin(this), this);
        getPluginManager().registerEvents(new PlayerMount(this), this);
        getPluginManager().registerEvents(new PlayerMove(this), this);
        getPluginManager().registerEvents(new PlayerQuit(this), this);
        getPluginManager().registerEvents(new PlayerRespawn(this), this);
        getPluginManager().registerEvents(new PlayerShearEntity(this), this);
        getPluginManager().registerEvents(new PlayerSpawnLocation(this), this);
        getPluginManager().registerEvents(new PlayerTeleport(this), this);
        getPluginManager().registerEvents(new PrepareAnvil(this), this);
        getPluginManager().registerEvents(new SignChange(this), this);
    }
    public void reload() {
        File file = new File(getDataFolder(), "config.yml");
        if (file.exists()) {
            try {
                getConfig().load(file);
                getConfig().save(file);
            } catch (IOException | InvalidConfigurationException e) {
                getMessage().sendLog(Level.WARNING, e.getMessage());
            }
        } else {
            getConfig().options().copyDefaults(true);
            try {
                getConfig().save(file);
            } catch (IOException e) {
                getMessage().sendLog(Level.WARNING, e.getMessage());
            }
        }
        getJail().reload();
        getKits().reload();
        getSpawn().reload();
        getWarps().reload();
        getWorth().reload();
        getUserdata().reload(getServer().getOfflinePlayers());
        getUserdata().resetTabList();
    }
    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }
    public PluginManager getPluginManager() {
        return pluginManager;
    }
    public VaultEconomyProvider getEconomyProvider() {
        return vaultEconomyProvider;
    }
    public Worth getWorth() {
        return worth;
    }
    public Warps getWarps() {
        return warps;
    }
    public Spawn getSpawn() {
        return spawn;
    }
    public Kits getKits() {
        return kits;
    }
    public Jail getJail() {
        return jail;
    }
    public Economy getEconomy() {
        return economy;
    }
    public Userdata getUserdata() {
        return userdata;
    }
    public Discord getDiscord() {
        return discord;
    }
    public Message getMessage() {
        return message;
    }
    public static Players getInstance() {
        return instance;
    }
}