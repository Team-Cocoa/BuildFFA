package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.main.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {
  @EventHandler
  public static void onPlayerRespawn(PlayerRespawnEvent e) {
    Player p = e.getPlayer();
    Main.playerData.get(p).setJoinInventory();
  }
}
