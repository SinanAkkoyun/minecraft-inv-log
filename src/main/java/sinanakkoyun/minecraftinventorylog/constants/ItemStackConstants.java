package sinanakkoyun.minecraftinventorylog.constants;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemStackConstants {
    public static List<ItemStack> guiItems = new ArrayList();
    public static ItemStack cancel = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
    public static ItemStack apply = new ItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
    public static ItemStack previous = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
    public static ItemStack next = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
    public static ItemStack confirm = new ItemStack(Material.LIME_CONCRETE, 1);

    public static void init() {
        ItemMeta cancel_m = cancel.getItemMeta();
        cancel_m.setDisplayName(ChatColor.RED + "Cancel");
        cancel.setItemMeta(cancel_m);

        ItemMeta apply_m = apply.getItemMeta();
        apply_m.setDisplayName(ChatColor.GREEN + "Apply");
        List<String> apply_lore = new ArrayList<String>(); //create a List<String> for the lore
        apply_lore.add(ChatColor.GOLD + "Do you want to apply this inventory to the player?");
        apply_lore.add(ChatColor.GOLD + "You can edit this inventory before applying.");
        apply_lore.add(ChatColor.GRAY + "<click to apply>");
        apply_m.setLore(apply_lore);
        apply.setItemMeta(apply_m);

        ItemMeta previous_m = previous.getItemMeta();
        previous_m.setDisplayName(ChatColor.GRAY + "Previous");
        previous.setItemMeta(previous_m);

        ItemMeta next_m = next.getItemMeta();
        next_m.setDisplayName(ChatColor.WHITE + "Next");
        next.setItemMeta(next_m);

        ItemMeta confirm_m = confirm.getItemMeta();
        confirm_m.setDisplayName(ChatColor.GREEN + "Confirm?");
        List<String> confirm_lore = new ArrayList<String>(); //create a List<String> for the lore
        confirm_lore.add(ChatColor.GOLD + "Clear players current inventory");
        confirm_lore.add(ChatColor.GOLD + "and restore the old one?");
        confirm_lore.add(ChatColor.GRAY + "<click to confirm>");
        confirm_m.setLore(confirm_lore);
        confirm_m.addEnchant(Enchantment.BINDING_CURSE, 1, false);
        confirm.setItemMeta(confirm_m);

        guiItems.add(cancel);
        guiItems.add(apply);
        guiItems.add(previous);
        guiItems.add(next);
        guiItems.add(confirm);
    }

    public static ItemStack getPlayerHead(Player player, long time) {
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
        meta.setDisplayName(player.getDisplayName());
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.DARK_GRAY + player.getUniqueId().toString());
        lore.add(ChatColor.DARK_GRAY + "" + time);
        meta.setLore(lore);
        meta.setOwningPlayer(player);
        playerHead.setItemMeta(meta);

        return playerHead;
    }

    public static UUID getUUIDFromPlayerHead(ItemStack playerHead) {
        SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
        return UUID.fromString(ChatColor.stripColor(meta.getLore().get(0)));
    }

    public static long getTimeFromPlayerHead(ItemStack playerHead) {
        SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
        return Long.parseLong(ChatColor.stripColor(meta.getLore().get(1)));
    }

    public static boolean isGUIItem(ItemStack itemStack) {
        if(itemStack == null)
            return false;

        return guiItems.contains(itemStack) || (itemStack.getItemMeta() instanceof SkullMeta && itemStack.getItemMeta().getLore().size() > 0);
    }
}
