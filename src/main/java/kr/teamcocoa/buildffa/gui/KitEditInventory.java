package kr.teamcocoa.buildffa.gui;

import kr.teamcocoa.buildffa.database.BuildFFADatabase;
import kr.teamcocoa.buildffa.managers.PlayerManager;
import kr.teamcocoa.buildffa.model.BuildFFAInventory;
import kr.teamcocoa.buildffa.model.BuildFFAPlayer;
import kr.teamcocoa.buildffa.translate.InventoryNode;
import kr.teamcocoa.buildffa.translate.ItemNode;
import kr.teamcocoa.buildffa.translate.MessageNode;
import kr.teamcocoa.buildffa.utils.ItemManager;
import kr.teamcocoa.buildffa.utils.LangUtils;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class KitEditInventory extends AbstractGUI {

    private static ThreadPoolExecutor saveExecutor = new ThreadPoolExecutor(1, 10, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10));
    private static ThreadPoolExecutor resetExecutor = new ThreadPoolExecutor(1, 10, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10));

    public KitEditInventory() {
        inventory = Bukkit.createInventory(null, 1 * 9);
        name = InventoryNode.INVENTORY_SORTING;
        fillInventory();
    }

    @Override
    public void openInventory(Player player) {
        Inventory newInventory = Bukkit.createInventory(null, 1 * 9, LangUtils.getMessage(player, name));
        newInventory.setContents(inventory.getContents());
        ItemStack saveItem = ItemManager.createDye(LangUtils.getMessage(player, ItemNode.SAVE), DyeColor.GREEN);
        ItemStack resetItem = ItemManager.createDye(LangUtils.getMessage(player, ItemNode.RESET), DyeColor.RED);
        newInventory.setItem(3, saveItem);
        newInventory.setItem(5, resetItem);

        BuildFFAPlayer buildFFAPlayer = PlayerManager.getPlayer(player);
        if(buildFFAPlayer == null) {
            return;
        }

        buildFFAPlayer.setInGameInventory(false);
        player.openInventory(newInventory);
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        try {
            Player player = ((Player) e.getWhoClicked());
            if(!checkThisInventoryClicked(e)) {
                return;
            }

            BuildFFAPlayer buildFFAPlayer = PlayerManager.getPlayer(player);
            if(buildFFAPlayer == null) {
                return;
            }

            // 이거 이름으로 구별하고 싶긴 한데
            // 바닐라 아이템은 ItemMeta 가 null 이 나오거나 displayName 가
            // NPE 가 발생할 수 있기 때문에 ItemStack instance 그 자체로 비교할 예정
            ItemStack saveItem = ItemManager.createDye(LangUtils.getMessage(player, ItemNode.SAVE), DyeColor.GREEN);
            ItemStack resetItem = ItemManager.createDye(LangUtils.getMessage(player, ItemNode.RESET), DyeColor.RED);
            ItemStack glassItem = ItemManager.grayGlassPane;
            ItemStack clickedItem = e.getCurrentItem();

            // 저장을 눌렀을때
            if(clickedItem.equals(saveItem)) {
                e.setCancelled(true);
                ItemStack[] inventory = player.getInventory().getContents().clone();
                player.closeInventory();
                saveExecutor.execute(() -> {
                    boolean valid = BuildFFAInventory.setKit(buildFFAPlayer.getInventory(), inventory);
                    if(!valid) {
                        player.sendMessage(LangUtils.getMessage(player, MessageNode.SETTING_ERROR));
                        return;
                    }
                    boolean saved = BuildFFADatabase.getInventoryDatabase().setInventory(player.getUniqueId(), buildFFAPlayer.getInventory());
                    if (saved) {
                        player.sendMessage(LangUtils.getMessage(player, MessageNode.SETTING_SAVED));
                    } else {
                        player.sendMessage(LangUtils.getMessage(player, MessageNode.SETTING_ERROR));
                    }
                });
                return;
            }

            // 리셋을 눌렀을때
            if(clickedItem.equals(resetItem)) {
                e.setCancelled(true);
                player.closeInventory();
                resetExecutor.execute(() -> {
                    BuildFFAInventory.setDefaultKit(buildFFAPlayer.getInventory());
                    boolean saved = BuildFFADatabase.getInventoryDatabase().resetInventory(player.getUniqueId());
                    if(saved) {
                        player.sendMessage(LangUtils.getMessage(player, MessageNode.SETTING_RESET));
                    }
                    else {
                        player.sendMessage(LangUtils.getMessage(player, MessageNode.SETTING_ERROR));
                    }
                });
                return;
            }

            // 회색 유리판을 눌렀을때
            if(clickedItem.equals(glassItem)) {
                e.setCancelled(true);
                return;
            }

        }
        catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onClose(InventoryCloseEvent e) {

    }
}
