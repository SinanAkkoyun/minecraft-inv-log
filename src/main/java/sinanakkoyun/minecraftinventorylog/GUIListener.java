package sinanakkoyun.minecraftinventorylog;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import sinanakkoyun.minecraftinventorylog.constants.ItemStackConstants;
import sinanakkoyun.minecraftinventorylog.constants.StyleConstants;
import sinanakkoyun.minecraftinventorylog.math.MathUtil;

import javax.swing.text.Style;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class GUIListener implements Listener {
    public MinecraftInventoryLog plugin;

    public GUIListener(MinecraftInventoryLog plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        ItemStack itemStack = event.getCurrentItem();
        if(itemStack == null)
            return;

        if(itemStack.equals(ItemStackConstants.cancel)) {
            event.getWhoClicked().sendMessage(StyleConstants.errorColor + "Restore got cancelled.");
            event.getWhoClicked().closeInventory();
        }

        if(itemStack.equals(ItemStackConstants.next)) {
            try {
                HumanEntity p = event.getWhoClicked();
                Inventory clickedInv = event.getClickedInventory();
                ItemStack playerHead = clickedInv.getItem(InventoryManager.playerHeadSlot);

                if(clickedInv != null && playerHead != null) {
                    long time = ItemStackConstants.getTimeFromPlayerHead(playerHead);
                    UUID id = ItemStackConstants.getUUIDFromPlayerHead(playerHead);
                    List<Long> times = InventoryConfigManager.getTimestampsOfPlayer(plugin, id);

                    if (times != null && times.size() > 0 && times.contains(time)) {
                        int nextTimeIndex = MathUtil.clamp(times.indexOf(time) + 1, 0, times.size() - 1);
                        p.sendMessage("index of current " + time + ": " + times.indexOf(time));
                        p.sendMessage("index of next " + times.get(nextTimeIndex) + ": " + nextTimeIndex);
                        p.openInventory(InventoryManager.createInventoryRestoreView(plugin, times.get(nextTimeIndex), id));
                    } else {
                        p.sendMessage(StyleConstants.errorColor + "Inventory is not up to date or config.yml has been altered while operating.");
                    }
                } else {
                    p.sendMessage(StyleConstants.errorColor + "Error. Either the inventory is null or the player head is not at the right spot in the inventory.");
                }
            } catch (Exception e) {
                event.getWhoClicked().sendMessage(StyleConstants.errorColor + e.getMessage());
            }
        }

        if(itemStack.equals(ItemStackConstants.previous)) {
            try {
                HumanEntity p = event.getWhoClicked();
                Inventory clickedInv = event.getClickedInventory();
                ItemStack playerHead = clickedInv.getItem(InventoryManager.playerHeadSlot);

                if(clickedInv != null && playerHead != null) {
                    long time = ItemStackConstants.getTimeFromPlayerHead(playerHead);
                    UUID id = ItemStackConstants.getUUIDFromPlayerHead(playerHead);
                    List<Long> times = InventoryConfigManager.getTimestampsOfPlayer(plugin, id);

                    if (times != null && times.size() > 0 && times.contains(time)) {
                        int previousTimeIndex = MathUtil.clamp(times.indexOf(time) - 1, 0, times.size() - 1);
                        p.sendMessage("index of current " + time + ": " + times.indexOf(time));
                        p.sendMessage("index of previous " + times.get(previousTimeIndex) + ": " + previousTimeIndex);
                        p.openInventory(InventoryManager.createInventoryRestoreView(plugin, times.get(previousTimeIndex), id));
                    } else {
                        p.sendMessage(StyleConstants.errorColor + "Inventory is not up to date or config.yml has been altered while operating.");
                    }
                } else {
                    p.sendMessage(StyleConstants.errorColor + "Error. Either the inventory is null or the player head is not at the right spot in the inventory.");
                }
            } catch (Exception e) {
                event.getWhoClicked().sendMessage(StyleConstants.errorColor + e.getMessage());
            }
        }

        if(itemStack.equals(ItemStackConstants.apply)) {
            try {
                Inventory clickedInv = event.getClickedInventory();
                event.getWhoClicked().sendMessage(StyleConstants.questionColor + "Are you sure you want to clear the player's current inventory and want to restore the chosen one?");
                event.getWhoClicked().openInventory(InventoryManager.updateConfirmRestoreView(clickedInv));
            } catch (Exception e) {
                event.getWhoClicked().sendMessage(StyleConstants.errorColor + e.getMessage());
            }
        }

        if(itemStack.equals(ItemStackConstants.confirm)) {
            try {
                HumanEntity p = event.getWhoClicked();
                Inventory clickedInv = event.getClickedInventory();
                ItemStack playerHead = clickedInv.getItem(InventoryManager.playerHeadSlot);

                if (playerHead == null) {
                    p.sendMessage(StyleConstants.errorColor + "The playerhead could not be found.");
                    p.closeInventory();
                    return;
                }

                Player player = Bukkit.getPlayer(ItemStackConstants.getUUIDFromPlayerHead(playerHead));

                if (player == null) {
                    p.sendMessage(StyleConstants.errorColor + "The player " + ChatColor.stripColor(playerHead.getItemMeta().getDisplayName()) + " is currently not online.");
                    p.closeInventory();
                    return;
                }

                player.getInventory().setContents(Arrays.copyOfRange(clickedInv.getContents(), 0, 41));
                player.sendMessage(StyleConstants.successColor + "Your inventory has been restored by " + p.getName() + ".");

                p.closeInventory();
                p.sendMessage(StyleConstants.successColor + "Successfully applied inventory to player!");
            } catch (Exception e) {
                event.getWhoClicked().sendMessage(StyleConstants.errorColor + e.getMessage());
            }
        }

        if(event.isRightClick() || event.isLeftClick()) {
            if(ItemStackConstants.isGUIItem(event.getCursor()) || ItemStackConstants.isGUIItem(event.getCurrentItem())) {
                event.setCancelled(true);
                ((Player)event.getWhoClicked()).updateInventory();
            }
        }
    }
}
