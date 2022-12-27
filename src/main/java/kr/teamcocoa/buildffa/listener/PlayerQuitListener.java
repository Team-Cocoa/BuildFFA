package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.database.BuildFFADatabase;
import kr.teamcocoa.buildffa.database.StatsDatabase;
import kr.teamcocoa.buildffa.managers.PlayerManager;
import kr.teamcocoa.buildffa.model.BuildFFAPlayer;
import kr.teamcocoa.buildffa.main.BuildFFA;
import kr.teamcocoa.buildffa.utils.StringUtils;
import kr.teamcocoa.buildffa.world.MapVote;
import kr.teamcocoa.nick.core.model.NickManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.text.MessageFormat;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PlayerQuitListener implements Listener {

    private ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 20, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<>(20));

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        e.setQuitMessage(null);

        Player player = e.getPlayer();
        BuildFFAPlayer buildFFAPlayer = PlayerManager.getPlayer(player);

        // 투표 무효 과정 처리
        MapVote mapVote = MapVote.getInstance();
        if (mapVote.getWherePlayerVoted(player) != null) {
            mapVote.removeVote(player, mapVote.getWherePlayerVoted(player));
        }

        // 사망 판정 처리
        if (buildFFAPlayer.isInGame()) {
            buildFFAPlayer.death(true);
        }

        executor.execute(() -> {
            StatsDatabase statsDatabase = BuildFFADatabase.getStatsDatabase();
            statsDatabase.updatePlayer(buildFFAPlayer);

            PlayerManager.removePlayer(player);

            boolean nicked = NickManager.getInstance().isNicked(player.getUniqueId());
            String joinMessage = MessageFormat.format(BuildFFA.PREFIX + StringUtils.color("&e{0} &cleft the game!"),
                    nicked ? NickManager.getInstance().getNickPlayer(player.getUniqueId()).getFakeNick() : player.getName());

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.sendMessage(joinMessage);
            }
        });
    }
}
