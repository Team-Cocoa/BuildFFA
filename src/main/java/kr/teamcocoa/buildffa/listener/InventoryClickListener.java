package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.enums.InventoryEnum;
import kr.teamcocoa.buildffa.enums.ItemEnum;
import kr.teamcocoa.buildffa.enums.MessageEnum;
import kr.teamcocoa.buildffa.items.shop.ShopInventory;
import kr.teamcocoa.buildffa.kit.BffaPlayer;
import kr.teamcocoa.buildffa.kit.KitData;
import kr.teamcocoa.buildffa.kit.KitEdit;
import kr.teamcocoa.buildffa.main.Main;
import kr.teamcocoa.buildffa.utils.Bar;
import kr.teamcocoa.buildffa.utils.LangUtils;
import kr.teamcocoa.buildffa.world.MapVoteInventory;
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
        Bukkit.getLogger().info(e.getEventName() + "is executed!");
        MapVoteInventory.getInstance().onClickInventory(e);
        ShopInventory.onClickShopInventory(e);
        Player p = (Player) e.getWhoClicked();
        if (Main.playerData.get(p).isBuild()) {
            return;
        }


        if (e.getView().getTitle().equals(LangUtils.getMessage(p, InventoryEnum.INVENTORY_SORTING))) {
            ItemStack saveItem = Main.inst().kitData.createDye(LangUtils.getMessage(p, ItemEnum.SAVE), Material.INK_SACK, (short) 10);
            ItemStack resetItem = Main.inst().kitData.createDye(LangUtils.getMessage(p, ItemEnum.RESET), Material.INK_SACK, (short) 1);
            ItemStack clickedItem = e.getCurrentItem();
            ItemStack glassItem = Main.inst().kitData.createItemStack(Material.STAINED_GLASS_PANE, " ", 1, new ArrayList(), (byte) 7);
            if (clickedItem.equals(glassItem)) {
                e.setCancelled(true);
                return;
            }

            if (clickedItem.equals(resetItem)) {
                try {
                    boolean saved = KitEdit.getInstance().resetInventorySetting(p);
                    if (saved) {
                        Main.playerData.get(p).setInventory(Main.inst().kitData.getDefaultKit());
                        p.sendMessage(LangUtils.getMessage(p, MessageEnum.SETTING_RESET));
                    }
                    else {
                        p.sendMessage(LangUtils.getMessage(p, MessageEnum.SETTING_ERROR));
                    }
                    p.closeInventory();
                    p.getInventory().clear();
                    Bukkit.getScheduler().runTaskLater(Main.inst(), () -> Main.playerData.get(p).setJoinInventory(), 5L);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    p.sendMessage(LangUtils.getMessage(p, MessageEnum.SETTING_ERROR));
                }
            }
            if (clickedItem.equals(saveItem)) {
                try {
                    ItemStack[] inventory = new ItemStack[9];
                    for (int i = 0; i < 9; i++) {
                        inventory[i] = p.getInventory().getContents()[i];
                    }
                    boolean saved = KitEdit.getInstance().setInventorySetting(p, inventory);
                    if (saved) {
                        Main.playerData.get(p).setInventory(inventory);
                        p.sendMessage(LangUtils.getMessage(p, MessageEnum.SETTING_SAVED));
                    } else {
                        p.sendMessage(LangUtils.getMessage(p, MessageEnum.SETTING_ERROR));
                    }
                    p.closeInventory();
                    p.getInventory().clear();
                    Bukkit.getScheduler().runTaskLater(Main.inst(), () -> Main.playerData.get(p).setJoinInventory(), 5L);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    p.sendMessage(LangUtils.getMessage(p, MessageEnum.SETTING_ERROR));
                }
            }
            return;
        }
//        if (Main.playerData.get(p).isInGame() && (e.getAction().equals(InventoryAction.PICKUP_ALL) && !e.getView().getTitle().equals(LangUtils.getMessage(p, InventoryEnum.VOTE)) || e.getAction().equals(InventoryAction.HOTBAR_SWAP))) {
//            e.setCancelled(true);
//            p.playSound(p.getLocation(), Sound.NOTE_BASS, 100F, 0F);
//            p.sendMessage(LangUtils.getMessage(p, MessageEnum.USE_INVENTORY_SORTING));
//            return;
//        }
    }
}
