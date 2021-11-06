package kr.teamcocoa.buildffa.commands;

import kr.teamcocoa.buildffa.main.Main;
import kr.teamcocoa.buildffa.utils.Config;

import kr.teamcocoa.buildffa.utils.StringUtils;
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
            player.sendMessage(StringUtils.color("&a[&dBuildFFA&a] &aThe Build mode has been activated."));
            player.setGameMode(GameMode.CREATIVE);
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            Main.playerData.get(player).setInGame(false);
            Main.playerData.get(player).setBuild(true);
          } else {
            player.sendMessage(StringUtils.color("&a[&dBuildFFA&a] &cThe Build mode has been deactivated."));
            Main.playerData.get(player).setJoinInventory();
            player.setGameMode(GameMode.SURVIVAL);
            Main.playerData.get(player).setBuild(false);
          }
        }
      }
      else {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a[&dTeamCocoa&a] &7This command does not exist or is deactivated."));
      } 
    } 
    return false;
  }
}
