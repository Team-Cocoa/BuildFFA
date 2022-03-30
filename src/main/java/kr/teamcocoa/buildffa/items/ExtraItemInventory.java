package kr.teamcocoa.buildffa.items;

import kr.teamcocoa.buildffa.enums.InventoryEnum;
import kr.teamcocoa.buildffa.utils.ItemManager;
import kr.teamcocoa.buildffa.utils.LangUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ExtraItemInventory {

    public static void onClickExtraItemInventory(InventoryClickEvent e) {
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

        if(clickedItem.getItemMeta() == null) {
            return;
        }

        if(clickedItem.getItemMeta().getDisplayName().equals(GoldenApple.getInstance().getName(player))) {

        }

        if(clickedItem.getItemMeta().getDisplayName().equals(RescuePlatform.getInstance().getName(player))) {

        }

    }

    public static void openExtraItemInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 3 * 9, LangUtils.getMessage(player, InventoryEnum.EXTRA_ITEM));
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, ItemManager.grayGlassPane);
        }



        for (int i = 18; i < 27; i++) {
            inventory.setItem(i, ItemManager.grayGlassPane);
        }
    }
}
