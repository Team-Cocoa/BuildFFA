package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.main.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        e.setDeathMessage(null);
        e.setKeepInventory(true);
        e.setKeepLevel(true);
        if (Main.playerData.containsKey(e.getEntity())) {
            Main.playerData.get(e.getEntity()).death(false);
        }

    }
}
