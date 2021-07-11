package kr.teamcocoa.buildffa.commands;

import kr.teamcocoa.buildffa.main.Main;
import kr.teamcocoa.buildffa.utils.Config;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Build implements CommandExecutor {
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (sender instanceof Player) {
      Player player = (Player)sender;
      if (player.hasPermission(Config.permissions.getString("build"))) {
        if (args.length == 0) {
          if (!Main.playerData.get(player).isBuild()) {
            player.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("build.01").replaceAll("&", "§"));
            player.setGameMode(GameMode.CREATIVE);
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            Main.playerData.get(player).setInGame(false);
            Main.playerData.get(player).setBuild(true);
          } else {
            player.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("build.02").replaceAll("&", "§"));
            Main.playerData.get(player).setJoinInventory();
            if (!Main.playerData.get(player).isInGame()) {
              Main.playerData.get(player).setInGame(true);
            }
            player.setGameMode(GameMode.SURVIVAL);
            Main.playerData.get(player).setBuild(false);
          }
        }
//        } else if (args.length == 1) {
//          Player target = Bukkit.getPlayer(args[0]);
//          if (target != null) {
//            if (!Main.playerData.get(player).isBuild()) {
//              player.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("build.03").replaceAll("%TARGET%", target.getName()).replaceAll("&", "§"));
//              target.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("build.04").replaceAll("%PLAYER%", player.getName()).replaceAll("&", "§"));
//              target.setGameMode(GameMode.CREATIVE);
//              target.getInventory().clear();
//              target.getInventory().setArmorContents(null);
//              Main.playerData.get(player).setBuild(true);
//            } else {
//              player.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("build.05").replaceAll("%TARGET%", target.getName()).replaceAll("&", "§"));
//              target.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("build.06").replaceAll("%PLAYER%", player.getName()).replaceAll("&", "§"));
//              Main.playerData.get(target).setJoinInventory();
//              if (Main.playerData.get(target).isInGame()){
//                Main.playerData.get(target).setInGame(false);
//              }
//              target.setGameMode(GameMode.SURVIVAL);
//              Main.playerData.get(player).setBuild(false);
//            }
//          } else {
//            player.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("build.07").replaceAll("%TARGET%", args[0]).replaceAll("&", "§"));
//          }
//        } else {
//          player.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("build.08").replaceAll("&", "§"));
//        }
      } else {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a[&dTeamCocoa&a] &7This command does not exist or is deactivated."));
      } 
    } 
    return false;
  }
}
