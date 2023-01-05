package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.translate.InventoryNode;
import kr.teamcocoa.buildffa.main.BuildFFA;
import kr.teamcocoa.buildffa.utils.LangUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCloseListener implements Listener {
    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        if(e.getInventory() != null) {
            if(!BuildFFA.playerData.get(player).isInGame() && !BuildFFA.playerData.get(player).isBuild() && e.getInventory().getName().equals(LangUtils.getMessage(player, InventoryNode.INVENTORY_SORTING))) {
                Bukkit.getScheduler().runTaskLater(BuildFFA.getInstance(), () -> {
                    if(!BuildFFA.playerData.get(player).isInGame() && !BuildFFA.playerData.get(player).isBuild() && e.getInventory().getName().equals(LangUtils.getMessage(player, InventoryNode.INVENTORY_SORTING))) {
                        BuildFFA.playerData.get(player).setJoinInventory();
                    }
                }, 5L);
            }
        }
    }
}
