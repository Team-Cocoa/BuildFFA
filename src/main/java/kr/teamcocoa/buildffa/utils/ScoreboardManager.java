package kr.teamcocoa.buildffa.utils;

import kr.teamcocoa.buildffa.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
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
  
  private String timeSek = "00";
  
  private String timeMinutes = "0";
  
  private double i = Config.config.getInt("mapchangedelaysek");
  
  public Boolean Mapchangeupdater = Boolean.valueOf(false);
  
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
  
//  public static void MapChangeUpdater() {
//    Mapchangeupdater = Boolean.valueOf(true);
//    (new BukkitRunnable() {
//        public void run() {
//          if (ScoreboardManager.i == 3.0D)
//            Bukkit.broadcastMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("mapchange.01").replaceAll("&", "§"));
//          if (ScoreboardManager.i == 2.0D)
//            Bukkit.broadcastMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("mapchange.02").replaceAll("&", "§"));
//          if (ScoreboardManager.i == 1.0D)
//            Bukkit.broadcastMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("mapchange.03").replaceAll("&", "§"));
//          if (ScoreboardManager.i == 0.0D) {
//            ScoreboardManager.i = Config.config.getInt("mapchangedelaysek");
//            Locations.MapChange();
//          } else {
//            ScoreboardManager.i = ScoreboardManager.i - 1.0D;
//          }
//          double DoubletimeMinutes = ScoreboardManager.i / 60.0D;
//          ScoreboardManager.timeMinutes = String.valueOf((int)DoubletimeMinutes);
//          double i2 = DoubletimeMinutes - Double.valueOf(ScoreboardManager.timeMinutes).doubleValue();
//          double b = i2 * 60.0D;
//          int sek = (int)Math.round(b);
//          ScoreboardManager.timeSek = String.valueOf(sek);
//          if (sek < 10)
//            ScoreboardManager.timeSek = "0" + ScoreboardManager.timeSek;
//        }
//      }).runTaskTimer((Plugin)Main.inst(), 0L, 20L);
//  }
  
  public void ScoreboardUpdater() {
    (new BukkitRunnable() {
        public void run() {
          for (Player player : Bukkit.getOnlinePlayers()) {
            setScoreboard(player);
          }
//          for (final Scoreboard board : boards.keySet()) {
//            final Player p = boards.get(board);
//            final String uuid = String.valueOf(p.getUniqueId());
//            final String killsSuffix = Config.messages.getString("scoreboard.kills.suffix").replaceAll("&", "§").replaceAll("%KILLS%", String.valueOf(Main.inst().stats.getKills(uuid)));
//            final String onlineSuffix = Config.messages.getString("scoreboard.online.suffix").replaceAll("&", "§").replaceAll("%ONLINEPLAYERS%", String.valueOf(Bukkit.getOnlinePlayers().size())).replaceAll("%MAXPLAYERS%", String.valueOf(Bukkit.getMaxPlayers()));
//            final String mapSuffix = Config.messages.getString("scoreboard.map.suffix").replaceAll("&", "§").replaceAll("%MAP%", Locations.getCurrentMap());
//            final String teamingSuffix = Config.messages.getString("scoreboard.teaming.suffix").replaceAll("&", "§").replaceAll("%STATE%", Config.getTeaming());
//            //final String mapchangeSuffix = Config.messages.getString("scoreboard.mapchange.suffix").replaceAll("&", "§").replaceAll("%MINUTES%", timeMinutes).replaceAll("%SECONDS%", timeSek);
//            board.getTeam("kills").setSuffix(killsSuffix);
//            board.getTeam("online").setSuffix(onlineSuffix);
//            board.getTeam("map").setSuffix(mapSuffix);
//            board.getTeam("teaming").setSuffix(teamingSuffix);
//            //board.getTeam("mapchange").setSuffix(mapchangeSuffix);
//          }
        }
      }).runTaskTimer((Plugin)Main.inst(), 0L, 20L);
  }
}
