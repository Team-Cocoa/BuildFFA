package kr.teamcocoa.buildffa.commands;

import kr.teamcocoa.buildffa.main.Main;
import kr.teamcocoa.buildffa.utils.Config;
import java.io.IOException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetStartmap implements CommandExecutor {
  public static String Mapname;
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (sender instanceof Player) {
      Player p = (Player)sender;
      if (p.hasPermission(Config.permissions.getString("setstartmap"))) {
        if (args.length == 1) {
          Mapname = args[0];
          if (Config.locations.get(Mapname) != null) {
            Config.config.set("startmap", Mapname);
            p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("setstartmap.01").replaceAll("%MAP%", Mapname).replaceAll("&", "§"));
            try {
              Config.config.save(Config.configFile);
            } catch (IOException e) {
              e.printStackTrace();
            } 
          } else {
            p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("setstartmap.02").replaceAll("%MAP%", Mapname).replaceAll("&", "§"));
          } 
        } else {
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("setstartmap.03").replaceAll("&", "§"));
        } 
      } else {
        p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nopermission").replaceAll("&", "§"));
      } 
    } 
    return false;
  }
}
