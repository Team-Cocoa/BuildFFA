package kr.teamcocoa.buildffa.listener;
import kr.teamcocoa.buildffa.kit.BffaPlayer;
import kr.teamcocoa.buildffa.kit.KitData;
import kr.teamcocoa.buildffa.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class InventoryClickListener implements Listener {
  @EventHandler
  public static void onInventoryClick(InventoryClickEvent e) {
    Player p = (Player)e.getWhoClicked();
    if (e.getCurrentItem() == null) {
      e.setCancelled(true);
      return;
    }
    if (e.getCurrentItem().getItemMeta() == null) {
      e.setCancelled(true);
      return;
    }
    if(Main.playerData.get(p).isBuild()){
        return;
    }
    if (e.getView().getTitle().equals("§cKit Selection")) {
        ItemStack clickedItem = e.getCurrentItem();
        int kit = 0;
        switch(clickedItem.getType()){
            case STICK:
                kit = 0;
                break;
            case FISHING_ROD:
                kit = 1;
                break;
            case BOW:
                kit = 2;
                break;
            default:
                kit = 3;
                break;
        }
        if(kit == 3){
            e.setCancelled(true);
            return;
        }
        p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 100.0F, 0.0F);
        try {
            KitData.setKit(p, kit);
            Main.playerData.get(p).setKit(kit);
            Main.playerData.get(p).setInventory(KitData.getPlayerKit(p, kit));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a[&dTeamCocoa&a] &aYour settings has been saved."));
        } catch (Exception e1) {
            e1.printStackTrace();
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a[&dTeamCocoa&a] &cAn error has occurred."));
        }
        p.closeInventory();
        return;
    }


    if(e.getView().getTitle() == "§cInventorySorting"){
        ItemStack saveItem = KitData.createDye("§aSave", Material.INK_SACK, (short)10);
        ItemStack resetItem = KitData.createDye("§cReset", Material.INK_SACK, (short)1);
        ItemStack clickedItem = e.getCurrentItem();
        ItemStack glassItem = KitData.createItemStack(Material.STAINED_GLASS_PANE, " ", 1, new ArrayList(), (byte)7);
        int kit = Main.playerData.get(p).getKit();
        if(clickedItem.equals(glassItem)){
            e.setCancelled(true);
            return;
        }
        if(clickedItem.equals(resetItem)){
            ItemStack[] inventory = KitData.getDefaultKit(KitData.getKitByInt(kit));
            try{
                Main.playerData.get(p).setInventory(inventory);
                KitData.setInventorySetting(p, inventory, KitData.getKitByInt(kit));
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a[&dTeamCocoa&a] &cYour settings has been reset."));
            }
            catch(Exception e1){
                e1.printStackTrace();
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a[&dTeamCocoa&a] &cAn error has occurred."));
            }
        }
        if(clickedItem.equals(saveItem)){
            ItemStack[] inventory = new ItemStack[9];
            for(int i = 9; i < 18; i++){
                ItemStack item = e.getInventory().getItem(i);
            }
        }
    }
  }
}
