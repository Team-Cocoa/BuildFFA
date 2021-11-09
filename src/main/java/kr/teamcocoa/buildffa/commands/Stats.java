package kr.teamcocoa.buildffa.commands;

import kr.teamcocoa.buildffa.kit.BffaPlayer;
import kr.teamcocoa.buildffa.kit.NickedBffaPlayer;
import kr.teamcocoa.buildffa.main.Main;
import org.bukkit.scheduler.BukkitRunnable;
import kr.teamcocoa.buildffa.utils.Config;
import java.text.DecimalFormat;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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
          if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
              (new BukkitRunnable() {
                @Override
                public void run () {
                  BffaPlayer player = Main.playerData.get(target);
//                  Bukkit.getLogger().info(player.toString());
                  String name, kills, deaths, kd;
                  name = target.getName();
                  if(player.isNicked()) {
                    NickedBffaPlayer nickedBffaPlayer = player.getNickedBffaPlayer();
                    kills = String.valueOf(nickedBffaPlayer.getKills());
                    deaths = String.valueOf(nickedBffaPlayer.getDeaths());
                    kd = (new DecimalFormat("#0.00")).format((double) nickedBffaPlayer.getKills() / (double) nickedBffaPlayer.getDeaths());
                  }
                  else {
                    kills = String.valueOf(player.getKills());
                    deaths = String.valueOf(player.getDeaths());
                    kd = (new DecimalFormat("#0.00")).format((double) player.getKills() / (double) player.getDeaths());
                  }
                p.sendMessage(Main.getPrefix() + Config.messages.getString("stats.01").replaceAll("&", "§"));
                p.sendMessage(Main.getPrefix() + Config.messages.getString("stats.02").replaceAll("&", "§").replaceAll("%PLAYER%", name));
                p.sendMessage(Main.getPrefix() + Config.messages.getString("stats.03").replaceAll("&", "§").replaceAll("%KILLS%", kills));
                p.sendMessage(Main.getPrefix() + Config.messages.getString("stats.04").replaceAll("&", "§").replaceAll("%DEATHS%", deaths));
                p.sendMessage(Main.getPrefix() + Config.messages.getString("stats.05").replaceAll("&", "§").replaceAll("%K/D%", kd));
                p.sendMessage(Main.getPrefix() + Config.messages.getString("stats.06").replaceAll("&", "§"));
              }
              }).runTaskAsynchronously(Main.inst());
            } else {
              OfflinePlayer offtarget = Bukkit.getOfflinePlayer(args[0]);
              if (offtarget != null)
                if ((offtarget.isOnline() | offtarget.hasPlayedBefore()) != true) {
                  (new BukkitRunnable(){
                    @Override
                    public void run(){
                      String uuid = String.valueOf(offtarget.getUniqueId());
                      String KD = String.valueOf(Main.inst().stats.getKills(uuid));
                      String KDString = String.valueOf(KD);
                      if (Main.inst().stats.getKills(uuid).intValue() != 0 && Main.inst().stats.getDeaths(uuid).intValue() != 0) {
                        double kills = Main.inst().stats.getKills(uuid).intValue();
                        double deaths = Main.inst().stats.getDeaths(uuid).intValue();
                        double killsdeaths = kills / deaths;
                        KD = (new DecimalFormat("#0.00")).format(killsdeaths);
                        KDString = KD;
                      }
                      String KillsString = String.valueOf(Main.inst().stats.getKills(uuid));
                      String DeathsString = String.valueOf(Main.inst().stats.getDeaths(uuid));
                      p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.01").replaceAll("&", "§"));
                      p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.02").replaceAll("&", "§").replaceAll("%PLAYER%", offtarget.getName()));
                      p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.03").replaceAll("&", "§").replaceAll("%KILLS%", KillsString));
                      p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.04").replaceAll("&", "§").replaceAll("%DEATHS%", DeathsString));
                      p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.05").replaceAll("&", "§").replaceAll("%K/D%", KDString));
                      p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.06").replaceAll("&", "§"));
                    }
                  }).runTaskAsynchronously(Main.inst());
                } else {
                  p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.07").replaceAll("&", "§").replaceAll("%TARGET%", args[0]));
                }  
            } 
          } else {
            p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.08").replaceAll("&", "§"));
          } 
        } else {
          (new BukkitRunnable(){
            @Override
            public void run(){
              String uuid = String.valueOf(p.getUniqueId());
              String KD = String.valueOf(Main.inst().stats.getKills(uuid));
              String KDString = String.valueOf(KD);
              if (Main.inst().stats.getKills(uuid).intValue() != 0 && Main.inst().stats.getDeaths(uuid).intValue() != 0) {
                double kills = Main.inst().stats.getKills(uuid).intValue();
                double deaths = Main.inst().stats.getDeaths(uuid).intValue();
                double killsdeaths = kills / deaths;
                KD = (new DecimalFormat("#0.00")).format(killsdeaths);
                KDString = KD;
              }
              String KillsString = String.valueOf(Main.inst().stats.getKills(uuid));
              String DeathsString = String.valueOf(Main.inst().stats.getDeaths(uuid));
              p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.01").replaceAll("&", "§"));
              p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.02").replaceAll("&", "§").replaceAll("%PLAYER%", p.getName()));
              p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.03").replaceAll("&", "§").replaceAll("%KILLS%", KillsString));
              p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.04").replaceAll("&", "§").replaceAll("%DEATHS%", DeathsString));
              p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.05").replaceAll("&", "§").replaceAll("%K/D%", KDString));
              p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.06").replaceAll("&", "§"));
            }
          }).runTaskAsynchronously(Main.inst());
          if (p.hasPermission(Config.permissions.getString("stats.setup")))
            p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("stats.setup.01").replaceAll("&", "§")); 
        } 
      }
    } 
    return false;
  }
}
