package sinanakkoyun.minecraftinventorylog;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import sinanakkoyun.minecraftinventorylog.constants.ItemStackConstants;

import java.io.File;
import java.util.Iterator;

public final class MinecraftInventoryLog extends JavaPlugin {
    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new GUIListener(this), this);
        this.getCommand("restore").setExecutor(new RestoreCommand(this));

        //this.getConfig().options().copyDefaults();
        if (!(new File(this.getDataFolder(), "config.yml")).exists()) {
            this.saveDefaultConfig();
        }

        ItemStackConstants.init();

        System.out.println(ChatColor.GREEN + "[Deployed] " + ChatColor.AQUA + "Sinan's Inventory Backup System");
    }

    @Override
    public void onDisable() {
        Iterator var2 = Bukkit.getOnlinePlayers().iterator();

        while(var2.hasNext()) {
            InventoryConfigManager.saveInventory(this, (Player)var2.next());
        }

        this.saveConfig();

        System.out.println(ChatColor.RED + "[Shutdown] " + ChatColor.AQUA + "Sinan's Inventory Backup System");
    }
}
