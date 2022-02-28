package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.enums.ItemEnum;
import kr.teamcocoa.buildffa.enums.MessageEnum;
import kr.teamcocoa.buildffa.kit.KitData;
import kr.teamcocoa.buildffa.kit.KitEdit;
import kr.teamcocoa.buildffa.main.Main;
import kr.teamcocoa.buildffa.utils.Config;
import kr.teamcocoa.buildffa.utils.LangUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractListener implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        try {
            if (e.getClickedBlock().getType() == Material.STONE_PLATE) {
                return;
            }
        }
        catch(Exception e2){

        }

        if (p.getItemInHand().hasItemMeta() && p.getItemInHand().getItemMeta().getDisplayName() != null) {
            String displayName = p.getItemInHand().getItemMeta().getDisplayName();
            if (displayName.equals(LangUtils.getMessage(p, ItemEnum.KIT))) {
                p.openInventory(Main.inst().kitData.getKitSelection(p));
                return;
            }
            if (displayName.equals(LangUtils.getMessage(p, ItemEnum.INVENTORY_SORTING))) {
                KitEdit.getInstance().openInventorySorting(Main.playerData.get(p));
                return;
            }
            if (displayName.equals(LangUtils.getMessage(p, ItemEnum.LEAVE_ITEM))) {
                p.kickPlayer("");
                return;
            }
            if (displayName.equals("§cKillEffects")){
                p.performCommand("killeffect");
                return;
            }
            if (p.getLocation().getY() >= 207) {
                Bukkit.getLogger().info("a");
                e.setCancelled(true);
                return;
            }
        }
    }
}
