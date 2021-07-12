package kr.teamcocoa.buildffa.listener;
import kr.teamcocoa.buildffa.kit.BffaPlayer;
import kr.teamcocoa.buildffa.kit.KitData;
import kr.teamcocoa.buildffa.main.Main;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class InventoryClickListener implements Listener {
  @EventHandler
  public void onInventoryClick(InventoryClickEvent e) {
    Player p = (Player)e.getWhoClicked();
    if (e.getCurrentItem() == null && !e.getView().getTitle().equals("§cInventorySorting")) {
      e.setCancelled(true);
      return;
    }
    if (e.getCurrentItem().getItemMeta() == null && !e.getView().getTitle().equals("§cInventorySorting")) {
      e.setCancelled(true);
      return;
    }
    if(Main.playerData.get(p).isBuild()){
        return;
    }
    if(Main.playerData.get(p).isInGame() && (e.getAction().equals(InventoryAction.PICKUP_ALL) || e.getAction().equals(InventoryAction.HOTBAR_SWAP))) {
        e.setCancelled(true);
        p.playSound(p.getLocation(), Sound.NOTE_BASS, 100F, 0F);
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a[&dTeamCocoa&a] &7Please use InventorySorting to edit your inventory layout!"));
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
                e.setCancelled(true);
                return;
        }
        p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 100.0F, 0.0F);
        try {
            Main.inst().kitData.setKit(p, kit);
            Main.playerData.get(p).setKit(kit);
            Main.playerData.get(p).setInventory(Main.inst().kitData.getPlayerKit(p, kit));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a[&dTeamCocoa&a] &aYour settings has been saved."));
        } catch (Exception e1) {
            e1.printStackTrace();
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a[&dTeamCocoa&a] &cAn error has occurred."));
        }
        p.closeInventory();
        return;
    }


    if(e.getView().getTitle().equals("§cInventorySorting")){
        ItemStack saveItem = Main.inst().kitData.createDye("§aSave", Material.INK_SACK, (short)10);
        ItemStack resetItem = Main.inst().kitData.createDye("§cReset", Material.INK_SACK, (short)1);
        ItemStack clickedItem = e.getCurrentItem();
        ItemStack glassItem = Main.inst().kitData.createItemStack(Material.STAINED_GLASS_PANE, " ", 1, new ArrayList(), (byte)7);
        int kit = Main.playerData.get(p).getKit();
        if(clickedItem.equals(glassItem)){
            e.setCancelled(true);
            return;
        }
        if(e.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY) || e
                .getAction().equals(InventoryAction.DROP_ALL_SLOT) || e
                .getAction().equals(InventoryAction.DROP_ALL_CURSOR) || e
                .getAction().equals(InventoryAction.DROP_ONE_SLOT) || e
                .getAction().equals(InventoryAction.DROP_ONE_CURSOR) || e
                .getAction().equals(InventoryAction.HOTBAR_SWAP) || e
                .getClickedInventory().equals(p.getInventory()) || e
                .getClickedInventory().getType().equals(InventoryType.PLAYER)){
            e.setCancelled(true);
            return;
        }
        if(clickedItem.equals(resetItem)){
            ItemStack[] inventory = Main.inst().kitData.getDefaultKit(Main.inst().kitData.getKitByInt(kit));
            try{
                Main.playerData.get(p).setInventory(inventory);
                Main.inst().kitData.setInventorySetting(p, inventory, Main.inst().kitData.getKitByInt(kit));
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a[&dTeamCocoa&a] &cYour settings has been reset."));
                p.closeInventory();
            }
            catch(Exception e1){
                e1.printStackTrace();
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a[&dTeamCocoa&a] &cAn error has occurred."));
            }
        }
        if(clickedItem.equals(saveItem)){
            ItemStack[] inventory = new ItemStack[9];
            ItemStack[] defaultInventory = Main.inst().kitData.getDefaultKit(Main.inst().kitData.getKitByInt(kit));
            for(int i = 9; i < 18; i++){
                ItemStack item = e.getInventory().getItem(i);
                int defaultIndex = Arrays.asList(defaultInventory).indexOf(item);
                if(defaultIndex != -1 && !item.equals(new ItemStack(Material.AIR))){
                    inventory[i - 9] = item;
                }
                else{
                    inventory[i - 9] = new ItemStack(Material.AIR);
                }
            }
            try{
                Main.inst().kitData.setInventorySetting(p, inventory, Main.inst().kitData.getKitByInt(kit));
                Main.playerData.get(p).setInventory(inventory);
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a[&dTeamCocoa&a] &aYour settings has been saved."));
                p.closeInventory();
            }
            catch(Exception e1){
                e1.printStackTrace();
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a[&dTeamCocoa&a] &cAn error has occurred."));
            }
        }
    }
  }
}
