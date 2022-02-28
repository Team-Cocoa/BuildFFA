package kr.teamcocoa.buildffa.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;

public class InventoryDragListener implements Listener {

    @EventHandler
    public void onDrag(InventoryDragEvent e){
        if(e.getWhoClicked() instanceof Player){
            if(e.getInventory().getName().equals(ChatColor.translateAlternateColorCodes('&', "&cInventorySorting"))) {
                e.setCancelled(true);
            }
        }
    }
}
