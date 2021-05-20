package commands;

import main.Main;
import utils.Config;
import java.io.IOException;
import java.text.DecimalFormat;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Stats implements CommandExecutor {
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (sender instanceof Player) {
      Player p = (Player)sender;
      if (Config.config.getBoolean("stats")) {
        if (args.length != 0) {
          String str;
          switch ((str = args[0]).hashCode()) {
            case 1985911071:
              if (!str.equals("setsign"))
                break; 
              if (p.hasPermission(Config.permissions.getString("stats.setup"))) {
                if (args.length == 2) {
                  if (args[1].equals("1") || args[1].equals("2") || args[1].equals("3")) {
                    World world = p.getLocation().getWorld();
                    int x = (int)p.getLocation().getX();
                    int y = (int)p.getLocation().getY();
                    int z = (int)p.getLocation().getZ();
                    Config.locations.set("stats.sign." + args[1] + ".world", world.getName());
                    Config.locations.set("stats.sign." + args[1] + ".x", Integer.valueOf(x));
                    Config.locations.set("stats.sign." + args[1] + ".y", Integer.valueOf(y));
                    Config.locations.set("stats.sign." + args[1] + ".z", Integer.valueOf(z));
                    try {
                      Config.locations.save(Config.locationsFile);
                    } catch (IOException e) {
                      e.printStackTrace();
                    } 
                    p.sendMessage(String.valueOf(Main.getPrefix()) + "§7Sign für §6Platz #" + args[1] + " §7gesetzt.");
                  } else {
                    p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.setup.01").replaceAll("&", "§"));
                  } 
                } else {
                  p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.setup.01").replaceAll("&", "§"));
                } 
              } else {
                p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nopermission").replaceAll("&", "§"));
              } 
              return false;
          } 
          if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
              String uuid = String.valueOf(target.getUniqueId());
              String KD = String.valueOf(utils.Stats.getKills(uuid));
              String KDString = String.valueOf(KD);
              if (utils.Stats.getKills(uuid).intValue() != 0 && utils.Stats.getDeaths(uuid).intValue() != 0) {
                double kills = utils.Stats.getKills(uuid).intValue();
                double deaths = utils.Stats.getDeaths(uuid).intValue();
                double killsdeaths = kills / deaths;
                KD = (new DecimalFormat("#0.00")).format(killsdeaths);
                KDString = KD;
              } 
              String KillsString = String.valueOf(utils.Stats.getKills(uuid));
              String DeathsString = String.valueOf(utils.Stats.getDeaths(uuid));
              p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.01").replaceAll("&", "§"));
              p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.02").replaceAll("&", "§").replaceAll("%PLAYER%", target.getName()));
              p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.03").replaceAll("&", "§").replaceAll("%KILLS%", KillsString));
              p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.04").replaceAll("&", "§").replaceAll("%DEATHS%", DeathsString));
              p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.05").replaceAll("&", "§").replaceAll("%K/D%", KDString));
              p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.06").replaceAll("&", "§"));
            } else {
              OfflinePlayer offtarget = Bukkit.getOfflinePlayer(args[0]);
              if (offtarget != null)
                if ((offtarget.isOnline() | offtarget.hasPlayedBefore()) != true) {
                  String uuid = String.valueOf(offtarget.getUniqueId());
                  String KD = String.valueOf(utils.Stats.getKills(uuid));
                  String KDString = String.valueOf(KD);
                  if (utils.Stats.getKills(uuid).intValue() != 0 && utils.Stats.getDeaths(uuid).intValue() != 0) {
                    double kills = utils.Stats.getKills(uuid).intValue();
                    double deaths = utils.Stats.getDeaths(uuid).intValue();
                    double killsdeaths = kills / deaths;
                    KD = (new DecimalFormat("#0.00")).format(killsdeaths);
                    KDString = KD;
                  } 
                  String KillsString = String.valueOf(utils.Stats.getKills(uuid));
                  String DeathsString = String.valueOf(utils.Stats.getDeaths(uuid));
                  p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.01").replaceAll("&", "§"));
                  p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.02").replaceAll("&", "§").replaceAll("%PLAYER%", offtarget.getName()));
                  p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.03").replaceAll("&", "§").replaceAll("%KILLS%", KillsString));
                  p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.04").replaceAll("&", "§").replaceAll("%DEATHS%", DeathsString));
                  p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.05").replaceAll("&", "§").replaceAll("%K/D%", KDString));
                  p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.06").replaceAll("&", "§"));
                } else {
                  p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.07").replaceAll("&", "§").replaceAll("%TARGET%", args[0]));
                  if (p.hasPermission(Config.permissions.getString("stats.setup")))
                    p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.setup.01").replaceAll("&", "§")); 
                }  
            } 
          } else {
            p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.08").replaceAll("&", "§"));
            if (p.hasPermission(Config.permissions.getString("stats.setup")))
              p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.setup.01").replaceAll("&", "§")); 
          } 
        } else {
          String uuid = String.valueOf(p.getUniqueId());
          String KD = String.valueOf(utils.Stats.getKills(uuid));
          String KDString = String.valueOf(KD);
          if (utils.Stats.getKills(uuid).intValue() != 0 && utils.Stats.getDeaths(uuid).intValue() != 0) {
            double kills = utils.Stats.getKills(uuid).intValue();
            double deaths = utils.Stats.getDeaths(uuid).intValue();
            double killsdeaths = kills / deaths;
            KD = (new DecimalFormat("#0.00")).format(killsdeaths);
            KDString = KD;
          } 
          String KillsString = String.valueOf(utils.Stats.getKills(uuid));
          String DeathsString = String.valueOf(utils.Stats.getDeaths(uuid));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.01").replaceAll("&", "§"));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.02").replaceAll("&", "§").replaceAll("%PLAYER%", p.getName()));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.03").replaceAll("&", "§").replaceAll("%KILLS%", KillsString));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.04").replaceAll("&", "§").replaceAll("%DEATHS%", DeathsString));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.05").replaceAll("&", "§").replaceAll("%K/D%", KDString));
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.06").replaceAll("&", "§"));
          if (p.hasPermission(Config.permissions.getString("stats.setup")))
            p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.setup.01").replaceAll("&", "§")); 
        } 
      } else {
        p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.deactivated").replaceAll("&", "§"));
      } 
    } 
    return false;
  }
}
