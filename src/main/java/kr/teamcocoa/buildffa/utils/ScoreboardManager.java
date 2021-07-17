package kr.teamcocoa.buildffa.utils;

import kr.teamcocoa.buildffa.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ScoreboardManager implements Listener {
  
  private String timeSek = "00";
  
  private String timeMinutes = "0";
  
  private double i = Config.config.getInt("mapchangedelaysek");
  
  public Boolean Mapchangeupdater = Boolean.valueOf(false);
  
  public void setScoreboard(Player p) {
    Scoreboard sb;
    String uuid = String.valueOf(p.getUniqueId());
    String killsSuffix = Config.messages.getString("scoreboard.kills.suffix").replaceAll("&", "§").replaceAll("%KILLS%", String.valueOf(Main.inst().stats.getKills(uuid)));
    String killsPrefix = Config.messages.getString("scoreboard.kills.prefix").replaceAll("&", "§");
    String killsScore = Config.messages.getString("scoreboard.kills.score").replaceAll("&", "§");
    String killsEntry = Config.messages.getString("scoreboard.kills.entry").replaceAll("&", "§");
    String onlineSuffix = Config.messages.getString("scoreboard.online.suffix").replaceAll("&", "§").replaceAll("%ONLINEPLAYERS%", String.valueOf(Bukkit.getOnlinePlayers().size())).replaceAll("%MAXPLAYERS%", String.valueOf(Bukkit.getMaxPlayers()));
    String onlinePrefix = Config.messages.getString("scoreboard.online.prefix").replaceAll("&", "§");
    String onlineScore = Config.messages.getString("scoreboard.online.score").replaceAll("&", "§");
    String onlineEntry = Config.messages.getString("scoreboard.online.entry").replaceAll("&", "§");
    String mapSuffix = Config.messages.getString("scoreboard.map.suffix").replaceAll("&", "§").replaceAll("%MAP%", Locations.getCurrentMap());
    String mapPrefix = Config.messages.getString("scoreboard.map.prefix").replaceAll("&", "§");
    String mapScore = Config.messages.getString("scoreboard.map.score").replaceAll("&", "§");
    String mapEntry = Config.messages.getString("scoreboard.map.entry").replaceAll("&", "§");
    String teamingSuffix = Config.messages.getString("scoreboard.teaming.suffix").replaceAll("&", "§").replaceAll("%STATE%", Config.getTeaming());
    String teamingPrefix = Config.messages.getString("scoreboard.teaming.prefix").replaceAll("&", "§");
    String teamingScore = Config.messages.getString("scoreboard.teaming.score").replaceAll("&", "§");
    String teamingEntry = Config.messages.getString("scoreboard.teaming.entry").replaceAll("&", "§");
    sb = Bukkit.getScoreboardManager().getNewScoreboard();
    Objective obj = sb.getObjective("aaa");
    if (obj == null)
      obj = sb.registerNewObjective("aaa", "bbb"); 
    obj.setDisplaySlot(DisplaySlot.SIDEBAR);
    obj.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&dBuildFFA"));
    Team kills = sb.registerNewTeam("kills");
    kills.setSuffix(killsSuffix);
    kills.setPrefix(killsPrefix);
    kills.addEntry(String.valueOf(ChatColor.AQUA.toString()) + killsEntry);
    Team online = sb.registerNewTeam("online");
    online.setPrefix(onlinePrefix);
    online.setSuffix(onlineSuffix);
    online.addEntry(String.valueOf(ChatColor.BLACK.toString()) + onlineEntry);
    Team map = sb.registerNewTeam("map");
    map.setPrefix(mapPrefix);
    map.setSuffix(mapSuffix);
    map.addEntry(String.valueOf(ChatColor.BLUE.toString()) + mapEntry);
//    Team mapchange = sb.registerNewTeam("mapchange");
//    mapchange.setPrefix(mapchangePrefix);
//    mapchange.setSuffix(mapchangeSuffix);
//    mapchange.addEntry(String.valueOf(ChatColor.DARK_GREEN.toString()) + mapchangeEntry);
    Team teaming = sb.registerNewTeam("teaming");
    teaming.setPrefix(teamingPrefix);
    teaming.setSuffix(teamingSuffix);
    teaming.addEntry(String.valueOf(ChatColor.DARK_GRAY.toString()) + teamingEntry);
    obj.getScore(mapScore).setScore(7);
    obj.getScore(ChatColor.DARK_BLUE.toString()).setScore(6);
    obj.getScore(killsScore).setScore(5);
    obj.getScore(String.valueOf(ChatColor.AQUA.toString()) + killsEntry).setScore(4);
    obj.getScore(ChatColor.DARK_AQUA.toString()).setScore(3);
    obj.getScore(teamingScore).setScore(2);
    obj.getScore(String.valueOf(ChatColor.DARK_GRAY.toString()) + teamingEntry).setScore(1);
    p.setScoreboard(sb);
    sb = null;
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
        }
      }).runTaskTimer((Plugin)Main.inst(), 0L, 20L);
  }
}
