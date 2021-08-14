package kr.teamcocoa.buildffa.utils;

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

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class ScoreboardManager implements Listener {
    public static int sec = 600;
    public static int i = (int)(Math.random() * 2) + 1;
    public static boolean pvpAble = true;
  
  public void setScoreboard(Player player) {
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
      scores.add(getScorePacket(scoreboard, objective, color("&fKills:"), 6));
      scores.add(getScorePacket(scoreboard, objective, color("&8» &e" + kills), 5));
      scores.add(getScorePacket(scoreboard, objective, " ", 4));
      scores.add(getScorePacket(scoreboard, objective, color("&fBest Killstreak:"), 3));
      scores.add(getScorePacket(scoreboard, objective, color("&8» &e" + killstreak + " "), 2));
      scores.add(getScorePacket(scoreboard, objective, "  ", 1));
      scores.add(getScorePacket(scoreboard, objective, color((Main.teaming ? "&a&lTeaming is Allowed" : "&4&lTeaming is Prohibited")), 0));

      PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
      connection.sendPacket(removeObjective);
      connection.sendPacket(createObjective);
      connection.sendPacket(displayObjective);
      for (PacketPlayOutScoreboardScore packets : scores) {
        connection.sendPacket(packets);
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
  
  public static void mapChangeUpdater() {

      Bukkit.getScheduler().runTaskTimer(Main.inst(), () -> {
          --sec;
          LocalTime localTime = LocalTime.ofSecondOfDay(sec);
          String time = localTime.toString();
          for(Player player : Bukkit.getOnlinePlayers()) {
              Bar.sendDefaultBar(player, time);
          }
          if(sec == 5) {
              pvpAble = false;
          }
          switch(sec) {
              case 600:
              case 300:
              case 180:
              case 60:
              case 30:
              case 10:
              case 5:
              case 4:
              case 3:
              case 2:
              case 1:
                  sendCountdownMessage();
                  break;
              case 0:
                  sec = 600;
                  i = i + 1 < 4 ? i + 1 : 1;
                  Locations.MapChange(i);
                  pvpAble = true;
                  break;
              default:
                  break;
          }
          }, 0L, 20L);
  }

  public static void sendAllPlayer(String string) {
      String message = ChatColor.translateAlternateColorCodes('&', string);
      for(Player player : Bukkit.getOnlinePlayers()) {
          player.sendMessage(message);
      }
  }

  public static void sendCountdownMessage() {
      if(sec >= 60) {
          sendAllPlayer(sec / 60 == 1 ? "&a[&dTeamCocoa&a] &aThe map will be changed in &e&l" + sec / 60 + "&a minute!" : "&a[&dTeamCocoa&a] &aThe map will be changed in &e&l" + sec / 60 + "&a minutes!");
      }
      else {
          sendAllPlayer(sec != 1 ? "&a[&dTeamCocoa&a] &aThe map will be changed in &e&l" + sec + "&a seconds!" : "&a[&dTeamCocoa&a] &aThe map will be changed in &e&l" + sec + "&a second!");

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
