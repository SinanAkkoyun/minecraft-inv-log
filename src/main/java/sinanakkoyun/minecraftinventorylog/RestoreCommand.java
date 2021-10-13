package sinanakkoyun.minecraftinventorylog;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sinanakkoyun.minecraftinventorylog.constants.StyleConstants;

import java.lang.reflect.Array;
import java.util.*;

public class RestoreCommand implements CommandExecutor {
    private MinecraftInventoryLog plugin;

    public RestoreCommand(MinecraftInventoryLog plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length > 0) {
            Player p = Bukkit.getPlayer(args[0]);

            if(p == null) {
                sender.sendMessage("Player " + args[0] + " not online.");
                return false;
            }

            this.plugin.reloadConfig();
            UUID id = p.getUniqueId();

            if (sender instanceof Player) {
                Player player = (Player) sender;
                sender.sendMessage(StyleConstants.statementColor + "Opening last inv...");

                List<Long> times = InventoryConfigManager.getTimestampsOfPlayer(plugin, id);

                if (times != null && times.size() > 0) {
                    sender.sendMessage(StyleConstants.statementColor + "" + times.size() + " inventories found!");
                    player.openInventory(InventoryManager.createInventoryRestoreView(plugin, times.get(times.size() - 1), id));
                } else {
                    sender.sendMessage("No saved inventories found.");
                    return false;
                }

                return true;
            }
        } else {
            sender.sendMessage("Must provide a player name.");
        }
        return false;
    }
}