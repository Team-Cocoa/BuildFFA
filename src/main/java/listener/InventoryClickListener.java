package listener;

import commands.Build;
import data.KitData;
import main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import utils.MYSQL;

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
        String Kit1name = KitData.getKitByInt(0);
        String Kit2name = KitData.getKitByInt(1);
        String Kit3name = KitData.getKitByInt(2);
        ItemStack Kit1symbol = KitData.getSymbol(0);
        ItemStack Kit2symbol = KitData.getSymbol(1);
        ItemStack Kit3symbol = KitData.getSymbol(2);
        Material Kit1symbolMaterial = null;
        Material Kit2symbolMaterial = null;
        Material Kit3symbolMaterial = null;
        if (Kit1symbol != null)
          Kit1symbolMaterial = Kit1symbol.getType(); 
        if (Kit2symbol != null)
          Kit2symbolMaterial = Kit2symbol.getType(); 
        if (Kit3symbol != null)
          Kit3symbolMaterial = Kit3symbol.getType();
        ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem.getItemMeta().getDisplayName().equals(Kit1name) && Kit1symbolMaterial != null && clickedItem.getType() != Material.BARRIER) {
//          Config.player.set("players." + p.getUniqueId().toString() + ".kitselected", Kit1name);
          p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 100.0F, 0.0F);
          try {
            Bukkit.getScheduler().runTaskAsynchronously(Main.inst(), ()-> {
              KitData.setKit(p, 0);
              p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a[&dTeamCocoa&a] &aYour settings has been saved."));
            });
          } catch (Exception e2) {
            e2.printStackTrace();
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a[&dTeamCocoa&a] &cAn error has occurred."));
          } 
          p.closeInventory();
          p.getInventory().setItem(0, Inventory.createItem(Kit1symbolMaterial, 1, Kit1name));
        } else {
          e.setCancelled(true);
        } 
        if (clickedItem.getItemMeta().getDisplayName().equals(Kit2name) && Kit1symbolMaterial != null && clickedItem.getType() != Material.BARRIER) {
//          Config.player.set("players." + p.getUniqueId().toString() + ".kitselected", Kit2name);
          p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 100.0F, 0.0F);
          try {
            Bukkit.getScheduler().runTaskAsynchronously(Main.inst(), ()-> {
              KitData.setKit(p, 1);
              p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a[&dTeamCocoa&a] &aYour settings has been saved."));
            });
          } catch (Exception e2) {
            e2.printStackTrace();
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a[&dTeamCocoa&a] &cAn error has occurred."));
          }
          p.closeInventory();
          p.getInventory().setItem(0, Inventory.createItem(Kit2symbolMaterial, 1, Kit2name));
        } else {
          e.setCancelled(true);
        } 
        if (clickedItem.getItemMeta().getDisplayName().equals(Kit3name) && Kit1symbolMaterial != null && clickedItem.getType() != Material.BARRIER) {
//          Config.player.set("players." + p.getUniqueId().toString() + ".kitselected", Kit3name);
          p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 100.0F, 0.0F);
          try {
            Bukkit.getScheduler().runTaskAsynchronously(Main.inst(), ()-> {
              KitData.setKit(p, 2);
              p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a[&dTeamCocoa&a] &aYour settings has been saved."));
            });
          } catch (Exception e2) {
            e2.printStackTrace();
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a[&dTeamCocoa&a] &cAn error has occurred."));
          }
          p.closeInventory();
          p.getInventory().setItem(0, Inventory.createItem(Kit3symbolMaterial, 1, Kit3name));
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
