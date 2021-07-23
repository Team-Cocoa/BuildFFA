package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.main.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCloseListener implements Listener {
    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        if(!Main.playerData.get(player).isInGame()) {
            Main.playerData.get(player).setJoinInventory();
        }
    }
}
