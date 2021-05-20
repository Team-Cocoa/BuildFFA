package commands;

import main.Main;
import utils.Config;
import utils.Locations;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawn implements CommandExecutor {
  public static String Mapname;
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (sender instanceof Player) {
      Player p = (Player)sender;
      if (p.hasPermission(Config.permissions.getString("setspawn"))) {
        if (args.length == 1) {
          Mapname = args[0];
          World world = p.getWorld();
          double x = p.getLocation().getX();
          double y = p.getLocation().getY();
          double z = p.getLocation().getZ();
          float pitch = p.getLocation().getPitch();
          float yaw = p.getLocation().getYaw();
          Locations.setSpawnLocation(world, x, y, z, pitch, yaw, Mapname);
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("setspawn.01").replaceAll("%MAP%", Mapname).replaceAll("&", "§"));
        } else {
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("setspawn.02").replaceAll("&", "§"));
        } 
      } else {
        p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nopermission").replaceAll("&", "§"));
      } 
    } 
    return false;
  }
}
