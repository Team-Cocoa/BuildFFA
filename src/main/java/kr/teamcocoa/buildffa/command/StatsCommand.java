package kr.teamcocoa.buildffa.command;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.inject.Inject;
import java.text.MessageFormat;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayerManager;
import kr.teamcocoa.buildffa.game.models.BuildFFAStats;
import kr.teamcocoa.buildffa.game.models.BuildFFAStatsManager;
import kr.teamcocoa.buildffa.game.prestige.PrestigeManager;
import kr.teamcocoa.buildffa.network.stats.StatsController;
import kr.teamcocoa.buildffa.translation.LangUtils;
import kr.teamcocoa.buildffa.translation.MessageEnum;
import kr.teamcocoa.core.network.controllers.mojang.ApiMojangController;
import kr.teamcocoa.core.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand implements CommandExecutor {

  private final BuildFFAPlayerManager buildFFAPlayerManager;

  private final BuildFFAStatsManager buildFFAStatsManager;

  private final StatsController statsController;

  private final PrestigeManager prestigeManager;

  private final ExecutorService executors =
      Executors.newFixedThreadPool(
          2, new ThreadFactoryBuilder().setNameFormat("bffa-stats-command-%d").build());

  @Inject
  public StatsCommand(
      BuildFFAPlayerManager buildFFAPlayerManager,
      BuildFFAStatsManager buildFFAStatsManager,
      StatsController statsController,
      PrestigeManager prestigeManager) {
    this.buildFFAPlayerManager = buildFFAPlayerManager;
    this.buildFFAStatsManager = buildFFAStatsManager;
    this.statsController = statsController;
    this.prestigeManager = prestigeManager;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

    if (!(sender instanceof Player) && args.length == 0) {
      return false;
    }

    if (args.length >= 1) {
      String playerName = args[0];

      Player searchPlayer;

      if ((searchPlayer = Bukkit.getPlayer(playerName)) != null) {
        BuildFFAPlayer buildFFAPlayer = buildFFAPlayerManager.getPlayer(searchPlayer);
        BuildFFAStats stats = buildFFAPlayer.getBuildFFAStats();
        sendMessage(sender, searchPlayer.getName(), stats);
        return true;
      }

      CompletableFuture<String> future = ApiMojangController.searchUUIDByName(playerName);
      future.thenAcceptAsync(
          s -> {
            UUID uuid = UUID.fromString(s);

            BuildFFAStats cachedStats = buildFFAStatsManager.getCache().readData(uuid);

            if (cachedStats != null) {
              sendMessage(sender, playerName, cachedStats);
              return;
            }

            cachedStats = new BuildFFAStats(uuid);
            try {
              boolean exist = statsController.loadStats(cachedStats);

              if (exist) {
                buildFFAStatsManager.getCache().createData(uuid, cachedStats);
                sendMessage(sender, playerName, cachedStats);
              } else {
                if (sender instanceof Player) {
                  Player player = ((Player) sender);
                  player.sendMessage(LangUtils.getMessage(player, MessageEnum.STATS_NOT_FOUND));
                } else {
                  sender.sendMessage(playerName + "not found.");
                }
              }
            } catch (IllegalStateException e) {
              e.printStackTrace();
              sender.sendMessage(
                  "An error has occurred while loading the stats. Contact to developer.");
            }
          },
          executors);

      return true;
    }

    Player player = ((Player) sender);
    BuildFFAStats stats = buildFFAStatsManager.getCache().readData(player.getUniqueId());
    sendMessage(player, player.getName(), stats);

    return true;
  }

  private void sendMessage(CommandSender sender, String name, BuildFFAStats stats) {
    if (sender instanceof Player) {
      Player player = ((Player) sender);
      String prestige = prestigeManager.getPrestigeName(stats.getKills());
      player.sendMessage(
          StringUtils.color(
              MessageFormat.format(
                  getListByString(LangUtils.getMessage(player, MessageEnum.STATS_MESSAGE)),
                  name,
                  stats.getKills(),
                  stats.getDeaths(),
                  stats.getBestKillStreaks(),
                  prestige)));
    } else {
      String prestige = prestigeManager.getPrestigeName(stats.getKills());
      sender.sendMessage(
          StringUtils.color(
              MessageFormat.format(
                  "name : {0} kills : {1} deaths : {2} bestKillStreaks : {3} prestige : {4}",
                  name,
                  stats.getKills(),
                  stats.getDeaths(),
                  stats.getBestKillStreaks(),
                  prestige)));
    }
  }

  private String getListByString(String string) {
    string = string.replace("[", "").replace("]", "").trim();
    StringBuilder sb = new StringBuilder();
    for (String s : string.split(",")) {
      sb.append(StringUtils.color(s) + "\n");
    }
    return sb.toString();
  }
}
