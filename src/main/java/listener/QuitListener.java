package listener;

import main.Main;
import utils.Config;
import utils.Stats;
import java.io.IOException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {
  @EventHandler
  public static void onQuit(PlayerQuitEvent e) {
    Player p = e.getPlayer();
    if (PlayerMoveListener.gotInventory.contains(p))
      Stats.addDeaths(p.getUniqueId().toString(), Integer.valueOf(1)); 
    if (Config.player.getString("players." + p.getUniqueId() + ".killstreak") != null) {
      Config.player.set("players." + p.getUniqueId() + ".killstreak", null);
      try {
        Config.player.save(Config.playerFile);
      } catch (IOException e2) {
        e2.printStackTrace();
      } 
    } 
    if (Config.config.getBoolean("join-quit-message")) {
      if (Config.config.getBoolean("displayname.quitmessage")) {
        e.setQuitMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("quitmessage").replaceAll("%PLAYER%", p.getDisplayName()).replaceAll("&", "§"));
      } else {
        e.setQuitMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("quitmessage").replaceAll("%PLAYER%", p.getName()).replaceAll("&", "§"));
      } 
    } else {
      e.setQuitMessage(null);
    } 
  }
}
