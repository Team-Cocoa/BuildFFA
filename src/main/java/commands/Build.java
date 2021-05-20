package commands;

import listener.PlayerMoveListener;
import main.Main;
import utils.Config;
import utils.Inventory;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Build implements CommandExecutor {
  public static ArrayList<String> buildmode = new ArrayList<>();
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (sender instanceof Player) {
      Player player = (Player)sender;
      if (player.hasPermission(Config.permissions.getString("build"))) {
        if (args.length == 0) {
          if (!buildmode.contains(player.getName())) {
            player.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("build.01").replaceAll("&", "§"));
            player.setGameMode(GameMode.CREATIVE);
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            buildmode.add(player.getName());
          } else {
            player.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("build.02").replaceAll("&", "§"));
            Inventory.setJoinInventory(player);
            if (PlayerMoveListener.gotInventory.contains(player))
              PlayerMoveListener.gotInventory.remove(player); 
            player.setGameMode(GameMode.SURVIVAL);
            buildmode.remove(player.getName());
          } 
        } else if (args.length == 1) {
          Player target = Bukkit.getPlayer(args[0]);
          if (target != null) {
            if (!buildmode.contains(target.getName())) {
              player.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("build.03").replaceAll("%TARGET%", target.getName()).replaceAll("&", "§"));
              target.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("build.04").replaceAll("%PLAYER%", player.getName()).replaceAll("&", "§"));
              target.setGameMode(GameMode.CREATIVE);
              target.getInventory().clear();
              target.getInventory().setArmorContents(null);
              buildmode.add(target.getName());
            } else {
              player.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("build.05").replaceAll("%TARGET%", target.getName()).replaceAll("&", "§"));
              target.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("build.06").replaceAll("%PLAYER%", player.getName()).replaceAll("&", "§"));
              Inventory.setJoinInventory(target);
              if (PlayerMoveListener.gotInventory.contains(target))
                PlayerMoveListener.gotInventory.remove(target); 
              target.setGameMode(GameMode.SURVIVAL);
              buildmode.remove(target.getName());
            } 
          } else {
            player.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("build.07").replaceAll("%TARGET%", args[0]).replaceAll("&", "§"));
          } 
        } else {
          player.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("build.08").replaceAll("&", "§"));
        } 
      } else {
        player.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nopermission").replaceAll("&", "§"));
      } 
    } 
    return false;
  }
}
