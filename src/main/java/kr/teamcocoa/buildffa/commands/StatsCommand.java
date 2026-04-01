package kr.teamcocoa.buildffa.commands;

import kr.teamcocoa.buildffa.databases.StatsDatabase;
import kr.teamcocoa.buildffa.enums.MessageEnum;
import kr.teamcocoa.buildffa.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.models.BuildFFAPlayerManager;
import kr.teamcocoa.buildffa.models.BuildFFAStats;
import kr.teamcocoa.buildffa.models.BuildFFAStatsManager;
import kr.teamcocoa.buildffa.prestige.PrestigeManager;
import kr.teamcocoa.buildffa.utils.LangUtils;
import kr.teamcocoa.core.network.controllers.mojang.ApiMojangController;
import kr.teamcocoa.core.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class StatsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player) && args.length == 0) {
            return false;
        }

        if(args.length >= 1) {
            String playerName = args[0];

            Player searchPlayer;

            if((searchPlayer = Bukkit.getPlayer(playerName)) != null) {
                BuildFFAPlayer buildFFAPlayer = BuildFFAPlayerManager.getPlayer(searchPlayer);
                BuildFFAStats stats = buildFFAPlayer.getBuildFFAStats();
                sendMessage(sender, searchPlayer.getName(), stats);
                return true;
            }

            CompletableFuture<String> future = ApiMojangController.searchUUIDByName(playerName);
            future.thenAcceptAsync(s -> {
                UUID uuid = UUID.fromString(s);

                BuildFFAStats cachedStats = BuildFFAStatsManager.getCache().readData(uuid);

                if (cachedStats != null) {
                    sendMessage(sender, playerName, cachedStats);
                    return;
                }

                cachedStats = new BuildFFAStats(uuid);
                try {
                    boolean exist = StatsDatabase.loadStats(cachedStats);

                    if (exist) {
                        BuildFFAStatsManager.getCache().createData(uuid, cachedStats);
                        sendMessage(sender, playerName, cachedStats);
                    } else {
                        if (sender instanceof Player) {
                            Player player = ((Player) sender);
                            player.sendMessage(LangUtils.getMessage(player, MessageEnum.STATS_NOT_FOUND));
                        } else {
                            sender.sendMessage(playerName + "not found.");
                        }
                    }
                }
                catch (IllegalStateException e) {
                    sender.sendMessage("An error has occurred while loading the stats. Contact to developer.");
                }

            });

            return true;

        }

        Player player = ((Player) sender);
        BuildFFAStats stats = BuildFFAStatsManager.getCache().readData(player.getUniqueId());
        sendMessage(player, player.getName(), stats);

        return true;

    }

    private void sendMessage(CommandSender sender, String name, BuildFFAStats stats) {
        if (sender instanceof Player) {
            Player player = ((Player) sender);
            String prestige = PrestigeManager.getInstance().getPrestigeName(stats.getKills());
            player.sendMessage(StringUtils.color(MessageFormat.format(getListByString(
                    LangUtils.getMessage(player, MessageEnum.STATS_MESSAGE)),
                        name,
                        stats.getKills(),
                        stats.getDeaths(),
                        stats.getBestKillStreaks(),
                        prestige)));
        }
        else {
            String prestige = PrestigeManager.getInstance().getPrestigeName(stats.getKills());
            sender.sendMessage(StringUtils.color(
                    MessageFormat.format("name : {0} kills : {1} deaths : {2} bestKillStreaks : {3} prestige : {4}",
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
        for(String s : string.split(",")) {
            sb.append(StringUtils.color(s) + "\n");
        }
        return sb.toString();
    }

}
