package org.achymake.players.listeners;

import org.achymake.players.Players;
import org.achymake.players.data.Message;
import org.achymake.players.data.Userdata;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntity implements Listener {
    private final Userdata userdata;
    private final Message message;
    public EntityDamageByEntity(Players plugin) {
        userdata = plugin.getUserdata();
        message = plugin.getMessage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamageByArrow(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow arrow) {
            if (!(arrow.getShooter() instanceof Player player))return;
            if (userdata.isFrozen(player) || userdata.isJailed(player)) {
                event.setCancelled(true);
            } else {
                if (event.getEntity() instanceof Player target) {
                    if (!userdata.isPVP(player)) {
                        event.setCancelled(true);
                        message.sendActionBar(player, "&c&lHey!&7 Sorry, but you're PVP is Disabled");
                    } else if (!userdata.isPVP(target)) {
                        event.setCancelled(true);
                        message.sendActionBar(player, "&c&lHey!&7 Sorry, but&f " + target.getName() + "&7 PVP is Disabled");
                    }
                }
            }
        } else if (event.getDamager() instanceof Player player) {
            if (userdata.isFrozen(player) || userdata.isJailed(player)) {
                event.setCancelled(true);
            } else {
                if (event.getEntity() instanceof Player target) {
                    if (!userdata.isPVP(player)) {
                        event.setCancelled(true);
                        message.sendActionBar(player,"&c&lHey!&7 Sorry, but you're PVP is Disabled");
                    } else if (!userdata.isPVP(target)) {
                        event.setCancelled(true);
                        message.sendActionBar(player,"&c&lHey!&7 Sorry, but&f " + target.getName() + "&7 PVP is Disabled");
                    }
                }
            }
        } else if (event.getDamager() instanceof Snowball snowball) {
            if (!(snowball.getShooter() instanceof Player player))return;
            if (userdata.isFrozen(player) || userdata.isJailed(player)) {
                event.setCancelled(true);
            } else {
                if (event.getEntity() instanceof Player target) {
                    if (!userdata.isPVP(player)) {
                        event.setCancelled(true);
                        message.sendActionBar(player, "&c&lHey!&7 Sorry, but you're PVP is Disabled");
                    } else if (!userdata.isPVP(target)) {
                        event.setCancelled(true);
                        message.sendActionBar(player, "&c&lHey!&7 Sorry, but&f " + target.getName() + "&7 PVP is Disabled");
                    }
                }
            }
        } else if (event.getDamager() instanceof SpectralArrow spectralArrow) {
            if (!(spectralArrow.getShooter() instanceof Player player))return;
            if (userdata.isFrozen(player) || userdata.isJailed(player)) {
                event.setCancelled(true);
            } else {
                if (event.getEntity() instanceof Player target) {
                    if (!userdata.isPVP(player)) {
                        event.setCancelled(true);
                        message.sendActionBar(player, "&c&lHey!&7 Sorry, but you're PVP is Disabled");
                    } else if (!userdata.isPVP(target)) {
                        event.setCancelled(true);
                        message.sendActionBar(player, "&c&lHey!&7 Sorry, but&f " + target.getName() + "&7 PVP is Disabled");
                    }
                }
            }
        } else if (event.getDamager() instanceof ThrownPotion thrownPotion) {
            if (!(thrownPotion.getShooter() instanceof Player player))return;
            if (userdata.isFrozen(player) || userdata.isJailed(player)) {
                event.setCancelled(true);
            } else {
                if (event.getEntity() instanceof Player target) {
                    if (!userdata.isPVP(player)) {
                        event.setCancelled(true);
                        message.sendActionBar(player, "&c&lHey!&7 Sorry, but you're PVP is Disabled");
                    } else if (!userdata.isPVP(target)) {
                        event.setCancelled(true);
                        message.sendActionBar(player, "&c&lHey!&7 Sorry, but&f " + target.getName() + "&7 PVP is Disabled");
                    }
                }
            }
        } else if (event.getDamager() instanceof Trident trident) {
            if (!(trident.getShooter() instanceof Player player))return;
            if (userdata.isFrozen(player) || userdata.isJailed(player)) {
                event.setCancelled(true);
            } else {
                if (event.getEntity() instanceof Player target) {
                    if (!userdata.isPVP(player)) {
                        event.setCancelled(true);
                        message.sendActionBar(player, "&c&lHey!&7 Sorry, but you're PVP is Disabled");
                    } else if (!userdata.isPVP(target)) {
                        event.setCancelled(true);
                        message.sendActionBar(player, "&c&lHey!&7 Sorry, but&f " + target.getName() + "&7 PVP is Disabled");
                    }
                }
            }
        }
    }
}
