package kr.teamcocoa.buildffa.utils;

import kr.teamcocoa.buildffa.enums.MessageEnum;
import kr.teamcocoa.buildffa.enums.OtherEnum;
import kr.teamcocoa.buildffa.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import net.minecraft.server.v1_8_R3.IScoreboardCriteria;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardObjective;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.Scoreboard;
import net.minecraft.server.v1_8_R3.ScoreboardObjective;
import net.minecraft.server.v1_8_R3.ScoreboardScore;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

import java.util.ArrayList;
import java.util.List;


public class ScoreboardManager implements Listener {
  
  public void setScoreboard(Player player) {
      try {
          int kills = Main.playerData.get(player).getKills();
          int killstreak = Main.playerData.get(player).getBestKillStreaks();

          Scoreboard scoreboard = new Scoreboard();
          ScoreboardObjective objective = scoreboard.registerObjective("buildffa", IScoreboardCriteria.b);
          objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&dBuildFFA"));

          PacketPlayOutScoreboardObjective removeObjective = new PacketPlayOutScoreboardObjective(objective, 1);

          PacketPlayOutScoreboardObjective createObjective = new PacketPlayOutScoreboardObjective(objective, 0);
          PacketPlayOutScoreboardDisplayObjective displayObjective = new PacketPlayOutScoreboardDisplayObjective(1, objective);

          List<PacketPlayOutScoreboardScore> scores = new ArrayList<>();
          scores.add(getScorePacket(scoreboard, objective, color("&aTeamCocoa.kr"), 8));
          scores.add(getScorePacket(scoreboard, objective, "", 7));
          scores.add(getScorePacket(scoreboard, objective, LangUtils.getMessage(player, OtherEnum.SCOREBOARD_KILLS), 6));
          scores.add(getScorePacket(scoreboard, objective, color("&8» &e" + kills), 5));
          scores.add(getScorePacket(scoreboard, objective, " ", 4));
          scores.add(getScorePacket(scoreboard, objective, LangUtils.getMessage(player, OtherEnum.SCOREBOARD_BEST_KILL_STREAK), 3));
          scores.add(getScorePacket(scoreboard, objective, color("&8» &e" + killstreak + " "), 2));
          scores.add(getScorePacket(scoreboard, objective, "  ", 1));
          scores.add(getScorePacket(scoreboard, objective, LangUtils.getMessage(player, Main.teaming ? OtherEnum.SCOREBOARD_TEAMING_ALLOW : OtherEnum.SCOREBOARD_TEAMING_PROHIBIT), 0));

          PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
          connection.sendPacket(removeObjective);
          connection.sendPacket(createObjective);
          connection.sendPacket(displayObjective);
          for (PacketPlayOutScoreboardScore packets : scores) {
              connection.sendPacket(packets);
          }
      }
      catch(NullPointerException e) {

      }
  }

  private PacketPlayOutScoreboardScore getScorePacket(Scoreboard scoreboard, ScoreboardObjective objective, String display, int scoreValue) {
    ScoreboardScore score = new ScoreboardScore(scoreboard, objective, display);
    score.setScore(scoreValue);
    return new PacketPlayOutScoreboardScore(score);
  }

  private String color(String string) {
    return ChatColor.translateAlternateColorCodes('&', string);
  }
  
//  public static void mapChangeUpdater() {
//
//      Bukkit.getScheduler().runTaskTimer(Main.inst(), () -> {
//          --sec;
//          LocalTime localTime = LocalTime.ofSecondOfDay(sec);
//          String time = localTime.toString();
//          for(Player player : Bukkit.getOnlinePlayers()) {
//              Bar.sendDefaultBar(player, time);
//          }
//          if(sec == 5) {
//              Location spawn = Locations.getSpawnLocation(Locations.getMapNameByInt(i = i + 1 < 4 ? i + 1 : 1));
//              if(!spawn.getChunk().isLoaded()) {
//                  spawn.getChunk().load();
//              }
//              pvpAble = false;
//              placeAble = false;
//          }
//          switch(sec) {
//              case 600:
//              case 300:
//              case 180:
//              case 60:
//              case 30:
//              case 10:
//              case 5:
//              case 4:
//              case 3:
//              case 2:
//                  break;
//              case 1:
//                  sendCountdownMessage();
//                  Main.worldData.removeBlocks();
//                  break;
//              case 0:
//                  sec = 600;
//                  i = i + 1 < 4 ? i + 1 : 1;
////                  Locations.MapChange(i);
//                  pvpAble = true;
//                  placeAble = true;
//                  break;
//              default:
//                  break;
//          }
//          }, 0L, 20L);
//  }

  public static void sendAllPlayer(MessageEnum node, int sec) {
      for(Player player : Bukkit.getOnlinePlayers()) {
          player.sendMessage(LangUtils.getMessage(player, node).replace("%time%", String.valueOf(sec)));
      }
  }
  
  public void ScoreboardUpdater() {
    (new BukkitRunnable() {
        public void run() {
          for (Player player : Bukkit.getOnlinePlayers()) {
            setScoreboard(player);
          }
        }
      }).runTaskTimer(Main.inst(), 0L, 20L);
  }
}
