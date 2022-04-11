package sinan.minecraftinventorylog.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import sinan.minecraftinventorylog.MinecraftInventoryLog;
import sinan.minecraftinventorylog.util.InventoryConfigManager;

public class PlayerListener implements Listener {
    public MinecraftInventoryLog plugin;

    public PlayerListener(MinecraftInventoryLog plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        //InventoryConfigManager.saveInventory(plugin, e.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        InventoryConfigManager.saveInventory(plugin, e.getPlayer());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        InventoryConfigManager.saveInventory(plugin, e.getEntity());
    }
}
