package kr.teamcocoa.buildffa.listener;
import kr.teamcocoa.buildffa.kit.KitData;
import kr.teamcocoa.buildffa.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
      ItemStack clickedItem = e.getCurrentItem();
      if (clickedItem.equals(KitData.getSymbol(0))) {
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
        return;
      }
      if (clickedItem.equals(KitData.getSymbol(1))) {
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
        return;
      }
      if (clickedItem.equals(KitData.getSymbol(2))) {
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
        return;
      }
    }

    if(e.getView().getTitle() == "§cInventorySorting"){

    }
  }
}
