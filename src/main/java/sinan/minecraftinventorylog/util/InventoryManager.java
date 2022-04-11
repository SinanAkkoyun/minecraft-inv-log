package sinan.minecraftinventorylog.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import sinan.minecraftinventorylog.constants.ItemStackConstants;
import sinan.minecraftinventorylog.constants.StyleConstants;
import sinan.minecraftinventorylog.types.InventoryEntry;

public class InventoryManager {
    public static int cancelSlot = 45;
    public static int previousSlot = 48;
    public static int playerHeadSlot = 49;
    public static int nextSlot = 50;
    public static int applySlot = 53;

    public static Inventory createInventoryRestoreView(InventoryEntry invEntry) {
        if(invEntry == null) {
            return null;
        }

        Inventory inv = Bukkit.createInventory(null, 54, StyleConstants.dateColor + StyleConstants.dateFormat.format(invEntry.date));
        inv.setContents(invEntry.contents);

        inv.setItem(cancelSlot, ItemStackConstants.cancel);
        inv.setItem(previousSlot, ItemStackConstants.previous);
        inv.setItem(playerHeadSlot, ItemStackConstants.getPlayerHeadWithIndex(Bukkit.getOfflinePlayer(invEntry.uuid), invEntry.index));
        inv.setItem(nextSlot, ItemStackConstants.next);
        inv.setItem(applySlot, ItemStackConstants.apply);

        return inv;
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
