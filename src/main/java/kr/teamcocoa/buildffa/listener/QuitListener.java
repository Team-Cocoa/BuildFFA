package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.kit.BffaPlayer;
import kr.teamcocoa.buildffa.main.Main;
import kr.teamcocoa.buildffa.utils.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {
  @EventHandler
  public void onQuit(PlayerQuitEvent e) {
    Player p = e.getPlayer();
    BffaPlayer bffaPlayer = Main.playerData.get(p);
    if (Main.playerData.get(p).isInGame()) {
      bffaPlayer.addDeaths();
    }
    Main.inst().stats.updatePlayer(bffaPlayer);
    Main.playerData.remove(p);
//    if (Config.player.getString("players." + p.getUniqueId() + ".killstreak") != null) {
//      Config.player.set("players." + p.getUniqueId() + ".killstreak", null);
//      try {
//        Config.player.save(Config.playerFile);
//      } catch (IOException e2) {
//        e2.printStackTrace();
//      }
//    }
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
