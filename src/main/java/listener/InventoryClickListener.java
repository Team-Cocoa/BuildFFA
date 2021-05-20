package listener;

import commands.Build;
import utils.Config;
import utils.Inventory;
import java.io.IOException;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener {
  @EventHandler
  public static void onInventoryClick(InventoryClickEvent e) {
    Player p = (Player)e.getWhoClicked();
    if (e.getCurrentItem() == null)
      return; 
    if (e.getCurrentItem().getItemMeta() == null)
      return; 
    if (e.getView().getTitle() == "§cKit Selection") {
      if (Config.config.getBoolean("kits")) {
        String Kit1name = Config.kits.getString("kits.view.selection.1");
        String Kit2name = Config.kits.getString("kits.view.selection.2");
        String Kit3name = Config.kits.getString("kits.view.selection.3");
        String Kit4name = Config.kits.getString("kits.view.selection.4");
        String Kit1prefix = Config.kits.getString(String.valueOf(Kit1name) + ".prefix");
        String Kit2prefix = Config.kits.getString(String.valueOf(Kit2name) + ".prefix");
        String Kit3prefix = Config.kits.getString(String.valueOf(Kit3name) + ".prefix");
        String Kit4prefix = Config.kits.getString(String.valueOf(Kit4name) + ".prefix");
        ItemStack Kit1symbol = Config.kits.getItemStack(String.valueOf(Kit1name) + ".symbol");
        ItemStack Kit2symbol = Config.kits.getItemStack(String.valueOf(Kit2name) + ".symbol");
        ItemStack Kit3symbol = Config.kits.getItemStack(String.valueOf(Kit3name) + ".symbol");
        ItemStack Kit4symbol = Config.kits.getItemStack(String.valueOf(Kit4name) + ".symbol");
        Material Kit1symbolMaterial = null;
        Material Kit2symbolMaterial = null;
        Material Kit3symbolMaterial = null;
        Material Kit4symbolMaterial = null;
        if (Kit1symbol != null)
          Kit1symbolMaterial = Kit1symbol.getType(); 
        if (Kit2symbol != null)
          Kit2symbolMaterial = Kit2symbol.getType(); 
        if (Kit3symbol != null)
          Kit3symbolMaterial = Kit3symbol.getType(); 
        if (Kit4symbol != null)
          Kit4symbolMaterial = Kit4symbol.getType(); 
        ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem.getItemMeta().getDisplayName().equals(Kit1prefix) && Kit1symbolMaterial != null && clickedItem.getType() != Material.BARRIER) {
          Config.player.set("players." + p.getUniqueId().toString() + ".kitselected", Kit1name);
          p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 100.0F, 0.0F);
          try {
            Config.player.save(Config.playerFile);
          } catch (IOException e2) {
            e2.printStackTrace();
          } 
          p.closeInventory();
          p.getInventory().setItem(0, Inventory.createItem(Kit1symbolMaterial, 1, Kit1prefix));
        } else {
          e.setCancelled(true);
        } 
        if (clickedItem.getItemMeta().getDisplayName().equals(Kit2prefix) && Kit1symbolMaterial != null && clickedItem.getType() != Material.BARRIER) {
          Config.player.set("players." + p.getUniqueId().toString() + ".kitselected", Kit2name);
          p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 100.0F, 0.0F);
          try {
            Config.player.save(Config.playerFile);
          } catch (IOException e2) {
            e2.printStackTrace();
          } 
          p.closeInventory();
          p.getInventory().setItem(0, Inventory.createItem(Kit2symbolMaterial, 1, Kit2prefix));
        } else {
          e.setCancelled(true);
        } 
        if (clickedItem.getItemMeta().getDisplayName().equals(Kit3prefix) && Kit1symbolMaterial != null && clickedItem.getType() != Material.BARRIER) {
          Config.player.set("players." + p.getUniqueId().toString() + ".kitselected", Kit3name);
          p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 100.0F, 0.0F);
          try {
            Config.player.save(Config.playerFile);
          } catch (IOException e2) {
            e2.printStackTrace();
          } 
          p.closeInventory();
          p.getInventory().setItem(0, Inventory.createItem(Kit3symbolMaterial, 1, Kit3prefix));
        } else {
          e.setCancelled(true);
        } 
        if (clickedItem.getItemMeta().getDisplayName().equals(Kit4prefix) && Kit1symbolMaterial != null && clickedItem.getType() != Material.BARRIER) {
          Config.player.set("players." + p.getUniqueId().toString() + ".kitselected", Kit4name);
          p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 100.0F, 0.0F);
          try {
            Config.player.save(Config.playerFile);
          } catch (IOException e2) {
            e2.printStackTrace();
          } 
          p.closeInventory();
          p.getInventory().setItem(0, Inventory.createItem(Kit4symbolMaterial, 1, Kit4prefix));
        } else {
          e.setCancelled(true);
        } 
        if (clickedItem.getItemMeta().getDisplayName().equals("§cReset§7-§6Sorting§7-§c" + Config.player.getString("players." + p.getUniqueId().toString() + ".kitselected")) && clickedItem.getType() == Material.BLAZE_ROD)
          if (Config.player.getString("players." + p.getUniqueId().toString() + "." + Config.player.getString("players." + p.getUniqueId().toString() + ".kitselected")) != null) {
            Config.player.set("players." + p.getUniqueId().toString() + "." + Config.player.getString("players." + p.getUniqueId().toString() + ".kitselected"), null);
            try {
              Config.player.save(Config.playerFile);
            } catch (IOException e2) {
              e2.printStackTrace();
            } 
            p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 100.0F, 0.0F);
          } else {
            p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 100.0F, 0.0F);
          }  
      } 
    } else if (!Build.buildmode.contains(p.getName()) && !e.getClickedInventory().getTitle().equals("§cInventorySorting") && !PlayerMoveListener.gotInventory.contains(p)) {
      e.setCancelled(true);
    } 
  }
}
