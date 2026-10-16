package kr.teamcocoa.buildffa.task;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;

public class LobbyBarrierTask implements Runnable {

  @Override
  public void run() {
    World world = Bukkit.getWorld("ArenaWorld");
    world.getBlockAt(0, 4, 0).setType(Material.BARRIER);
    world.getBlockAt(0, 7, 0).setType(Material.BARRIER);
    world.getBlockAt(1, 6, 0).setType(Material.BARRIER);
    world.getBlockAt(-1, 6, 0).setType(Material.BARRIER);
    world.getBlockAt(0, 6, 1).setType(Material.BARRIER);
    world.getBlockAt(0, 6, -1).setType(Material.BARRIER);
  }
}
