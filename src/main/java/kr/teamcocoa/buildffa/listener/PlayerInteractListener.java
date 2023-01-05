package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.gui.KitEditInventory;
import kr.teamcocoa.buildffa.gui.ShopInventory;
import kr.teamcocoa.buildffa.items.extra.RescuePlatform;
import kr.teamcocoa.buildffa.managers.GUIManager;
import kr.teamcocoa.buildffa.translate.ItemNode;
import kr.teamcocoa.buildffa.utils.LangUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        try {
            if (e.getClickedBlock().getType() == Material.STONE_PLATE) {
                return;
            }
        }
        catch(Exception e2){

        }

        RescuePlatform.getInstance().onClick(e);

        if (player.getItemInHand().hasItemMeta() && player.getItemInHand().getItemMeta().getDisplayName() != null) {
            String displayName = player.getItemInHand().getItemMeta().getDisplayName();
            if (displayName.equals(LangUtils.getMessage(player, ItemNode.INVENTORY_SORTING))) {
                GUIManager.getGUI(KitEditInventory.class).openInventory(player);
                return;
            }
            if (displayName.equals(LangUtils.getMessage(player, ItemNode.LEAVE_ITEM))) {
                player.kickPlayer("");
                return;
            }
            if (displayName.equals(LangUtils.getMessage(player, ItemNode.SHOP))) {
                GUIManager.getGUI(ShopInventory.class).openInventory(player);
                return;
            }
            if (displayName.equals("§cKillEffects")){
                player.performCommand("killeffect");
                return;
            }
            if (player.getLocation().getY() >= 207) {
                e.setCancelled(true);
                return;
            }
        }
    }
}
