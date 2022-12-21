package kr.teamcocoa.buildffa.utils;

import kr.teamcocoa.buildffa.translate.MessageNode;
import kr.teamcocoa.buildffa.translate.OtherNode;
import kr.teamcocoa.buildffa.main.BuildFFA;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import net.minecraft.server.v1_8_R3.IScoreboardCriteria;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardObjective;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_8_R3.Scoreboard;
import net.minecraft.server.v1_8_R3.ScoreboardObjective;
import net.minecraft.server.v1_8_R3.ScoreboardScore;

import java.util.ArrayList;
import java.util.List;


public class ScoreboardManager implements Listener {

    public void setScoreboard(Player player) {
        try {
            int kills = BuildFFA.playerData.get(player).getKills();
            int killStreak = BuildFFA.playerData.get(player).getBestKillStreaks();

            Scoreboard scoreboard = new Scoreboard();
            ScoreboardObjective objective = scoreboard.registerObjective("buildffa", IScoreboardCriteria.b);
            objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&dBuildFFA"));

            PacketPlayOutScoreboardObjective removeObjective = new PacketPlayOutScoreboardObjective(objective, 1);

            PacketPlayOutScoreboardObjective createObjective = new PacketPlayOutScoreboardObjective(objective, 0);
            PacketPlayOutScoreboardDisplayObjective displayObjective = new PacketPlayOutScoreboardDisplayObjective(1, objective);

            List<PacketPlayOutScoreboardScore> scores = new ArrayList<>();
            scores.add(getScorePacket(scoreboard, objective, StringUtils.color("&aTeamCocoa.kr"), 8));
            scores.add(getScorePacket(scoreboard, objective, "", 7));
            scores.add(getScorePacket(scoreboard, objective, LangUtils.getMessage(player, OtherNode.SCOREBOARD_KILLS), 6));
            scores.add(getScorePacket(scoreboard, objective, StringUtils.color("&8» &e" + kills), 5));
            scores.add(getScorePacket(scoreboard, objective, " ", 4));
            scores.add(getScorePacket(scoreboard, objective, LangUtils.getMessage(player, OtherNode.SCOREBOARD_BEST_KILL_STREAK), 3));
            scores.add(getScorePacket(scoreboard, objective, StringUtils.color("&8» &e" + killStreak + " "), 2));
            scores.add(getScorePacket(scoreboard, objective, "  ", 1));
            scores.add(getScorePacket(scoreboard, objective, LangUtils.getMessage(player, BuildFFA.teaming ? OtherNode.SCOREBOARD_TEAMING_ALLOW : OtherNode.SCOREBOARD_TEAMING_PROHIBIT), 0));

            PlayerUtils.sendPackets(player, removeObjective, createObjective, displayObjective);

            for (PacketPlayOutScoreboardScore packets : scores) {
                PlayerUtils.sendPackets(player, packets);
            }
        } catch (NullPointerException e) {

        }
    }

    private PacketPlayOutScoreboardScore getScorePacket(Scoreboard scoreboard, ScoreboardObjective objective, String display, int scoreValue) {
        ScoreboardScore score = new ScoreboardScore(scoreboard, objective, display);
        score.setScore(scoreValue);
        return new PacketPlayOutScoreboardScore(score);
    }

    public static void sendAllPlayer(MessageNode node, int sec) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(LangUtils.getMessage(player, node).replace("%time%", String.valueOf(sec)));
        }
    }

    public void ScoreboardUpdater() {
        (new BukkitRunnable() {
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    setScoreboard(player);
                }
            }
        }).runTaskTimer(BuildFFA.getInstance(), 0L, 20L);
    }
}
