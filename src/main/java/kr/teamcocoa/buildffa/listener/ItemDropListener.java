package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.main.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ItemDropListener implements Listener {
  @EventHandler
  public void onItemDrop(PlayerDropItemEvent e) {
    Player p = e.getPlayer();
    if (!Main.playerData.get(p).isBuild()) {
      e.setCancelled(true);
    }
  }
}
