package sinanakkoyun.minecraftinventorylog;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import sinanakkoyun.minecraftinventorylog.constants.ItemStackConstants;
import sinanakkoyun.minecraftinventorylog.constants.StyleConstants;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class InventoryManager {
    public static int cancelSlot = 45;
    public static int previousSlot = 48;
    public static int playerHeadSlot = 49;
    public static int nextSlot = 50;
    public static int applySlot = 53;

    public static Inventory createInventoryRestoreView(MinecraftInventoryLog plugin, long timestamp, UUID id) {
        List<Long> times = InventoryConfigManager.getTimestampsOfPlayer(plugin, id);
        if(times == null) {
            return null;
        }

        Inventory inv = Bukkit.createInventory(null, 54, StyleConstants.dateColor + "[" + times.indexOf(timestamp) + "] " + StyleConstants.dateFormat.format(new Date(timestamp)));

        if(plugin.getConfig().get(id.toString()) + "." + timestamp == null) {
            return null;
        }

        //Get inventory from config file
        Object i = plugin.getConfig().getList(id.toString() + "." + timestamp + ".items");
        ItemStack[] contents = null;
        if (i instanceof ItemStack) {
            contents = (ItemStack[]) i;
        } else if (i instanceof List) {
            contents = (ItemStack[]) ((List) i).toArray(new ItemStack[((List) i).size()]);
        }
        if (contents != null) {
            inv.setContents(contents);
        }

        inv.setItem(cancelSlot, ItemStackConstants.cancel);
        inv.setItem(previousSlot, ItemStackConstants.previous);
        inv.setItem(playerHeadSlot, ItemStackConstants.getPlayerHead(Bukkit.getPlayer(id), timestamp));
        inv.setItem(nextSlot, ItemStackConstants.next);
        inv.setItem(applySlot, ItemStackConstants.apply);

        return inv;
    }

    public static ItemStack[] getContents(MinecraftInventoryLog plugin, long timestamp, UUID id) {
        Object i = plugin.getConfig().getList(id.toString() + "." + timestamp + ".items");
        ItemStack[] contents = null;
        if (i instanceof ItemStack) {
            contents = (ItemStack[]) i;
        } else if (i instanceof List) {
            contents = (ItemStack[]) ((List) i).toArray(new ItemStack[((List) i).size()]);
        }

        return contents;
    }

    public static Inventory createConfirmInventory() {
        Inventory inv = Bukkit.createInventory(null, 9, ChatColor.BLACK + "Confirm action");

        inv.setItem(4, ItemStackConstants.confirm);
        return inv;
    }

    public static Inventory updateConfirmRestoreView(Inventory restoreInv) {
        restoreInv.setItem(cancelSlot, ItemStackConstants.cancel);
        restoreInv.setItem(previousSlot, ItemStackConstants.confirm);
        restoreInv.setItem(nextSlot, ItemStackConstants.confirm);
        restoreInv.setItem(applySlot, ItemStackConstants.cancel);

        //restoreInv.setItem(playerHeadSlot, ItemStackConstants.confirm);
        return restoreInv;
    }
}
