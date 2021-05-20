package listener;

import main.Main;
import utils.Config;
import utils.Inventory;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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
//                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 2147483647, 2));
//                p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 2147483647, 1));
                ItemStack[] inv = Inventory.getSortInventoryHotbar(p, Kitname);
                org.bukkit.inventory.Inventory inventar = Bukkit.createInventory(null, 9, "§cInventorySorting");
                inventar.setContents(inv);
                p.openInventory(inventar);
              } else {
//                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 2147483647, 2));
//                p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 2147483647, 1));
                ItemStack[] inv = Inventory.getInventoryHotbar(Kitname);
                org.bukkit.inventory.Inventory inventar = Bukkit.createInventory(null, 9, "§cInventorySorting");
                inventar.setContents(inv);
                p.openInventory(inventar);
              } 
            } else {
              p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nokitselected").replaceAll("&", "§"));
            } 
          } else {
            p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nokitselected").replaceAll("&", "§"));
          }  
      } else if (p.getItemInHand().getItemMeta().getDisplayName().equals("§cStats")) {
        if (Config.config.getBoolean("stats")) {
//          p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 2147483647, 2));
//          p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 2147483647, 1));
          Inventory.setStatsInventory(p);
        } else {
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.deactivated").replaceAll("&", "§"));
        } 
      } else if (p.getItemInHand().getItemMeta().getDisplayName().equals("§cBack to the Lobby")) {
        p.performCommand(Config.config.getString("lobbyitemcommand"));
      } else if (p.getItemInHand().getItemMeta().spigot().isUnbreakable() && p.getItemInHand().getItemMeta().getDisplayName().equals("§cJump") && p.getItemInHand().getType() == Material.FEATHER) {
        Vector vector = e.getPlayer().getVelocity();
        vector.setY(2.1D);
        p.setVelocity(vector);
        p.playSound(p.getLocation(), Sound.ENDERDRAGON_WINGS, 3.0F, 1.0F);
        p.playEffect(p.getLocation(), Effect.FLAME, 3);
        int amount = p.getItemInHand().getAmount();
        if (amount == 1) {
          p.getInventory().removeItem(new ItemStack[] { p.getItemInHand() });
        } else {
          p.getItemInHand().setAmount(amount - 1);
        } 
      } else if (p.getLocation().getY() >= Config.locations.getDouble(String.valueOf(Locations.getCurrentMap()) + ".arenaheight")) {
        e.setCancelled(true);
      }  
  }
}
