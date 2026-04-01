package kr.teamcocoa.buildffa.utils;

import kr.teamcocoa.buildffa.enums.OtherEnum;
import kr.teamcocoa.buildffa.BuildFFABootstrap;
import kr.teamcocoa.buildffa.models.BuildFFAPlayer;
import kr.teamcocoa.core.bukkit.utils.PacketUtils;
import kr.teamcocoa.core.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;


public class ScoreboardManager implements Listener {

    private static void setScoreboard(Player player, List<String> lines) {

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("BuildFFASB", "dummy");

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(StringUtils.color("&dBuildFFA"));







        Scoreboard scoreboard = new Scoreboard();
        Objective objective = scoreboard.addObjective("BuildFFASB",
                ObjectiveCriteria.DUMMY,
                Component.Serializer.fromJson("{\n" +
                        "  \"text\": \"" + StringUtils.color("&dBuildFFA") + "\"\n" +
                        "}"),
                ObjectiveCriteria.RenderType.INTEGER);

        ClientboundSetObjectivePacket removeObjective = new ClientboundSetObjectivePacket(objective, 1);

        ClientboundSetObjectivePacket createObjective = new ClientboundSetObjectivePacket(objective, 0);

        ClientboundSetDisplayObjectivePacket displayObjective = new ClientboundSetDisplayObjectivePacket(1, objective);

        List<ClientboundSetScorePacket> scores = new ArrayList<>();
        int voidCount = 0;
        int fixedIndex = lines.size() - 1;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).equals("")) {
                voidCount++;
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < voidCount; j++) {
                    sb.append(" ");
                }
                scores.add(getScorePacket(objective, sb.toString(), fixedIndex));
            } else {
                scores.add(getScorePacket(objective, lines.get(i), fixedIndex));
            }
            fixedIndex--;
        }

        PacketUtils.sendPackets(player, removeObjective, createObjective, displayObjective);
        for (ClientboundSetScorePacket packets : scores) {
            PacketUtils.sendPackets(player, packets);
        }
    }

    public static void sendBuildFFAScoreboard(BuildFFAPlayer buildFFAPlayer) {
        int kills = buildFFAPlayer.getBuildFFAStats().getKills();
        int killstreak = buildFFAPlayer.getBuildFFAStats().getBestKillStreaks();

        Player player = buildFFAPlayer.getPlayer();

        List<String> lines = new ArrayList<>();
        lines.add("&aTeamCocoa.kr");
        lines.add("");
        lines.add(LangUtils.getMessage(player, OtherEnum.SCOREBOARD_KILLS));
        lines.add("&8» &e" + kills);
        lines.add(" ");
        lines.add(LangUtils.getMessage(player, OtherEnum.SCOREBOARD_BEST_KILL_STREAK));
        lines.add("&8» &e" + killstreak + " ");
        lines.add("  ");
        lines.add(LangUtils.getMessage(player, BuildFFABootstrap.teaming ? OtherEnum.SCOREBOARD_TEAMING_ALLOW : OtherEnum.SCOREBOARD_TEAMING_PROHIBIT));

        setScoreboard(player, lines);
    }

    private static ClientboundSetScorePacket getScorePacket(Objective objective, String display, int scoreValue) {
        return new ClientboundSetScorePacket(ServerScoreboard.Method.CHANGE, objective.getName(), StringUtils.color(display), scoreValue);
    }

}
