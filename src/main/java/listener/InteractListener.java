package listener;

import main.Main;
import utils.Config;
import utils.Locations;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class InteractListener implements Listener {
  @EventHandler
  public static void onInteract(PlayerInteractEvent e) {
    Player p = e.getPlayer();
    try {
      if (e.getClickedBlock().getType() == Material.STONE_PLATE) {
        return;
      }
    }
    catch(Exception e2){

    }

    if (p.getItemInHand().hasItemMeta() && 
      p.getItemInHand().getItemMeta().getDisplayName() != null)
      if (p.getItemInHand().getItemMeta().getDisplayName().equals("§cKits")) {

        if (Config.config.getBoolean("kits"))
          if (Config.kits.getString("kits.view") != null) {
            Inventory.setKitAuswahlInventory(p);
          } else if (p.hasPermission(Config.permissions.getString("kit.cmd"))) {
            p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nokitselection.admin").replaceAll("&", "§"));
          } else {
            p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nokitselection.user").replaceAll("&", "§"));
          }  
      } else if (p.getItemInHand().getItemMeta().getDisplayName().equals("§cInventorySorting")) {
        if (Config.config.getBoolean("kits"))
          if (Config.player.getString("players." + p.getUniqueId().toString()) != null) {
            if (Config.player.getString("players." + p.getUniqueId().toString() + ".kitselected") != null) {
              String Kitname = Config.player.getString("players." + p.getUniqueId().toString() + ".kitselected");
              if (Config.player.getString("players." + p.getUniqueId().toString() + "." + Kitname + ".items") != null) {
                ItemStack[] inv = Inventory.getSortInventoryHotbar(p, Kitname);
                org.bukkit.inventory.Inventory inventar = Bukkit.createInventory(null, 18, "§cInventorySorting");
                inventar.setContents(inv);
                p.openInventory(inventar);
              } else {
                ItemStack[] inv = Inventory.getInventoryHotbar(Kitname);
                org.bukkit.inventory.Inventory inventar = Bukkit.createInventory(null, 18, "§cInventorySorting");
                inventar.setContents(inv);
                p.openInventory(inventar);
              } 
            } else {
              p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nokitselected").replaceAll("&", "§"));
            } 
          } else {
            p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nokitselected").replaceAll("&", "§"));
          }  
      }
      else if (p.getItemInHand().getItemMeta().getDisplayName().equals("§cBack to the lobby")) {
        p.kickPlayer("");
      }
      else if (p.getLocation().getY() >= Config.locations.getDouble(String.valueOf(Locations.getCurrentMap()) + ".arenaheight")) {
        e.setCancelled(true);
      }  
  }
}
