package sinan.minecraftinventorylog.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sinan.minecraftinventorylog.MinecraftInventoryLog;
import sinan.minecraftinventorylog.constants.StyleConstants;
import sinan.minecraftinventorylog.types.InventoryEntry;
import sinan.minecraftinventorylog.util.InventoryManager;

import java.sql.SQLException;
import java.util.*;

public class RestoreCommand implements CommandExecutor {
    private MinecraftInventoryLog plugin;

    public RestoreCommand(MinecraftInventoryLog plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length > 0) {
            UUID id = null;

            if(Bukkit.getPlayer(args[0]) == null) {
                if(Bukkit.getOfflinePlayerIfCached(args[0]) != null)
                    id = Bukkit.getOfflinePlayerIfCached(args[0]).getUniqueId();
                else
                    sender.sendMessage(StyleConstants.statementColor + "Player not in cache.");
            } else {
                id = Bukkit.getPlayer(args[0]).getUniqueId();
            }

            if(id == null) {
                sender.sendMessage("Player " + args[0] + " not found.");
                return false;
            }

            if (sender instanceof Player) {
                Player player = (Player) sender;
                sender.sendMessage(StyleConstants.statementColor + "Opening last inv...");

                //sender.sendMessage(StyleConstants.statementColor + "" + times.size() + " inventories found!");
                try {
                    InventoryEntry entry = plugin.getLastInv(id);
                    if(entry == null)
                        sender.sendMessage("Null");

                    player.openInventory(InventoryManager.createInventoryRestoreView(entry));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                return true;
            }
        } else {
            sender.sendMessage("Must provide a player name.");
        }
        return false;
    }
}