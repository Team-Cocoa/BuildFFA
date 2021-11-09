package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.enums.MessageEnum;
import kr.teamcocoa.buildffa.main.Main;
import kr.teamcocoa.buildffa.kit.BffaPlayer;
import kr.teamcocoa.buildffa.utils.*;
import kr.teamcocoa.buildffa.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class JoinListener implements Listener {
  @EventHandler
  public void onJoin(PlayerJoinEvent e) {
    final Player p = e.getPlayer();
    Main.playerData.put(p, new BffaPlayer(p));
    p.addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(999999, 1));
    Title.sendTitle(p, "", StringUtils.color("&7Your data is loading..."), 20, 1000, 20);
    e.setJoinMessage(null);
//    if (Config.config.getBoolean("join-quit-message")) {
//      if (Config.config.getBoolean("displayname.joinmessage")) {
//        e.setJoinMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("joinmessage").replaceAll("%PLAYER%", p.getDisplayName()).replaceAll("&", "§"));
//      } else {
//        e.setJoinMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("joinmessage").replaceAll("%PLAYER%", p.getName()).replaceAll("&", "§"));
//      }
//
//
//
//    } else {
//      e.setJoinMessage(null);
//    }


//    Bukkit.getScheduler().runTaskLater(Main.inst(), () -> {
//
//    }, 5L);

    Bukkit.getScheduler().runTaskLaterAsynchronously(Main.inst(), () -> {
      String uuid = String.valueOf(p.getUniqueId());
      Main.inst().stats.createPlayer(uuid);
      Main.inst().scoreboardManager.setScoreboard(p);
      Title.sendTitle(p,
              ChatColor.translateAlternateColorCodes('&', "&4/Kits"),
              LangUtils.getMessage(p, MessageEnum.JOIN_TITLE),
              10, 80, 10);
      for(PotionEffect effect : p.getActivePotionEffects()) {
        p.removePotionEffect(effect.getType());
      }
      Bukkit.getScheduler().runTaskLater(Main.inst(), () -> {
        Location spawn = WorldManager.getInstance().getSpawnByName(WorldManager.getInstance().getCurrentMap());
        p.teleport(spawn);
        p.setLevel(0);
        p.setHealth(1.0D);
        p.setFoodLevel(20);
        if (!Main.playerData.get(p).isBuild()) {
          Main.playerData.get(p).setInGame(false);
          Main.playerData.get(p).setJoinInventory();
        }
      }, 1L);
    }, 5L);
//    p.teleport(spawn);

  }
}
