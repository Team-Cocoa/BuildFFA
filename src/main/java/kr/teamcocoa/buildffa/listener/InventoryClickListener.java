package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.enums.InventoryEnum;
import kr.teamcocoa.buildffa.items.shop.ShopInventory;
import kr.teamcocoa.buildffa.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.models.BuildFFAPlayerManager;
import kr.teamcocoa.buildffa.utils.LangUtils;
import kr.teamcocoa.buildffa.world.MapVoteInventory;
import kr.teamcocoa.core.bukkit.utils.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(e.getClickedInventory() == null) {
            return;
        }

        if(e.getClickedInventory().getType() == InventoryType.CRAFTING) {
            e.setCancelled(true);
            return;
        }

        Player player = (Player) e.getWhoClicked();
        BuildFFAPlayer buildFFAPlayer = BuildFFAPlayerManager.getPlayer(player);

        if(buildFFAPlayer.isBuild()) {
            return;
        }

        Component title = e.getView().title();

        if(ComponentUtils.componentEquals(title, LangUtils.getMessage(player, InventoryEnum.VOTE))) {
            e.setCancelled(true);
            MapVoteInventory.getInstance().onClickInventory(e);
        }

        if(ComponentUtils.componentEquals(title, LangUtils.getMessage(player, InventoryEnum.EXTRA_ITEM))) {
            e.setCancelled(true);
            ShopInventory.onClickShopInventory(e);
        }

    }
}
