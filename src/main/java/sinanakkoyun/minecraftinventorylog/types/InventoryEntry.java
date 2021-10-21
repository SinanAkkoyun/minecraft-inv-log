package sinanakkoyun.minecraftinventorylog.types;

import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.UUID;

public class InventoryEntry {
    public ItemStack[] contents;
    public Date date;
    public UUID uuid;
    public int index;

    public InventoryEntry(ItemStack[] contents, Date date, UUID uuid, int index) {
        this.contents = contents;
        this.date = date;
        this.uuid = uuid;
        this.index = index;
    }
}
