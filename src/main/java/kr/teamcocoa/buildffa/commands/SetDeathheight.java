package kr.teamcocoa.buildffa.commands;

import kr.teamcocoa.buildffa.main.Main;
import kr.teamcocoa.buildffa.utils.Config;
import java.io.IOException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetDeathheight implements CommandExecutor {
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (sender instanceof Player) {
      Player p = (Player)sender;
      if (p.hasPermission(Config.permissions.getString("setdeathheight"))) {
        if (args.length == 1) {
          String Mapname = args[0];
          double deathhigh = p.getLocation().getY();
          String high = String.valueOf(deathhigh);
          if (Config.locations.get(Mapname) != null) {
            Config.locations.set(String.valueOf(Mapname) + ".deathheight", Double.valueOf(deathhigh));
            p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("setdeathheight.01").replaceAll("%TODESHÖHE%", high).replaceAll("%MAP%", Mapname).replaceAll("&", "§"));
            try {
              Config.locations.save(Config.locationsFile);
            } catch (IOException e) {
              e.printStackTrace();
            } 
          } else {
            p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("setdeathheight.02").replaceAll("%MAP%", Mapname).replaceAll("&", "§"));
          } 
        } else {
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("setdeathheight.03").replaceAll("&", "§"));
        } 
      } else {
        p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nopermission").replaceAll("&", "§"));
      } 
    } 
    return false;
  }
}
