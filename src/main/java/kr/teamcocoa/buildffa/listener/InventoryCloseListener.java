package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.enums.InventoryEnum;
import kr.teamcocoa.buildffa.main.Main;
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
            if(!Main.playerData.get(player).isInGame() && !Main.playerData.get(player).isBuild() && e.getInventory().getName().equals(LangUtils.getMessage(player, InventoryEnum.INVENTORY_SORTING))) {
                Bukkit.getScheduler().runTaskLater(Main.inst(), () -> {
                    if(!Main.playerData.get(player).isInGame() && !Main.playerData.get(player).isBuild() && e.getInventory().getName().equals(LangUtils.getMessage(player, InventoryEnum.INVENTORY_SORTING))) {
                        Main.playerData.get(player).setJoinInventory();
                    }
                }, 5L);
            }
        }
    }
}
