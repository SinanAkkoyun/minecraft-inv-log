package sinanakkoyun.minecraftinventorylog.itemstack;

import org.bukkit.inventory.ItemStack;

public class ItemStackUtil {
    public static ItemStack nullifyAir(ItemStack itemStack) {
        return itemStack.getType().isAir() ? null : itemStack;
    }
    public static boolean checkIfEqual(ItemStack[] contents1, ItemStack[] contents2) {
        int mismatch = 0;
        for(int i = 0; i < contents1.length; i++) {

            if(!nullifyAir(contents1[i]).equals(nullifyAir(contents2[i]))) {
                mismatch++;
            }
        }

        return !(mismatch > 0);
    }
}
