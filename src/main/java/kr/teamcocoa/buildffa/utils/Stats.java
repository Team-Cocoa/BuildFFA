package kr.teamcocoa.buildffa.utils;

import kr.teamcocoa.buildffa.kit.BffaPlayer;
import kr.teamcocoa.buildffa.main.Main;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Stats {
  public boolean playerExists(String uuid) {
    try(ResultSet rs = Main.inst().mysql.getResult("SELECT `UUID` FROM Stats WHERE UUID= '" + uuid + "'")) {
      if (rs.next()){
        boolean returnBoolean = rs.getString("UUID") != null;
        return returnBoolean;
      }
      return false;
    }
    catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }
  
  public void createPlayer(String uuid) {
      if (!playerExists(uuid)) {
        Main.inst().mysql.update("INSERT INTO Stats(UUID, KILLS, DEATHS) VALUES ('" + uuid + "', '0', '0');");
        Main.inst().mysql.update("INSERT INTO `inventory`(uuid) VALUES(\"" + uuid + "\");");
      }

  }

  public Integer getMaxKillStreak(String uuid) {
      Integer i = Integer.valueOf(0);
      if (playerExists(uuid)) {
          try(ResultSet rs = Main.inst().mysql.getResult("SELECT `max_killstreak` FROM Stats WHERE UUID= '" + uuid + "'")) {
              if (rs.next());
              i = Integer.valueOf(rs.getInt("max_killstreak"));
          }
          catch (SQLException e) {
              e.printStackTrace();
          }
      }
      else {
          createPlayer(uuid);
          getKills(uuid);
      }
      return i;
  }

  public void setMaxKillStreak(String uuid, int killStreak) {
      if(playerExists(uuid)) {
          Main.inst().mysql.update("UPDATE Stats SET `max_killstreak` = '" + killStreak + "' WHERE UUID= '" + uuid + "';");
      }
      else {
          createPlayer(uuid);
          setMaxKillStreak(uuid, killStreak);
      }
  }
  
  public Integer getKills(String uuid) {
      Integer i = Integer.valueOf(0);
      if (playerExists(uuid)) {
        try(ResultSet rs = Main.inst().mysql.getResult("SELECT `KILLS` FROM Stats WHERE UUID= '" + uuid + "'")) {
          if (rs.next()) {
              i = Integer.valueOf(rs.getInt("KILLS"));
          }
        }
        catch (SQLException e) {
          e.printStackTrace();
        }
      } else {
        createPlayer(uuid);
        getKills(uuid);
      }
    return i;
  }
  
  public Integer getDeaths(String uuid) {
    Integer i = Integer.valueOf(0);
      if (playerExists(uuid)) {
        try(ResultSet rs = Main.inst().mysql.getResult("SELECT `DEATHS` FROM Stats WHERE UUID= '" + uuid + "'")) {
          if (rs.next());
          i = Integer.valueOf(rs.getInt("DEATHS"));
        }
        catch (SQLException e) {
          e.printStackTrace();
        }
      } else {
        createPlayer(uuid);
        getDeaths(uuid);
      }
    return i;
  }
  
  public void setKills(String uuid, Integer kills) {
      if (playerExists(uuid)) {
        Main.inst().mysql.update("UPDATE Stats SET KILLS= '" + kills + "' WHERE UUID= '" + uuid + "';");
      } else {
        createPlayer(uuid);
        setKills(uuid, kills);
      }
  }
  
  public void setDeaths(String uuid, Integer deaths) {
      if (playerExists(uuid)) {
        Main.inst().mysql.update("UPDATE Stats SET DEATHS= '" + deaths + "' WHERE UUID= '" + uuid + "';");
      } else {
        createPlayer(uuid);
        setDeaths(uuid, deaths);
      } 

  }
  
  public void addKills(String uuid, Integer kills) {
      if (playerExists(uuid)) {
        setKills(uuid, Integer.valueOf(getKills(uuid).intValue() + kills.intValue()));
      } else {
        createPlayer(uuid);
        addKills(uuid, kills);
      } 

  }
  
  public void addDeaths(String uuid, Integer deaths) {
      if (playerExists(uuid)) {
        setDeaths(uuid, Integer.valueOf(getDeaths(uuid).intValue() + deaths.intValue()));
      } else {
        createPlayer(uuid);
        addDeaths(uuid, deaths);
      }
  }
  
  public void removeKills(String uuid, Integer kills) {
      if (playerExists(uuid)) {
        setKills(uuid, Integer.valueOf(getKills(uuid).intValue() - kills.intValue()));
      } else {
        createPlayer(uuid);
        removeKills(uuid, kills);
      }
  }
  
  public void removeDeaths(String uuid, Integer deaths) {
      if (playerExists(uuid)) {
        setDeaths(uuid, Integer.valueOf(getDeaths(uuid).intValue() - deaths.intValue()));
      } else {
        createPlayer(uuid);
        removeDeaths(uuid, deaths);
      }
  }

  public void updatePlayer(BffaPlayer bffaPlayer) {
      int kills = bffaPlayer.getKills();
      int deaths = bffaPlayer.getDeaths();
      int bestKills = bffaPlayer.getBestKillStreaks();
      Main.inst().mysql.update("UPDATE `stats` SET `KILLS` = '" + kills
              + "', `DEATHS` = '" + deaths
              + "', `max_killstreak` = '" +bestKills
              + "' WHERE `UUID` = '" + bffaPlayer.getPlayer().getUniqueId().toString()
              + "';"
      );
  }

  public void updateRanking() {
      Bukkit.getScheduler().runTaskTimerAsynchronously(Main.inst(),
              () -> Main.playerData.forEach(((player, bffaPlayer) -> updatePlayer(bffaPlayer))),
              0L, 6000L);
  }
}
