package utils;

import main.Main;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Ranking {
  private static HashMap<Integer, String> rank = new HashMap<>();
  
  public static void set() {
      (new BukkitRunnable(){
        @Override
        public void run() {
          try {
            ResultSet rs = MYSQL.getResult("SELECT UUID FROM Stats ORDER BY Kills DESC LIMIT 3");
            int i = 0;
            while (rs.next()) {
              i++;
              rank.put(Integer.valueOf(i), rs.getString("UUID"));
            }
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }).runTaskAsynchronously(Main.inst());
  }
  
  public static void update() {
//    Location loc1 = Locations.getStatsSignLocation(1);
//    Location loc2 = Locations.getStatsSignLocation(2);
//    Location loc3 = Locations.getStatsSignLocation(3);
//    if (loc1 != null || loc2 != null || loc3 != null)
//      (new BukkitRunnable() {
//          public void run() {
//            Ranking.set();
//          }
//        }).runTaskTimer((Plugin)Main.inst(), 0L, 100L);
  }
}
