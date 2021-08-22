package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.enums.MessageEnum;
import kr.teamcocoa.buildffa.main.Main;
import kr.teamcocoa.buildffa.kit.BffaPlayer;
import kr.teamcocoa.buildffa.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
  @EventHandler
  public void onJoin(PlayerJoinEvent e) {
    final Player p = e.getPlayer();
    p.setLevel(0);
    String uuid = String.valueOf(p.getUniqueId());
    Main.inst().stats.createPlayer(uuid);
    Main.playerData.put(p, new BffaPlayer(p));
    Main.inst().scoreboardManager.setScoreboard(p);
    if (Config.config.getBoolean("join-quit-message")) {
      if (Config.config.getBoolean("displayname.joinmessage")) {
        e.setJoinMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("joinmessage").replaceAll("%PLAYER%", p.getDisplayName()).replaceAll("&", "§"));
      } else {
        e.setJoinMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("joinmessage").replaceAll("%PLAYER%", p.getName()).replaceAll("&", "§"));
      }

      Bukkit.getScheduler().runTaskLater(Main.inst(), () -> {
        Title.sendTitle(p,
                ChatColor.translateAlternateColorCodes('&', "&4/Kits"),
                LangUtils.getMessage(p, MessageEnum.JOIN_TITLE),
                10, 80, 10);
      }, 5L);

    } else {
      e.setJoinMessage(null);
    } 
//    if (!Config.config.getString("startmap").equals("none")) {
//      if (Locations.getCurrentMap().equals("Keine Map"))
//        Locations.setCurrentMap(Config.config.getString("startmap").replaceAll("&", "§"));
//    } else {
//      p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nostartmap.01").replaceAll("&", "§"));
//      p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nostartmap.02").replaceAll("&", "§"));
//      p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nostartmap.03").replaceAll("&", "§"));
//      return;
//    }
    if (Config.locations.getString(String.valueOf(Locations.CurrentMapname) + ".deathheight") != null) {
      if (Config.locations.getString(String.valueOf(Locations.CurrentMapname) + ".arenaheight") != null) {
        Location mapspawn = Locations.getSpawnLocation(Locations.CurrentMapname);
        p.teleport(mapspawn);
        if (!Main.playerData.get(p).isBuild()) {
          Main.playerData.get(p).setJoinInventory();
          p.setHealth(1.0D);
          p.setFoodLevel(20);
        } 
      }
//      else {
//        p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("noarenaheight.01").replaceAll("&", "§"));
//        p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("noarenaheight.02").replaceAll("&", "§"));
//      }
    }
//    else {
//      p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nodeathheight.01").replaceAll("&", "§"));
//      p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nodeathheight.02").replaceAll("&", "§"));
//    }
  }
}
