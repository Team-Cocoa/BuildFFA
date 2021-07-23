package kr.teamcocoa.buildffa.commands;

import kr.teamcocoa.buildffa.main.Main;
import kr.teamcocoa.buildffa.utils.Config;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Teaming implements CommandExecutor {
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (sender instanceof Player) {
      Player p = (Player)sender;
      if (p.hasPermission(Config.permissions.getString("teaming"))) {
        if(Main.teaming) {
          Main.teaming = false;
          for(Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a[&dTeamCocoa&a] &7Teaming is &4&lPROHIBITED &7from now on!"));
          }
        }
        else {
          Main.teaming = true;
          for(Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a[&dTeamCocoa&a] &7Teaming is &a&lALLOWED &7from now on!"));
          }
        }
      } else {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a[&dTeamCocoa&a] &7This command does not exist or is deactivated."));
      } 
    } 
    return false;
  }
}
