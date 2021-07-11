package kr.teamcocoa.buildffa.utils;

import kr.teamcocoa.buildffa.main.Main;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import org.bukkit.scheduler.BukkitRunnable;

public class Ranking {
  private HashMap<Integer, String> rank = new HashMap<>();
  public void set() {
      (new BukkitRunnable(){
        @Override
        public void run() {
          ResultSet rs = null;
          try {
             rs = Main.inst().mysql.getResult("SELECT UUID FROM Stats ORDER BY Kills DESC LIMIT 3");
            int i = 0;
            while (rs.next()) {
              i++;
              rank.put(Integer.valueOf(i), rs.getString("UUID"));
            }
          } catch (SQLException e) {
            e.printStackTrace();
          }
          finally {
            try {
              if(rs != null) {
                rs.close();
              }
            }
            catch(SQLException e) {
              e.printStackTrace();
            }
          }
        }
      }).runTaskAsynchronously(Main.inst());
  }
}
