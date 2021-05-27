package utils;

import main.Main;
import org.bukkit.Bukkit;
import utils.MYSQL;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Stats {
  public static boolean playerExists(String uuid) {
    try {
      ResultSet rs = MYSQL.getResult("SELECT * FROM Stats WHERE UUID= '" + uuid + "'");
      if (rs.next())
        return (rs.getString("UUID") != null); 
      return false;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    } 
  }
  
  public static void createPlayer(String uuid) {
    if (Config.config.getBoolean("mysql.support")) {
      if (!playerExists(uuid))
        Bukkit.getScheduler().runTaskAsynchronously(Main.inst(), () -> {
          MYSQL.update("INSERT INTO Stats(UUID, KILLS, DEATHS) VALUES ('" + uuid + "', '0', '0');");
        });
    } else {
      if (Config.stats.get(String.valueOf(uuid) + "kills") == null) {
        Config.stats.set(String.valueOf(uuid) + "kills", Integer.valueOf(0));
        try {
          Config.stats.save(Config.statsFile);
        } catch (IOException e2) {
          e2.printStackTrace();
        }
      }
      if (Config.stats.get(String.valueOf(uuid) + "deaths") == null) {
        Config.stats.set(String.valueOf(uuid) + "deaths", Integer.valueOf(0));
        try {
          Config.stats.save(Config.statsFile);
        } catch (IOException e2) {
          e2.printStackTrace();
        }
      }
    }
  }
  
  public static Integer getKills(String uuid) {
    Integer i = Integer.valueOf(0);
    if (Config.config.getBoolean("mysql.support")) {
      if (playerExists(uuid)) {
        try {
          ResultSet rs = MYSQL.getResult("SELECT * FROM Stats WHERE UUID= '" + uuid + "'");
          if (!rs.next() || Integer.valueOf(rs.getInt("KILLS")) == null);
          i = Integer.valueOf(rs.getInt("KILLS"));
        } catch (SQLException e) {
          e.printStackTrace();
        } 
      } else {
        createPlayer(uuid);
        getKills(uuid);
      } 
    } else if (Config.stats.get(String.valueOf(uuid) + "kills") != null) {
      i = Integer.valueOf(Config.stats.getInt(String.valueOf(uuid) + "kills"));
    } else {
      createPlayer(uuid);
      getKills(uuid);
    } 
    return i;
  }
  
  public static Integer getDeaths(String uuid) {
    Integer i = Integer.valueOf(0);
    if (Config.config.getBoolean("mysql.support")) {
      if (playerExists(uuid)) {
        try {
          ResultSet rs = MYSQL.getResult("SELECT * FROM Stats WHERE UUID= '" + uuid + "'");
          if (!rs.next() || Integer.valueOf(rs.getInt("DEATHS")) == null);
          i = Integer.valueOf(rs.getInt("DEATHS"));
        } catch (SQLException e) {
          e.printStackTrace();
        } 
      } else {
        createPlayer(uuid);
        getDeaths(uuid);
      } 
    } else if (Config.stats.get(String.valueOf(uuid) + "deaths") != null) {
      i = Integer.valueOf(Config.stats.getInt(String.valueOf(uuid) + "deaths"));
    } else {
      createPlayer(uuid);
      getDeaths(uuid);
    } 
    return i;
  }
  
  public static void setKills(String uuid, Integer kills) {
    if (Config.config.getBoolean("mysql.support")) {
      if (playerExists(uuid)) {
        MYSQL.update("UPDATE Stats SET KILLS= '" + kills + "' WHERE UUID= '" + uuid + "';");
      } else {
        createPlayer(uuid);
        setKills(uuid, kills);
      } 
    } else if (Config.stats.get(String.valueOf(uuid) + "kills") != null) {
      Config.stats.set(String.valueOf(uuid) + "kills", kills);
      try {
        Config.stats.save(Config.statsFile);
      } catch (IOException e2) {
        e2.printStackTrace();
      } 
    } else {
      createPlayer(uuid);
      setKills(uuid, kills);
    } 
  }
  
  public static void setDeaths(String uuid, Integer deaths) {
    if (Config.config.getBoolean("mysql.support")) {
      if (playerExists(uuid)) {
        MYSQL.update("UPDATE Stats SET DEATHS= '" + deaths + "' WHERE UUID= '" + uuid + "';");
      } else {
        createPlayer(uuid);
        setDeaths(uuid, deaths);
      } 
    } else if (Config.stats.get(String.valueOf(uuid) + "deaths") != null) {
      Config.stats.set(String.valueOf(uuid) + "deaths", deaths);
      try {
        Config.stats.save(Config.statsFile);
      } catch (IOException e2) {
        e2.printStackTrace();
      } 
    } else {
      createPlayer(uuid);
      setDeaths(uuid, deaths);
    } 
  }
  
  public static void addKills(String uuid, Integer kills) {
    if (Config.config.getBoolean("mysql.support")) {
      if (playerExists(uuid)) {
        setKills(uuid, Integer.valueOf(getKills(uuid).intValue() + kills.intValue()));
      } else {
        createPlayer(uuid);
        addKills(uuid, kills);
      } 
    } else if (Config.stats.get(String.valueOf(uuid) + "kills") != null) {
      Config.stats.set(String.valueOf(uuid) + "kills", Integer.valueOf(getKills(uuid).intValue() + kills.intValue()));
      try {
        Config.stats.save(Config.statsFile);
      } catch (IOException e2) {
        e2.printStackTrace();
      } 
    } else {
      createPlayer(uuid);
      addKills(uuid, kills);
    } 
  }
  
  public static void addDeaths(String uuid, Integer deaths) {
    if (Config.config.getBoolean("mysql.support")) {
      if (playerExists(uuid)) {
        setDeaths(uuid, Integer.valueOf(getDeaths(uuid).intValue() + deaths.intValue()));
      } else {
        createPlayer(uuid);
        addDeaths(uuid, deaths);
      } 
    } else if (Config.stats.get(String.valueOf(uuid) + "deaths") != null) {
      Config.stats.set(String.valueOf(uuid) + "deaths", Integer.valueOf(getDeaths(uuid).intValue() + deaths.intValue()));
      try {
        Config.stats.save(Config.statsFile);
      } catch (IOException e2) {
        e2.printStackTrace();
      } 
    } else {
      createPlayer(uuid);
      addDeaths(uuid, deaths);
    } 
  }
  
  public static void removeKills(String uuid, Integer kills) {
    if (Config.config.getBoolean("mysql.support")) {
      if (playerExists(uuid)) {
        setKills(uuid, Integer.valueOf(getKills(uuid).intValue() - kills.intValue()));
      } else {
        createPlayer(uuid);
        removeKills(uuid, kills);
      } 
    } else if (Config.stats.get(String.valueOf(uuid) + "kills") != null) {
      Config.stats.set(String.valueOf(uuid) + "kills", Integer.valueOf(getKills(uuid).intValue() - kills.intValue()));
      try {
        Config.stats.save(Config.statsFile);
      } catch (IOException e2) {
        e2.printStackTrace();
      } 
    } else {
      createPlayer(uuid);
      removeKills(uuid, kills);
    } 
  }
  
  public static void removeDeaths(String uuid, Integer deaths) {
    if (Config.config.getBoolean("mysql.support")) {
      if (playerExists(uuid)) {
        setDeaths(uuid, Integer.valueOf(getDeaths(uuid).intValue() - deaths.intValue()));
      } else {
        createPlayer(uuid);
        removeDeaths(uuid, deaths);
      } 
    } else if (Config.stats.get(String.valueOf(uuid) + "deaths") != null) {
      Config.stats.set(String.valueOf(uuid) + "deaths", Integer.valueOf(getDeaths(uuid).intValue() - deaths.intValue()));
      try {
        Config.stats.save(Config.statsFile);
      } catch (IOException e2) {
        e2.printStackTrace();
      } 
    } else {
      createPlayer(uuid);
      removeDeaths(uuid, deaths);
    } 
  }
}
