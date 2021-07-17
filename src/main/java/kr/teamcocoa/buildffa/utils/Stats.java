package kr.teamcocoa.buildffa.utils;

import kr.teamcocoa.buildffa.main.Main;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Stats {
  public boolean playerExists(String uuid) {
    ResultSet rs = null;
    try {
       rs = Main.inst().mysql.getResult("SELECT `UUID` FROM Stats WHERE UUID= '" + uuid + "'");
      if (rs.next()){
        boolean returnBoolean = rs.getString("UUID") != null;
        rs.close();
        return returnBoolean;
      }
      return false;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
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
  
  public void createPlayer(String uuid) {
      if (!playerExists(uuid)) {
        Main.inst().mysql.update("INSERT INTO Stats(UUID, KILLS, DEATHS) VALUES ('" + uuid + "', '0', '0');");
        Main.inst().mysql.update("INSERT INTO `kit_archer`(`uuid`) VALUES ('" + uuid + "')");
        Main.inst().mysql.update("INSERT INTO `kit_default`(`uuid`) VALUES ('" + uuid + "')");
        Main.inst().mysql.update("INSERT INTO `kit_fisher`(`uuid`) VALUES ('" + uuid + "')");
      }

  }
  
  public Integer getKills(String uuid) {
    Integer i = Integer.valueOf(0);
      if (playerExists(uuid)) {
          ResultSet rs = null;
        try {
           rs = Main.inst().mysql.getResult("SELECT `KILLS` FROM Stats WHERE UUID= '" + uuid + "'");
          if (!rs.next() || Integer.valueOf(rs.getInt("KILLS")) == null);
          i = Integer.valueOf(rs.getInt("KILLS"));
          rs.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
        finally {
            if(rs != null) {
                try {
                    rs.close();
                }
                catch(SQLException e) {
                    e.printStackTrace();
                }
            }
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
          ResultSet rs = null;
        try {
           rs = Main.inst().mysql.getResult("SELECT `DEATHS` FROM Stats WHERE UUID= '" + uuid + "'");
          if (!rs.next() || Integer.valueOf(rs.getInt("DEATHS")) == null);
          i = Integer.valueOf(rs.getInt("DEATHS"));
        } catch (SQLException e) {
          e.printStackTrace();
        }
        finally {
            if(rs != null) {
                try {
                    rs.close();
                }
                catch(SQLException e) {
                    e.printStackTrace();
                }
            }
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
}
