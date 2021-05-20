package listener;

import commands.Build;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ItemDropListener implements Listener {
  @EventHandler
  public void onItemDrop(PlayerDropItemEvent e) {
    Player p = e.getPlayer();
    if (!Build.buildmode.contains(p.getName()))
      e.setCancelled(true); 
  }
}
