package listener;

import commands.Build;
import main.Main;

import utils.Config;
import utils.Locations;
import utils.RemoveBlockAnimation;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;



public class BlockPlaceListener implements Listener {
  @EventHandler
  public void onBlockPlace(final BlockPlaceEvent e) {
    Player p = e.getPlayer();
    double height = Config.locations.getDouble(String.valueOf(Locations.getCurrentMap()) + ".arenaheight");
    if (p.getLocation().getY() <= height) {
      if (!Build.buildmode.contains(p.getName())) {
        Block b = e.getBlock();
        Main.worldData.addBlock(b);
        new RemoveBlockAnimation(p, b).runTaskTimer(Main.inst(), 0L, 10L);

      } 
    } else if (!Build.buildmode.contains(p.getName())) {
      e.setCancelled(true);
    } 
  }
}
