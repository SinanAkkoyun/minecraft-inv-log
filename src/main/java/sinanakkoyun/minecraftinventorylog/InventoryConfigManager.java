package sinanakkoyun.minecraftinventorylog;

import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import sinanakkoyun.minecraftinventorylog.constants.ConfigConstants;
import sinanakkoyun.minecraftinventorylog.itemstack.ItemStackUtil;

import java.util.*;

public class InventoryConfigManager {
    public static void saveInventory(MinecraftInventoryLog plugin, Player player) {
        UUID id = player.getUniqueId();

        if(!player.getInventory().isEmpty()) {
            plugin.reloadConfig();

            List<Long> times = InventoryConfigManager.getTimestampsOfPlayer(plugin, player.getUniqueId());
            if(times != null && times.size() > ConfigConstants.maxInventoriesSaved - 1) {
                plugin.getConfig().set(id.toString() + "." + times.get(0), null);
            }
            /*if(times.size() > 0) {
                if (ItemStackUtil.checkIfEqual(player.getInventory().getContents(), InventoryManager.getContents(plugin, times.get(times.size() - 1), id))) {
                    plugin.saveConfig();
                    return;
                }
            }*/

            plugin.getConfig().set(id.toString() + "." + new Date().getTime() + ".items", player.getInventory().getContents());
            plugin.saveConfig();
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
