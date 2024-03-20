package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.main.BuildFFA;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        e.setCancelled(true);
        e.setDeathMessage(null);
        e.setKeepInventory(true);
        e.setKeepLevel(true);
        if (BuildFFA.playerData.containsKey(e.getEntity())) {
            BuildFFA.playerData.get(e.getEntity()).death(false);
        }
    }
}
