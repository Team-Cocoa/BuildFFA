package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.main.Main;

import kr.teamcocoa.buildffa.utils.Config;
import kr.teamcocoa.buildffa.utils.Locations;
import kr.teamcocoa.buildffa.utils.RemoveBlockAnimation;

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
      if (!Main.playerData.get(p).isBuild()) {
        Block b = e.getBlock();
        Main.worldData.addBlock(b);
        new RemoveBlockAnimation(e, b).runTaskTimer(Main.inst(), 0L, 10L);
      } 
    } else if (!Main.playerData.get(p).isBuild()) {
      e.setCancelled(true);
    } 
  }
}
