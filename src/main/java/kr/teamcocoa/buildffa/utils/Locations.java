package kr.teamcocoa.buildffa.utils;

import kr.teamcocoa.buildffa.kit.BffaPlayer;
import kr.teamcocoa.buildffa.main.Main;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Locations {
  public static String CurrentMapname;
  
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
    CurrentMapname = Mapname;
  }
  
  public static String getCurrentMap() {
    if (CurrentMapname != null) {
      switch(CurrentMapname) {
        case "NEWVERSION" :
          return "NewVision";
        case "RUGIA" :
          return "Architecture";
        case "CWBW" :
          return "CwBw";
        default:
          return "Unknown Map";
      }
    }
    return "Unknown Map";
  }

  public static String getMapNameByInt(int i) {
    String mapName;
    switch(i) {
      case 2:
        mapName = "NEWVERSION";
        break;
      case 3:
        mapName = "RUGIA";
        break;
      default:
        mapName = "CWBW";
        break;
    }
    return mapName;
  }
  
  public static void MapChange(int i) {
    String mapName;
    switch(i) {
      case 2:
        mapName = "NEWVERSION";
        break;
      case 3:
        mapName = "RUGIA";
        break;
      default:
        mapName = "CWBW";
        break;
    }
    CurrentMapname = mapName;
    Location spawn = getSpawnLocation(CurrentMapname);
    Main.worldData.removeBlocks();
    setCurrentMap(mapName);
    for(Player player : Bukkit.getOnlinePlayers()) {
      player.teleport(spawn);
      BffaPlayer bffaPlayer = Main.playerData.get(player);
      bffaPlayer.setJoinInventory();
      bffaPlayer.setInGame(false);

    }
  }
}
