package listener;

import commands.Build;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {
  @EventHandler
  public void onBlockBreak(BlockBreakEvent e) {
    Player p = e.getPlayer();
    if (!Build.buildmode.contains(p.getName()))
      e.setCancelled(true); 
  }
}
