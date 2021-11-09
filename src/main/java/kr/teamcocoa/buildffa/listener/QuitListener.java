package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.enums.MessageEnum;
import kr.teamcocoa.buildffa.kit.BffaPlayer;
import kr.teamcocoa.buildffa.main.Main;
import kr.teamcocoa.buildffa.utils.Config;
import kr.teamcocoa.buildffa.utils.LangUtils;
import kr.teamcocoa.buildffa.world.MapVote;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.Arrays;

public class QuitListener implements Listener {
  @EventHandler
  public void onQuit(PlayerQuitEvent e) {
    Player p = e.getPlayer();
    BffaPlayer bffaPlayer = Main.playerData.get(p);
    MapVote mapVote = MapVote.getInstance();
    if(mapVote.getWherePlayerVoted(p) != null) {
      mapVote.removeVote(p, mapVote.getWherePlayerVoted(p));
    }
    if (Main.playerData.get(p).isInGame()) {
      bffaPlayer.addDeaths();
      try {
        if (bffaPlayer.getLastHitPlayer() != null) {
          final BffaPlayer killerBffaPlayer = Main.playerData.get(bffaPlayer.getLastHitPlayer());
          final String nameKiller = bffaPlayer.getLastHitPlayer().getName();
          Player killer = killerBffaPlayer.getPlayer();
          killer.playSound(killer.getLocation(), Sound.ORB_PICKUP, 1F, 1F);
          killerBffaPlayer.addKills();
          if(killerBffaPlayer.isNicked()) {
            killerBffaPlayer.getNickedBffaPlayer().addKills();
          }
          String KillerHealth = (new DecimalFormat("#0.0")).format(killer.getHealth() / 2.0D);
          p.sendMessage(LangUtils.getMessage(p, MessageEnum.PLAYER_KILL).replaceAll("%KILLER%", p.getKiller().getName()).replaceAll("%KILLERHEALTH%", KillerHealth));


          int killerKillstreak = killerBffaPlayer.getPlayerKillStreak() + 1;
          killer.setHealth(20.0D);
          killer.setLevel(killerKillstreak);
          killerBffaPlayer.setPlayerKillStreak(killerKillstreak);

          /*
           * 여기서 부터 max kill streak 설정
           * */
          if (killerKillstreak > killerBffaPlayer.getBestKillStreaks()) {
            killerBffaPlayer.setBestKillStreaks(killerKillstreak);
          }
          /*
           * max kill streak 설정 끝
           * */

          if (killerKillstreak % 3 == 0) {
            try {
              String kit = Main.inst().kitData.getKitByInt(Main.inst().kitData.getKit(killer));
              int index = Arrays.asList(killerBffaPlayer.getInventory()).indexOf(new ItemStack(Material.ENDER_PEARL, 2));
              if (killer.getInventory().getItem(index) == null) {
                ItemStack blockItem = new ItemStack(Material.ENDER_PEARL, 1);
                killer.getInventory().setItem(index, blockItem);
              } else if (killer.getInventory().getItem(index).getAmount() < 2) {
                int amount = killer.getInventory().getItem(index).getAmount();
                ItemStack blockItem = new ItemStack(Material.ENDER_PEARL, amount + 1);
                killer.getInventory().setItem(index, blockItem);
              }
              if (kit.toLowerCase() == "archer") {
                int index1 = Arrays.asList(Main.playerData.get(killer).getInventory()).indexOf(new ItemStack(Material.ARROW, 16));
                if (killer.getInventory().getItem(index) == null) {
                  ItemStack blockItem = new ItemStack(Material.ARROW, 5);
                  killer.getInventory().setItem(index1, blockItem);
                } else if (killer.getInventory().getItem(index1).getAmount() < 16) {
                  int amount1 = killer.getInventory().getItem(index1).getAmount();
                  ItemStack blockItem = new ItemStack(Material.ARROW, amount1 + (amount1 + 5 < 16 ? 5 : 5 - (amount1 + 5 - 16)));
                  killer.getInventory().setItem(index1, blockItem);
                }
              }
              killer.playSound(killer.getLocation(), Sound.LEVEL_UP, 100.0F, 0.0F);
            } catch (NullPointerException e1) {
              e1.printStackTrace();
            }
            Main.playerData.put(killer, killerBffaPlayer);
          }


          Bukkit.getScheduler().runTaskLater(Main.inst(), () -> {
            // 코드 원작자 나가 뒤져라 씨발
            // 정리가 시급하다 나중에
            if (bffaPlayer.getPlayerKillStreak() >= 5) {
              String killstreakPlayerString = String.valueOf(bffaPlayer.getPlayerKillStreak());
              for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(LangUtils.getMessage(player, MessageEnum.KILL_STREAK_BROKEN).replaceAll("%KILLSTREAK%", killstreakPlayerString).replaceAll("%KILLER%", nameKiller).replaceAll("%PLAYER%", p.getName()));
              }
            }
            if (killerKillstreak != 0 && (killerKillstreak % 5 == 0 || killerKillstreak > 15)) {
              for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(LangUtils.getMessage(player, MessageEnum.KILL_STREAK).replaceAll("%KILLSTREAK%", String.valueOf(killerKillstreak)).replaceAll("%PLAYER%", nameKiller));
              }
            }
          }, 3L);
        }
      }
      catch (Exception e1) {

      }
    }
    Main.inst().stats.updatePlayer(bffaPlayer);
    Main.playerData.remove(p);
//    if (Config.config.getBoolean("join-quit-message")) {
//      if (Config.config.getBoolean("displayname.quitmessage")) {
//        e.setQuitMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("quitmessage").replaceAll("%PLAYER%", p.getDisplayName()).replaceAll("&", "§"));
//      } else {
//        e.setQuitMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("quitmessage").replaceAll("%PLAYER%", p.getName()).replaceAll("&", "§"));
//      }
//    } else {
//      e.setQuitMessage(null);
//    }
    e.setQuitMessage(null);
  }
}
