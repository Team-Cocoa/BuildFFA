package kr.teamcocoa.buildffa.utils;

import kr.teamcocoa.buildffa.main.BuildFFA;
import kr.teamcocoa.buildffa.managers.PlayerManager;
import kr.teamcocoa.buildffa.model.BuildFFAPlayer;
import kr.teamcocoa.buildffa.model.BuildFFAStats;
import kr.teamcocoa.buildffa.translate.MessageNode;
import kr.teamcocoa.buildffa.translate.OtherNode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScoreboardExecutor {

    private static ScheduledExecutorService scoreboardUpdater = Executors.newSingleThreadScheduledExecutor();
    private static boolean updaterRunning = false;

    public static void setScoreboard(BuildFFAPlayer buildFFAPlayer) {
        Player player = buildFFAPlayer.getPlayer();

        BuildFFAStats stats = buildFFAPlayer.getStats();
        int kills = stats.getKills();
        int killStreak = stats.getBestKillStreak();

        Scoreboard scoreboard = new Scoreboard();
        ScoreboardObjective objective = scoreboard.registerObjective("buildffa", IScoreboardCriteria.b);
        objective.setDisplayName(StringUtils.color("&dBuildFFA"));

        PacketPlayOutScoreboardObjective removeObjective = new PacketPlayOutScoreboardObjective(objective, 1);

        PacketPlayOutScoreboardObjective createObjective = new PacketPlayOutScoreboardObjective(objective, 0);
        PacketPlayOutScoreboardDisplayObjective displayObjective = new PacketPlayOutScoreboardDisplayObjective(1, objective);

        List<PacketPlayOutScoreboardScore> scores = new ArrayList<>();
        scores.add(getScorePacket(scoreboard, objective, "&aTeamCocoa.kr", 8));
        scores.add(getScorePacket(scoreboard, objective, "", 7));
        scores.add(getScorePacket(scoreboard, objective, LangUtils.getMessage(player, OtherNode.SCOREBOARD_KILLS), 6));
        scores.add(getScorePacket(scoreboard, objective, "&8» &e" + kills, 5));
        scores.add(getScorePacket(scoreboard, objective, " ", 4));
        scores.add(getScorePacket(scoreboard, objective, LangUtils.getMessage(player, OtherNode.SCOREBOARD_BEST_KILL_STREAK), 3));
        scores.add(getScorePacket(scoreboard, objective, "&8» &e" + killStreak + " ", 2));
        scores.add(getScorePacket(scoreboard, objective, "  ", 1));
        scores.add(getScorePacket(scoreboard, objective, LangUtils.getMessage(player,
                BuildFFA.isTeaming() ? OtherNode.SCOREBOARD_TEAMING_ALLOW : OtherNode.SCOREBOARD_TEAMING_PROHIBIT), 0));

        PlayerUtils.sendPackets(player, removeObjective, createObjective, displayObjective);

        for (PacketPlayOutScoreboardScore packets : scores) {
            PlayerUtils.sendPackets(player, packets);
        }
    }

    private static PacketPlayOutScoreboardScore getScorePacket(Scoreboard scoreboard, ScoreboardObjective objective, String display, int scoreValue) {
        ScoreboardScore score = new ScoreboardScore(scoreboard, objective, StringUtils.color(display));
        score.setScore(scoreValue);
        return new PacketPlayOutScoreboardScore(score);
    }

    public static void startUpdater () {
        if(updaterRunning) {
            return;
        }
        scoreboardUpdater.scheduleAtFixedRate(() -> {
            for (BuildFFAPlayer buildFFAPlayer : PlayerManager.getAllPlayers()) {
                setScoreboard(buildFFAPlayer);
            }
        }, 0, 1, TimeUnit.SECONDS);
        updaterRunning = true;
    }

    public static void stopUpdater() {
        if(!updaterRunning) {
            return;
        }
        scoreboardUpdater.shutdown();
        updaterRunning = false;
    }
}
