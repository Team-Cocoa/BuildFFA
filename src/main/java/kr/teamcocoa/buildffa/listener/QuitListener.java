package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.databases.StatsDatabase;
import kr.teamcocoa.buildffa.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.models.BuildFFAPlayerManager;
import kr.teamcocoa.buildffa.world.MapVote;
import kr.teamcocoa.buildffa.world.maps.Maps;
import kr.teamcocoa.core.utils.StringUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class QuitListener implements Listener {

    private static ThreadPoolExecutor executors = new ThreadPoolExecutor(1, 30, 1, TimeUnit.SECONDS, new LinkedBlockingDeque<>(30));

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        e.quitMessage(Component.empty());

        BuildFFAPlayer buildFFAPlayer = BuildFFAPlayerManager.getPlayer(player);

        String quitMessage = StringUtils.color("&a[&dBuildFFA&a] &e" + player.getName() + " left the game!");
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendMessage(quitMessage);
        }

        Maps votedMap = MapVote.getInstance().getWhatPlayerVoted(player);

        if(votedMap != null) {
            MapVote.getInstance().removeVote(player, votedMap);
        }

        if(buildFFAPlayer.isInGame()) {
            buildFFAPlayer.death(true);
        }

        BuildFFAPlayerManager.removePlayer(player);

        executors.execute(() -> {
            StatsDatabase.upsertStats(buildFFAPlayer.getBuildFFAStats());
        });

    }
}
