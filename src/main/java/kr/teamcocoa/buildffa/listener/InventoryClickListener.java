package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.translate.InventortNode;
import kr.teamcocoa.buildffa.translate.ItemNode;
import kr.teamcocoa.buildffa.translate.MessageNode;
import kr.teamcocoa.buildffa.items.shop.ShopInventory;
import kr.teamcocoa.buildffa.kit.KitEdit;
import kr.teamcocoa.buildffa.main.BuildFFA;
import kr.teamcocoa.buildffa.utils.LangUtils;
import kr.teamcocoa.buildffa.world.MapVoteInventory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        MapVoteInventory.getInstance().onClickInventory(e);
        ShopInventory.onClickShopInventory(e);
        Player p = (Player) e.getWhoClicked();
        if (BuildFFA.playerData.get(p).isBuild()) {
            return;
        }


        if (e.getView().getTitle().equals(LangUtils.getMessage(p, InventortNode.INVENTORY_SORTING))) {
            ItemStack saveItem = BuildFFA.getInstance().kitData.createDye(LangUtils.getMessage(p, ItemNode.SAVE), Material.INK_SACK, (short) 10);
            ItemStack resetItem = BuildFFA.getInstance().kitData.createDye(LangUtils.getMessage(p, ItemNode.RESET), Material.INK_SACK, (short) 1);
            ItemStack clickedItem = e.getCurrentItem();
            if(clickedItem == null) {
                e.setCancelled(true);
                return;
            }
            ItemStack glassItem = BuildFFA.getInstance().kitData.createItemStack(Material.STAINED_GLASS_PANE, " ", 1, new ArrayList(), (byte) 7);
            if (clickedItem.equals(glassItem)) {
                e.setCancelled(true);
                return;
            }

            if (clickedItem.equals(resetItem)) {
                try {
                    boolean saved = KitEdit.getInstance().resetInventorySetting(p);
                    if (saved) {
                        BuildFFA.playerData.get(p).setInventory(BuildFFA.getInstance().kitData.getDefaultKit());
                        p.sendMessage(LangUtils.getMessage(p, MessageNode.SETTING_RESET));
                    }
                    else {
                        p.sendMessage(LangUtils.getMessage(p, MessageNode.SETTING_ERROR));
                    }
                    p.closeInventory();
                    p.getInventory().clear();
                    Bukkit.getScheduler().runTaskLater(BuildFFA.getInstance(), () -> BuildFFA.playerData.get(p).setJoinInventory(), 5L);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    p.sendMessage(LangUtils.getMessage(p, MessageNode.SETTING_ERROR));
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
                        BuildFFA.playerData.get(p).setInventory(inventory);
                        p.sendMessage(LangUtils.getMessage(p, MessageNode.SETTING_SAVED));
                    } else {
                        p.sendMessage(LangUtils.getMessage(p, MessageNode.SETTING_ERROR));
                    }
                    p.closeInventory();
                    p.getInventory().clear();
                    Bukkit.getScheduler().runTaskLater(BuildFFA.getInstance(), () -> BuildFFA.playerData.get(p).setJoinInventory(), 5L);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    p.sendMessage(LangUtils.getMessage(p, MessageNode.SETTING_ERROR));
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
