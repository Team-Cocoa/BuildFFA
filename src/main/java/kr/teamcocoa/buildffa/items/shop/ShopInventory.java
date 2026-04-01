package kr.teamcocoa.buildffa.items.shop;

import kr.teamcocoa.buildffa.enums.InventoryEnum;
import kr.teamcocoa.buildffa.enums.MessageEnum;
import kr.teamcocoa.buildffa.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.models.BuildFFAPlayerManager;
import kr.teamcocoa.buildffa.utils.LangUtils;
import kr.teamcocoa.core.bukkit.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ShopInventory {

    public static void onClickShopInventory(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        if(e.getCurrentItem() == null) {
            return;
        }

        ItemStack clickedItem = e.getCurrentItem();

        if(!clickedItem.hasItemMeta()) {
            return;
        }

        e.setCancelled(true);

        BuyStatus status = null;

        ItemMeta clickedItemMeta = clickedItem.getItemMeta();

        BuildFFAPlayer buildFFAPlayer = BuildFFAPlayerManager.getPlayer(player);

        if(clickedItemMeta.getDisplayName().equalsIgnoreCase(SnowBall.getInstance().getName(player))) {
            status = SnowBall.getInstance().purchase(buildFFAPlayer);
        }
        if(clickedItemMeta.getDisplayName().equalsIgnoreCase(Bow.getInstance().getName(player))) {
            status = Bow.getInstance().purchase(buildFFAPlayer);
        }

        if(status != null) {
            switch (status) {
                case SUCCESS: {
                    player.sendMessage(LangUtils.getMessage(player, MessageEnum.SHOP_BOUGHT));
                    break;
                }
                case FAILED: {
                    player.sendMessage(LangUtils.getMessage(player, MessageEnum.SHOP_CANNOT_BUY));
                    break;
                }
                case ALREADY_BOUGHT: {
                    player.sendMessage(LangUtils.getMessage(player, MessageEnum.SHOP_ALREADY_BOUGHT));
                    break;
                }
            }
        }

    }

    public static void openShopInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 1 * 9, LangUtils.getMessage(player, InventoryEnum.EXTRA_ITEM));
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, ItemUtils.getGUIBackGround());
        }
        inventory.setItem(2, SnowBall.getInstance().getVoteItemStack(player));
        inventory.setItem(6, Bow.getInstance().getVoteItemStack(player));
        player.openInventory(inventory);
    }
}
