package kr.teamcocoa.buildffa.utils;

import kr.teamcocoa.buildffa.main.Main;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Locations {
  private static String CurrentMapname;
  
  public static Location getSpawnLocation(String Mapname) {
    if (Config.locations.getString(Mapname) != null) {
      World world = Bukkit.getWorld(Config.locations.getString(String.valueOf(Mapname) + ".spawn.world"));
      double x = Config.locations.getDouble(String.valueOf(Mapname) + ".spawn.x");
      double y = Config.locations.getDouble(String.valueOf(Mapname) + ".spawn.y");
      double z = Config.locations.getDouble(String.valueOf(Mapname) + ".spawn.z");
      String pitchString = Config.locations.getString(String.valueOf(Mapname) + ".spawn.pitch");
      String yawString = Config.locations.getString(String.valueOf(Mapname) + ".spawn.yaw");
      float pitch = Float.parseFloat(pitchString);
      float yaw = Float.parseFloat(yawString);
      Location loc = new Location(world, x, y, z, yaw, pitch);
      return loc;
    } 
    return null;
  }
  
  public static void setSpawnLocation(World world, double x, double y, double z, float pitch, float yaw, String Mapname) {
    if (Config.locations.getString("maps").equals("keine vorhanden")) {
      Config.locations.set("maps", null);
      try {
        Config.locations.save(Config.locationsFile);
      } catch (IOException e) {
        e.printStackTrace();
      } 
      Config.locations.set("maps.1", Mapname);
      setCurrentMap(Mapname);
    } else if (Config.locations.getString(String.valueOf(Mapname) + ".spawn") == null) {
      Boolean Mapfound = Boolean.valueOf(false);
      int i = 1;
      while (!Mapfound.booleanValue()) {
        if (Config.locations.get("maps." + (i + 1)) == null) {
          Config.locations.set("maps." + (i + 1), Mapname);
          Mapfound = Boolean.valueOf(true);
          try {
            Config.locations.save(Config.locationsFile);
          } catch (IOException e) {
            e.printStackTrace();
          } 
          continue;
        } 
        i++;
      } 
    } 
    Config.locations.set(String.valueOf(Mapname) + ".spawn.world", world.getName());
    Config.locations.set(String.valueOf(Mapname) + ".spawn.x", Double.valueOf(x));
    Config.locations.set(String.valueOf(Mapname) + ".spawn.y", Double.valueOf(y));
    Config.locations.set(String.valueOf(Mapname) + ".spawn.z", Double.valueOf(z));
    Config.locations.set(String.valueOf(Mapname) + ".spawn.pitch", Float.valueOf(pitch));
    Config.locations.set(String.valueOf(Mapname) + ".spawn.yaw", Float.valueOf(yaw));
    try {
      Config.locations.save(Config.locationsFile);
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }
  
  public static void setCurrentMap(String Mapname) {
    Config.locations.set("currentmap", Mapname);
    try {
      Config.locations.save(Config.locationsFile);
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }
  
  public static String getCurrentMap() {
    CurrentMapname = Config.locations.getString("currentmap");
    if (CurrentMapname != null)
      return CurrentMapname; 
    return "Keine Map";
  }
  
  public static void MapChange() {
    if (Config.locations.getString("maps").equals("keine vorhanden")) {
      Bukkit.broadcastMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("mapnotchanged").replaceAll("&", "§"));
      return;
    } 
    if (Bukkit.getOnlinePlayers().size() == 0) {
      Bukkit.broadcastMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("mapnotchanged").replaceAll("&", "§"));
      return;
    } 
    Boolean MapChanged = Boolean.valueOf(false);
    int i = 1;
    while (!MapChanged.booleanValue()) {
      if (Config.locations.getString("maps." + i).equals(getCurrentMap())) {
        if (Config.locations.getString("maps." + (i + 1)) != null) {
          String NewMapname = Config.locations.getString("maps." + (i + 1));
          Location loc = getSpawnLocation(NewMapname);
          for (Player all : Bukkit.getOnlinePlayers()) {
            if (all != null) {
              all.closeInventory();
              all.teleport(loc);
              all.getInventory().clear();
              all.getInventory().setArmorContents(null);
              if (Main.playerData.get(all).isInGame()){
                Main.playerData.get(all).setInGame(false);
              }
              Main.playerData.get(all).setJoinInventory();
            } 
          } 
          setCurrentMap(NewMapname);
          Bukkit.broadcastMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("mapchanged").replaceAll("&", "§").replaceAll("%NEWMAP%", NewMapname));
          MapChanged = Boolean.valueOf(true);
          continue;
        } 
        if (!Config.locations.getString("maps.1").equals(getCurrentMap())) {
          String NewMapname = Config.locations.getString("maps.1");
          Location loc = getSpawnLocation(NewMapname);
          for (Player all : Bukkit.getOnlinePlayers()) {
            if (all != null) {
              all.closeInventory();
              all.teleport(loc);
              all.getInventory().clear();
              all.getInventory().setArmorContents(null);
              if (Main.playerData.get(all).isInGame()){
                Main.playerData.get(all).setInGame(false);
              }
              Main.playerData.get(all).setJoinInventory();
            } 
            MapChanged = Boolean.valueOf(true);
            setCurrentMap(NewMapname);
            Bukkit.broadcastMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("mapchanged").replaceAll("&", "§").replaceAll("%NEWMAP%", NewMapname));
          } 
          continue;
        } 
        Bukkit.broadcastMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("mapnotchanged").replaceAll("&", "§"));
        MapChanged = Boolean.valueOf(true);
        continue;
      } 
      i++;
    } 
  }
  
  public static Location getStatsSignLocation(int ID) {
    if (Config.locations.getString("stats.sign." + ID) != null) {
      World world = Bukkit.getWorld(Config.locations.getString("stats.sign." + ID + ".world"));
      int x = (int)Config.locations.getDouble("stats.sign." + ID + ".x");
      int y = (int)Config.locations.getDouble("stats.sign." + ID + ".y");
      int z = (int)Config.locations.getDouble("stats.sign." + ID + ".z");
      Location loc = new Location(world, x, y, z);
      return loc;
    } 
    return null;
  }
}
