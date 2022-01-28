package kr.teamcocoa.buildffa.utils;

import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MYSQL {
  public static String host = Config.config.getString("mysql.host");
  
  public static String port = Config.config.getString("mysql.port");
  
  public static String database = Config.config.getString("mysql.database");
  
  public static String username = Config.config.getString("mysql.username");
  
  public static String password = Config.config.getString("mysql.password");
  
  public Connection con;
  
  public void connect() {
    if (!isConnected())
      try {
        con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
        System.out.println("| [BuildFFA] Successfully connected to MYSQL!                 |");
      } catch (SQLException e) {
        System.out.println("| [BuildFFA] Failed to connect to MYSQL server.               |");
      }  
  }
  
  public void disconnect() {
    if (isConnected())
      try {
        con.close();
        System.out.println("| [BuildFFA] MYSQL               Disconnected.                |");
      } catch (SQLException e) {
        e.printStackTrace();
      }  
  }
  
  public boolean isConnected() {
    if (con == null)
      return false; 
    return true;
  }
  
  public void update(String qry) {
    try (PreparedStatement ps = con.prepareStatement(qry)) {
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  
  public ResultSet getResult(String qry) {
    PreparedStatement ps;
    try {
      ps = con.prepareStatement(qry);
      return ps.executeQuery();
    }
    catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }
}
