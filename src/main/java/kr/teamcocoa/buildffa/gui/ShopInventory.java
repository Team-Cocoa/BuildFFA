package kr.teamcocoa.buildffa.gui;

import kr.teamcocoa.buildffa.items.shop.Bow;
import kr.teamcocoa.buildffa.items.shop.BuyStatus;
import kr.teamcocoa.buildffa.items.shop.SnowBall;
import kr.teamcocoa.buildffa.translate.InventoryNode;
import kr.teamcocoa.buildffa.translate.MessageNode;
import kr.teamcocoa.buildffa.main.BuildFFA;
import kr.teamcocoa.buildffa.utils.ItemManager;
import kr.teamcocoa.buildffa.utils.LangUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class ShopInventory extends AbstractGUI {

    public ShopInventory() {
        inventory = Bukkit.createInventory(null, 3 * 9);
        name = InventoryNode.EXTRA_ITEM;
        fillInventory();
    }

    @Override
    public void openInventory(Player player) {
        Inventory newInventory = Bukkit.createInventory(null, 3 * 9, LangUtils.getMessage(player, name));
        newInventory.setContents(inventory.getContents());
        newInventory.setItem(11, SnowBall.getInstance().getVoteItemStack(player));
        newInventory.setItem(15, Bow.getInstance().getVoteItemStack(player));
        player.openInventory(newInventory);
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        try {
            Player player = ((Player) e.getWhoClicked());
            if(!checkItemStack(e) || !checkThisInventoryClicked(e)) {
                return;
            }
            ItemStack clickedItem = e.getCurrentItem();

            if(!clickedItem.hasItemMeta()) {
                return;
            }

            BuyStatus status = null;
            e.setCancelled(true);

            if(clickedItem.getItemMeta().getDisplayName().equals(SnowBall.getInstance().getName(player))) {
                status = SnowBall.getInstance().purchase(BuildFFA.playerData.get(player));
            }

            if(clickedItem.getItemMeta().getDisplayName().equals(Bow.getInstance().getName(player))) {
                status = Bow.getInstance().purchase(BuildFFA.playerData.get(player));
            }

            if(status != null) {
                switch(status) {
                    case FAILED:
                        player.sendMessage(LangUtils.getMessage(player, MessageNode.SHOP_CANNOT_BUY));
                        break;
                    case SUCCESS:
                        player.sendMessage(LangUtils.getMessage(player, MessageNode.SHOP_BOUGHT));
                        break;
                    case ALREADY_BOUGHT:
                        player.sendMessage(LangUtils.getMessage(player, MessageNode.SHOP_ALREADY_BOUGHT));
                        break;
                }
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
