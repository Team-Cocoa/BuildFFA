package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.main.BuildFFA;
import kr.teamcocoa.buildffa.managers.GUIManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        GUIManager.onClick(e);
        Player p = (Player) e.getWhoClicked();
        if (BuildFFA.playerData.get(p).isBuild()) {
            return;
        }
    }
}
