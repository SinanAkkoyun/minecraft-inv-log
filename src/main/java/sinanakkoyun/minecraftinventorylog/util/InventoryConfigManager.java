package sinanakkoyun.minecraftinventorylog.util;

import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import sinanakkoyun.minecraftinventorylog.MinecraftInventoryLog;
import sinanakkoyun.minecraftinventorylog.constants.ConfigConstants;
import sinanakkoyun.minecraftinventorylog.itemstack.ItemStackUtil;

import java.util.*;

public class InventoryConfigManager {
    public static void saveInventory(MinecraftInventoryLog plugin, Player player) {
        UUID id = player.getUniqueId();

        if(!player.getInventory().isEmpty()) {
            try {
                plugin.logInv(player);
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static List<Long> getTimestampsOfPlayer(MinecraftInventoryLog plugin, UUID id) {
        if(plugin.getConfig() == null || plugin.getConfig().get(id.toString()) == null) {
            return null;
        }

        Set<Long> timesSet = new HashSet<>();
        for (String s : plugin.getConfig().getConfigurationSection(id.toString()).getKeys(false)) {
            try {
                timesSet.add(Long.valueOf(s));
            } catch (NumberFormatException e) { }
        }

        List<Long> times = new ArrayList<>(timesSet);
        Collections.sort(times);
        return times;
    }
}
