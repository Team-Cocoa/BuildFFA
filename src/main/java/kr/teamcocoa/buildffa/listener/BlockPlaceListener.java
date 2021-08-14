package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.block.DespawnBlock;
import kr.teamcocoa.buildffa.main.Main;

import kr.teamcocoa.buildffa.utils.Config;
import kr.teamcocoa.buildffa.utils.Locations;
import kr.teamcocoa.buildffa.block.RemoveBlockAnimation;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;



public class BlockPlaceListener implements Listener {
  @EventHandler
  public void onBlockPlace(final BlockPlaceEvent e) {
    Block block = e.getBlock();
    if(block.getX() == 1000 && block.getZ() == 1000) {
      e.setCancelled(true);
      return;
    }
    Player p = e.getPlayer();
    double height = Config.locations.getDouble(String.valueOf(Locations.CurrentMapname) + ".arenaheight");
    if (p.getLocation().getY() <= height) {
      if (!Main.playerData.get(p).isBuild()) {
        Block b = e.getBlock();
        DespawnBlock despawnBlock = new DespawnBlock(e, b);
        Main.worldData.addBlock(b);
        RemoveBlockAnimation.blocks.add(despawnBlock);
//        new RemoveBlockAnimation(e, b).runTaskTimer(Main.inst(), 0L, 10L);
      }
    } else if (!Main.playerData.get(p).isBuild()) {
      e.setCancelled(true);
    }
  }
}
