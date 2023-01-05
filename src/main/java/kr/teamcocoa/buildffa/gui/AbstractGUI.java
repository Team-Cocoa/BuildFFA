package kr.teamcocoa.buildffa.gui;

import kr.teamcocoa.buildffa.translate.InventoryNode;
import kr.teamcocoa.buildffa.utils.ItemManager;
import kr.teamcocoa.buildffa.utils.LangUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public abstract class AbstractGUI {

    protected Inventory inventory;
    protected InventoryNode name;

    public abstract void openInventory(Player player);
    public abstract void onClick(InventoryClickEvent e);
    public abstract void onClose(InventoryCloseEvent e);

    protected void fillInventory() {
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, ItemManager.grayGlassPane);
        }
    }

    protected boolean checkThisInventoryClicked(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        return e.getView().getTitle().equals(LangUtils.getMessage(player, name));
    }

    protected boolean checkItemStack(InventoryClickEvent e) {
        return e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta();
    }

}
