package sinan.minecraftinventorylog;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import sinan.minecraftinventorylog.serializer.ItemStackSerializer;
import sinan.minecraftinventorylog.commands.RestoreCommand;
import sinan.minecraftinventorylog.constants.ItemStackConstants;
import sinan.minecraftinventorylog.listeners.GUIListener;
import sinan.minecraftinventorylog.listeners.PlayerListener;
import sinan.minecraftinventorylog.mysql.MySQL;
import sinan.minecraftinventorylog.types.InventoryEntry;
import sinan.minecraftinventorylog.util.InventoryConfigManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.logging.Level;

public final class MinecraftInventoryLog extends JavaPlugin {
    FileConfiguration config = this.getConfig();
    public MySQL sql;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new GUIListener(this), this);
        this.getCommand("invrestore").setExecutor(new RestoreCommand(this));

        ItemStackConstants.init();

        try {
            initConfig();
        } catch(Exception e) {
            getLogger().log(Level.SEVERE, e.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        sql = new MySQL(config.getString("sql.host"),
                config.getString("sql.port"),
                config.getString("sql.database"),
                config.getString("sql.username"),
                config.getString("sql.password"));

        sql.connect();

        setupDefaultTables();

        getLogger().log(Level.INFO, ChatColor.RED + "[Startup] " + ChatColor.AQUA + "Sinan's Inventory Backup System");

        //1500 bytes per max item stack * 41 slots
        /*
        ItemStack maxSize = new ItemStack(Material.HEART_OF_THE_SEA, 64);
        ItemMeta maxMeta = maxSize.getItemMeta();
        for(Enchantment enchantment : Enchantment.values()) {
            maxMeta.addEnchant(enchantment, 2147483647, true);
        }
        maxMeta.setDisplayName(String.join("", ChatColor.values().toString()) + new String(new char[700]).replace("\0", "a"));
        maxMeta.addItemFlags(ItemFlag.values());
        maxMeta.setLore(Arrays.asList(String.join("", ChatColor.values().toString()) + new String(new char[700]).replace("\0", "a"),
                String.join("", ChatColor.values().toString()) + new String(new char[700]).replace("\0", "a"),
                String.join("", ChatColor.values().toString()) + new String(new char[700]).replace("\0", "a"),
                String.join("", ChatColor.values().toString()) + new String(new char[700]).replace("\0", "a"),
                String.join("", ChatColor.values().toString()) + new String(new char[700]).replace("\0", "a"),
                String.join("", ChatColor.values().toString()) + new String(new char[700]).replace("\0", "a"),
                String.join("", ChatColor.values().toString()) + new String(new char[700]).replace("\0", "a"),
                String.join("", ChatColor.values().toString()) + new String(new char[700]).replace("\0", "a"),
                String.join("", ChatColor.values().toString()) + new String(new char[700]).replace("\0", "a"),
                String.join("", ChatColor.values().toString()) + new String(new char[700]).replace("\0", "a"),
                String.join("", ChatColor.values().toString()) + new String(new char[700]).replace("\0", "a"),
                String.join("", ChatColor.values().toString()) + new String(new char[700]).replace("\0", "a"),
                String.join("", ChatColor.values().toString()) + new String(new char[700]).replace("\0", "a"),
                String.join("", ChatColor.values().toString()) + new String(new char[700]).replace("\0", "a"),
                String.join("", ChatColor.values().toString()) + new String(new char[700]).replace("\0", "a"),
                String.join("", ChatColor.values().toString()) + new String(new char[700]).replace("\0", "a"),
                String.join("", ChatColor.values().toString()) + new String(new char[700]).replace("\0", "a"),
                String.join("", ChatColor.values().toString()) + new String(new char[700]).replace("\0", "a"),
                String.join("", ChatColor.values().toString()) + new String(new char[700]).replace("\0", "a"),
                String.join("", ChatColor.values().toString()) + new String(new char[700]).replace("\0", "a"),
                String.join("", ChatColor.values().toString()) + new String(new char[700]).replace("\0", "a"),
                String.join("", ChatColor.values().toString()) + new String(new char[700]).replace("\0", "a"),
                String.join("", ChatColor.values().toString()) + new String(new char[700]).replace("\0", "a"),
                String.join("", ChatColor.values().toString()) + new String(new char[700]).replace("\0", "a"),
                String.join("", ChatColor.values().toString()) + new String(new char[700]).replace("\0", "a"),
                String.join("", ChatColor.values().toString()) + new String(new char[700]).replace("\0", "a"),
                String.join("", ChatColor.values().toString()) + new String(new char[700]).replace("\0", "a"),
                String.join("", ChatColor.values().toString()) + new String(new char[700]).replace("\0", "a"),
                String.join("", ChatColor.values().toString()) + new String(new char[700]).replace("\0", "a"),
                String.join("", ChatColor.values().toString()) + new String(new char[700]).replace("\0", "a"),
                String.join("", ChatColor.values().toString()) + new String(new char[700]).replace("\0", "a")
                ));
        maxSize.setItemMeta(maxMeta);

        ItemStack[] contents = new ItemStack[54];
        for(int i=0; i<54; i++) {
            contents[i] = maxSize;
        }

        try {
            getLogger().log(Level.INFO, SerializationUtils.serialize(new ArrayList<ItemStack>(Arrays.asList(contents))).length + " bytes in max item stack!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Inventory inv = Bukkit.createInventory(null, 54);
        inv.setContents(contents);
        inv.setItem(0, maxSize);
        Bukkit.getPlayer("Console_Bot").openInventory(inv);
*/
    }

    @Override
    public void onDisable() {
        Iterator var2 = Bukkit.getOnlinePlayers().iterator();
        while(var2.hasNext()) {
            InventoryConfigManager.saveInventory(this, (Player)var2.next());
        }

        try {
            sql.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        getLogger().log(Level.INFO, ChatColor.RED + "[Shutdown] " + ChatColor.AQUA + "Sinan's Inventory Backup System");
    }

    private void setupDefaultTables() {
        try {
            sql.con.prepareStatement("CREATE TABLE IF NOT EXISTS invlog (date TIMESTAMP NOT NULL, UUID VARCHAR(40) NOT NULL, inv BLOB(61500))").executeUpdate();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    private void initConfig() throws Exception {
        config.addDefault("sql.host", "default");
        config.addDefault("sql.port", "default");
        config.addDefault("sql.database", "default");
        config.addDefault("sql.username", "default");
        config.addDefault("sql.password", "default");
        config.options().copyDefaults(true);
        saveConfig();

        if(config.getString("sql.host").equals("default")) {
            throw new Exception("SQL host not set.");
        }
        if(config.getString("sql.port").equals("default")) {
            throw new Exception("SQL port not set.");
        }
        if(config.getString("sql.database").equals("default")) {
            throw new Exception("SQL database not set.");
        }
        if(config.getString("sql.username").equals("default")) {
            throw new Exception("SQL username not set.");
        }
        if(config.getString("sql.password").equals("default")) {
            throw new Exception("SQL password not set.");
        }
    }

    public void logInv(Player player) throws SQLException {
        PreparedStatement ps = sql.con.prepareStatement("INSERT INTO invlog (date,UUID,inv) VALUES (?,?,?)");
        ps.setTimestamp(1, new Timestamp(new Date().getTime()));
        ps.setString(2, player.getUniqueId().toString());
        ps.setBytes(3, ItemStackSerializer.serialize(player.getInventory().getContents()));
        ps.executeUpdate();
    }

    public InventoryEntry getLastInv(UUID uuid, int lastIndex) throws SQLException {
        PreparedStatement ps2 = sql.con.prepareStatement("SELECT date,inv FROM invlog WHERE UUID = ? ORDER BY date DESC LIMIT ?, 1");
        int i=1; ps2.setString(i, uuid.toString());
        i++; ps2.setInt(i, lastIndex);
        ResultSet rs = ps2.executeQuery();

        while (rs.next()) {
            Date date = rs.getTimestamp("date");
            ItemStack[] inv = ItemStackSerializer.deserialize(rs.getBytes("inv"));

            return new InventoryEntry(inv, date, uuid, lastIndex);
        }

        return null;
    }
    public InventoryEntry getLastInv(UUID uuid) throws SQLException {
        return getLastInv(uuid, 0);
    }
}
