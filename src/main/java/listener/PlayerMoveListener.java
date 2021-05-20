package listener;

import commands.Build;
import main.Main;
import utils.Config;
import utils.Inventory;
import utils.Locations;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class PlayerMoveListener implements Listener {
  private static ArrayList<Player> died = new ArrayList<>();
  
  public static ArrayList<Player> gotInventory = new ArrayList<>();
  
  @EventHandler
  public static void onPlayerMove(PlayerMoveEvent e) {
    final Player p = e.getPlayer();
    Location loc = p.getLocation();
    if (Locations.getCurrentMap() != null)
      if (Config.locations.getString(String.valueOf(Locations.getCurrentMap()) + ".deathheight") != null)
        if (Config.locations.getString(String.valueOf(Locations.getCurrentMap()) + ".arenaheight") != null) {
          if (loc.getY() <= Config.locations.getDouble(String.valueOf(Locations.getCurrentMap()) + ".deathheight"))
            if (!died.contains(p) && !Build.buildmode.contains(p.getName())) {
              p.setHealth(0.0D);
              died.add(p);
              Bukkit.getScheduler().runTaskLater((Plugin)Main.inst(), new Runnable() {
                    public void run() {
                      PlayerMoveListener.died.remove(p);
                    }
                  },  20L);
            }  
          if (loc.getY() <= Config.locations.getDouble(String.valueOf(Locations.getCurrentMap()) + ".arenaheight"))
            if (!gotInventory.contains(p) && !Build.buildmode.contains(p.getName())) {
              p.closeInventory();
              p.getInventory().clear();
              gotInventory.add(p);
              p.setHealth(20.0D);
              p.setLevel(0);
              String defaultKit = Config.kits.getString("kits.default");
              if (defaultKit != null) {
                ItemStack[] InvdefaultKitContents = Inventory.getInventoryHotbar(defaultKit);
                if (Config.player.getString("players." + p.getUniqueId().toString()) != null) {
                  if (Config.player.getString("players." + p.getUniqueId().toString() + ".kitselected") != null) {
                    String SelectedKit = Config.player.getString("players." + p.getUniqueId().toString() + ".kitselected");
                    if (Config.player.getString("players." + p.getUniqueId().toString() + "." + SelectedKit) != null) {
                      ItemStack[] InvSortKitContents = Inventory.getSortInventoryHotbar(p, SelectedKit);
                      p.getInventory().setContents(InvSortKitContents);
                      ItemStack[] inv2 = Inventory.getInventoryArmorContents(SelectedKit);
                      p.getInventory().setArmorContents(inv2);
                      p.playSound(p.getLocation(), Sound.ORB_PICKUP, 100.0F, 0.0F);
                    } else {
                      ItemStack[] InvSelectedKitContents = Inventory.getInventoryHotbar(SelectedKit);
                      p.getInventory().setContents(InvSelectedKitContents);
                      ItemStack[] inv2 = Inventory.getInventoryArmorContents(SelectedKit);
                      p.getInventory().setArmorContents(inv2);
                      p.playSound(p.getLocation(), Sound.ORB_PICKUP, 100.0F, 0.0F);
                    } 
                  } else {
                    p.getInventory().setContents(InvdefaultKitContents);
                    ItemStack[] inv2 = Inventory.getInventoryArmorContents(defaultKit);
                    p.getInventory().setArmorContents(inv2);
                  } 
                } else {
                  p.getInventory().setContents(InvdefaultKitContents);
                  ItemStack[] inv2 = Inventory.getInventoryArmorContents(defaultKit);
                  p.getInventory().setArmorContents(inv2);
                } 
              } else {
                p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nodefaultkit.01").replaceAll("&", "§"));
                p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nodefaultkit.02").replaceAll("&", "§"));
              } 
            }  
        }   
  }
}
