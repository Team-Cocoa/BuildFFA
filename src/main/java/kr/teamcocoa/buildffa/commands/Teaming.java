package kr.teamcocoa.buildffa.commands;

import kr.teamcocoa.buildffa.main.Main;
import kr.teamcocoa.buildffa.utils.Config;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Teaming implements CommandExecutor {
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (sender instanceof Player) {
      Player p = (Player)sender;
      if (p.hasPermission(Config.permissions.getString("teaming"))) {
        if (args.length == 0) {
          if (Config.config.getBoolean("teaming")) {
            Config.config.set("teaming", Boolean.valueOf(false));
            try {
              Config.config.save(Config.configFile);
            } catch (IOException e2) {
              e2.printStackTrace();
            } 
            p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("teaming.01").replaceAll("&", "§"));
          } else {
            Config.config.set("teaming", Boolean.valueOf(true));
            try {
              Config.config.save(Config.configFile);
            } catch (IOException e2) {
              e2.printStackTrace();
            } 
            Bukkit.broadcastMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("teaming.02").replaceAll("&", "§"));
          } 
        } else {
          Bukkit.broadcastMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("teaming.03").replaceAll("&", "§"));
        } 
      } else {
        p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nopermission").replaceAll("&", "§"));
      } 
    } 
    return false;
  }
}
