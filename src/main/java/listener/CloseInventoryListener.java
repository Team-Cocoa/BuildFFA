package listener;

import utils.Config;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.potion.PotionEffectType;

public class CloseInventoryListener implements Listener {
  @EventHandler
  public static void onCloseInventory(InventoryCloseEvent e) {
    Player p = (Player)e.getPlayer();
    if (p.hasPotionEffect(PotionEffectType.BLINDNESS))
      p.removePotionEffect(PotionEffectType.BLINDNESS); 
    if (p.hasPotionEffect(PotionEffectType.CONFUSION))
      p.removePotionEffect(PotionEffectType.CONFUSION); 
    if (e.getInventory().getTitle() == "§cInventorySorting") {
      org.bukkit.inventory.Inventory inv = e.getInventory();
      if (Config.player.getString("players." + p.getUniqueId().toString() + ".kitselected") != null) {
        String Kitname = Config.player.getString("players." + p.getUniqueId().toString() + ".kitselected");
        Inventory.saveSortInventoryHotbar(p, Kitname, inv);
        p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 100.0F, 0.0F);
        p.getInventory().clear();
        Inventory.setJoinInventory(p);
      } 
    } 
  }
}
