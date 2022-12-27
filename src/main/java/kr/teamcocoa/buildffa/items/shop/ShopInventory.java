package kr.teamcocoa.buildffa.items.shop;

import kr.teamcocoa.buildffa.enums.InventoryEnum;
import kr.teamcocoa.buildffa.enums.MessageEnum;
import kr.teamcocoa.buildffa.main.Main;
import kr.teamcocoa.buildffa.utils.ItemManager;
import kr.teamcocoa.buildffa.utils.LangUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ShopInventory {

    public static void onClickShopInventory(InventoryClickEvent e) {
        if(!(e.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) e.getWhoClicked();

        if (!e.getView().getTitle().equals(LangUtils.getMessage(player, InventoryEnum.EXTRA_ITEM))) {
            return;
        }

        if(e.getCurrentItem() == null) {
            return;
        }

        ItemStack clickedItem = e.getCurrentItem();

        if(!clickedItem.hasItemMeta()) {
            return;
        }

        BuyStatus status = null;
        e.setCancelled(true);

        if(clickedItem.getItemMeta().getDisplayName().equals(SnowBall.getInstance().getName(player))) {
            status = SnowBall.getInstance().purchase(Main.playerData.get(player));
        }

        if(clickedItem.getItemMeta().getDisplayName().equals(Bow.getInstance().getName(player))) {
            status = Bow.getInstance().purchase(Main.playerData.get(player));
        }

        if(status != null) {
            switch(status) {
                case FAILED:
                    player.sendMessage(LangUtils.getMessage(player, MessageEnum.SHOP_CANNOT_BUY));
                    break;
                case SUCCESS:
                    player.sendMessage(LangUtils.getMessage(player, MessageEnum.SHOP_BOUGHT));
                    break;
                case ALREADY_BOUGHT:
                    player.sendMessage(LangUtils.getMessage(player, MessageEnum.SHOP_ALREADY_BOUGHT));
                    break;
            }
        }

    }

    public static void openShopInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 3 * 9, LangUtils.getMessage(player, InventoryEnum.EXTRA_ITEM));
        for (int i = 0; i < 27; i++) {
            inventory.setItem(i, ItemManager.grayGlassPane);
        }
        inventory.setItem(11, SnowBall.getInstance().getVoteItemStack(player));
        inventory.setItem(15, Bow.getInstance().getVoteItemStack(player));
        player.openInventory(inventory);
    }
}
