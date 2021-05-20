package listener;

import utils.Inventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {
  @EventHandler
  public static void onPlayerRespawn(PlayerRespawnEvent e) {
    Player p = e.getPlayer();
    Inventory.setJoinInventory(p);
  }
}
