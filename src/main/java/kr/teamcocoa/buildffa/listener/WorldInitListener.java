package kr.teamcocoa.buildffa.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;

public class WorldInitListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldLoad(WorldInitEvent e) {
        e.getWorld().setKeepSpawnInMemory(false);
    }

    @EventHandler
    public void onLoad(WorldLoadEvent e) {
        e.getWorld().setKeepSpawnInMemory(false);
    }
}
