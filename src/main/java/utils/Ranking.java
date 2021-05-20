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
    ResultSet rs = MYSQL.getResult("SELECT UUID FROM Stats ORDER BY Kills DESC LIMIT 3");
    int i = 0;
    try {
      while (rs.next()) {
        i++;
        rank.put(Integer.valueOf(i), rs.getString("UUID"));
      } 
    } catch (SQLException e) {
      e.printStackTrace();
    } 
    Location loc1 = Locations.getStatsSignLocation(1);
    Location loc2 = Locations.getStatsSignLocation(2);
    Location loc3 = Locations.getStatsSignLocation(3);
    if (loc1 != null) {
      String name;
      int id = 1;
      if (rank.get(Integer.valueOf(id)) != null) {
        name = Bukkit.getOfflinePlayer(UUID.fromString(rank.get(Integer.valueOf(id)))).getName();
      } else {
        name = "???";
      } 
      Location loc = Locations.getStatsSignLocation(id);
      if (loc.getBlock().getState() instanceof Sign) {
        int kills;
        BlockState blockState = loc.getBlock().getState();
        Sign sign = (Sign)blockState;
        if (rank.get(Integer.valueOf(id)) != null) {
          kills = Stats.getKills(rank.get(Integer.valueOf(id))).intValue();
        } else {
          kills = 0;
        } 
        sign.setLine(0, "§6Platz #" + id);
        sign.setLine(1, "§0" + name);
        sign.setLine(2, "");
        sign.setLine(3, "§4" + String.valueOf(kills) + " Kills");
        sign.update();
        if (rank.get(Integer.valueOf(id)) != null) {
          Location skullLoc = loc;
          skullLoc.setY(loc.getY() + 1.0D);
          Block skullBlock = Bukkit.getWorld(loc.getWorld().getName()).getBlockAt(skullLoc);
          skullBlock.setType(Material.SKULL);
          Skull skullState = (Skull)skullBlock.getState();
          MaterialData skullData = skullState.getData();
          org.bukkit.material.Skull skull = (org.bukkit.material.Skull)skullData;
          if (Config.config.getString("statswalldirection").equals("EAST"))
            skull.setFacingDirection(BlockFace.WEST);
          if (Config.config.getString("statswalldirection").equals("SOUTH"))
            skull.setFacingDirection(BlockFace.SOUTH); 
          if (Config.config.getString("statswalldirection").equals("NORTH"))
            skull.setFacingDirection(BlockFace.NORTH); 
          if (Config.config.getString("statswalldirection").equals("WEST"))
            skull.setFacingDirection(BlockFace.EAST); 
          if (Config.config.getString("statswalldirection").equals("EAST/NORTH/SOUTH/WEST"))
            skull.setFacingDirection(BlockFace.WEST); 
          skullState.setSkullType(SkullType.PLAYER);
          skullState.setOwner(name);
          skullState.update();
        } 
      } 
    } 
    if (loc2 != null) {
      String name;
      int id = 2;
      if (rank.get(Integer.valueOf(id)) != null) {
        name = Bukkit.getOfflinePlayer(UUID.fromString(rank.get(Integer.valueOf(id)))).getName();
      } else {
        name = "???";
      } 
      Location loc = Locations.getStatsSignLocation(id);
      if (loc.getBlock().getState() instanceof Sign) {
        int kills;
        BlockState blockState = loc.getBlock().getState();
        Sign sign = (Sign)blockState;
        if (rank.get(Integer.valueOf(id)) != null) {
          kills = Stats.getKills(rank.get(Integer.valueOf(id))).intValue();
        } else {
          kills = 0;
        } 
        sign.setLine(0, "§6Platz #" + id);
        sign.setLine(1, "§0" + name);
        sign.setLine(2, "");
        sign.setLine(3, "§4" + String.valueOf(kills) + " Kills");
        sign.update();
        if (rank.get(Integer.valueOf(id)) != null) {
          Location skullLoc = loc;
          skullLoc.setY(loc.getY() + 1.0D);
          Block skullBlock = Bukkit.getWorld(loc.getWorld().getName()).getBlockAt(skullLoc);
          skullBlock.setType(Material.SKULL);
          Skull skullState = (Skull)skullBlock.getState();
          MaterialData skullData = skullState.getData();
          org.bukkit.material.Skull skull = (org.bukkit.material.Skull)skullData;
          if (Config.config.getString("statswalldirection").equals("EAST"))
            skull.setFacingDirection(BlockFace.WEST); 
          if (Config.config.getString("statswalldirection").equals("SOUTH"))
            skull.setFacingDirection(BlockFace.SOUTH); 
          if (Config.config.getString("statswalldirection").equals("NORTH"))
            skull.setFacingDirection(BlockFace.NORTH); 
          if (Config.config.getString("statswalldirection").equals("WEST"))
            skull.setFacingDirection(BlockFace.EAST); 
          if (Config.config.getString("statswalldirection").equals("EAST/NORTH/SOUTH/WEST"))
            skull.setFacingDirection(BlockFace.WEST); 
          skullState.setSkullType(SkullType.PLAYER);
          skullState.setOwner(name);
          skullState.update();
        } 
      } 
    } 
    if (loc3 != null) {
      String name;
      int id = 3;
      if (rank.get(Integer.valueOf(id)) != null) {
        name = Bukkit.getOfflinePlayer(UUID.fromString(rank.get(Integer.valueOf(id)))).getName();
      } else {
        name = "???";
      } 
      Location loc = Locations.getStatsSignLocation(id);
      if (loc.getBlock().getState() instanceof Sign) {
        int kills;
        BlockState blockState = loc.getBlock().getState();
        Sign sign = (Sign)blockState;
        if (rank.get(Integer.valueOf(id)) != null) {
          kills = Stats.getKills(rank.get(Integer.valueOf(id))).intValue();
        } else {
          kills = 0;
        } 
        sign.setLine(0, "§6Platz #" + id);
        sign.setLine(1, "§0" + name);
        sign.setLine(2, "");
        sign.setLine(3, "§4" + String.valueOf(kills) + " Kills");
        sign.update();
        if (rank.get(Integer.valueOf(id)) != null) {
          Location skullLoc = loc;
          skullLoc.setY(loc.getY() + 1.0D);
          Block skullBlock = Bukkit.getWorld(loc.getWorld().getName()).getBlockAt(skullLoc);
          skullBlock.setType(Material.SKULL);
          Skull skullState = (Skull)skullBlock.getState();
          MaterialData skullData = skullState.getData();
          org.bukkit.material.Skull skull = (org.bukkit.material.Skull)skullData;
          if (Config.config.getString("statswalldirection").equals("EAST"))
            skull.setFacingDirection(BlockFace.WEST); 
          if (Config.config.getString("statswalldirection").equals("SOUTH"))
            skull.setFacingDirection(BlockFace.SOUTH); 
          if (Config.config.getString("statswalldirection").equals("NORTH"))
            skull.setFacingDirection(BlockFace.NORTH); 
          if (Config.config.getString("statswalldirection").equals("WEST"))
            skull.setFacingDirection(BlockFace.EAST); 
          if (Config.config.getString("statswalldirection").equals("EAST/NORTH/SOUTH/WEST"))
            skull.setFacingDirection(BlockFace.WEST); 
          skullState.setSkullType(SkullType.PLAYER);
          skullState.setOwner(name);
          skullState.update();
        } 
      } 
    } 
  }
  
  public static void update() {
    Location loc1 = Locations.getStatsSignLocation(1);
    Location loc2 = Locations.getStatsSignLocation(2);
    Location loc3 = Locations.getStatsSignLocation(3);
    if (loc1 != null || loc2 != null || loc3 != null)
      (new BukkitRunnable() {
          public void run() {
            Ranking.set();
          }
        }).runTaskTimer((Plugin)Main.inst(), 0L, 100L); 
  }
}
