package kr.teamcocoa.buildffa.utils;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.game.models.GameSettings;
import kr.teamcocoa.buildffa.translation.LangUtils;
import kr.teamcocoa.buildffa.translation.OtherEnum;
import kr.teamcocoa.core.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

@Singleton
public class ScoreboardManager {

  private static final String OBJECTIVE_NAME = "BuildFFASB";

  // 1.8 scoreboard entries can't be longer than 40 characters
  private static final int MAX_ENTRY_LENGTH = 40;

  private final GameSettings gameSettings;

  @Inject
  public ScoreboardManager(GameSettings gameSettings) {
    this.gameSettings = gameSettings;
  }

  private static void setScoreboard(Player player, List<String> lines) {
    // Use the player's own scoreboard so the name tag teams from TabManager are kept
    Scoreboard scoreboard = player.getScoreboard();
    if (scoreboard.equals(Bukkit.getScoreboardManager().getMainScoreboard())) {
      scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
      player.setScoreboard(scoreboard);
    }

    Objective objective = scoreboard.getObjective(OBJECTIVE_NAME);
    if (objective == null) {
      objective = scoreboard.registerNewObjective(OBJECTIVE_NAME, "dummy");
      objective.setDisplayName(StringUtils.color("&dBuildFFA"));
    }
    if (objective.getDisplaySlot() != DisplaySlot.SIDEBAR) {
      objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    Set<String> entries = new HashSet<>();
    int voidCount = 0;
    int fixedIndex = lines.size() - 1;
    for (String line : lines) {
      String entry;
      if (line.equals("")) {
        voidCount++;
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < voidCount; j++) {
          sb.append(" ");
        }
        entry = sb.toString();
      } else {
        entry = StringUtils.color(line);
      }

      if (entry.length() > MAX_ENTRY_LENGTH) {
        entry = entry.substring(0, MAX_ENTRY_LENGTH);
      }

      entries.add(entry);
      Score score = objective.getScore(entry);
      if (!score.isScoreSet() || score.getScore() != fixedIndex) {
        score.setScore(fixedIndex);
      }
      fixedIndex--;
    }

    for (String entry : new ArrayList<>(scoreboard.getEntries())) {
      if (!entries.contains(entry) && objective.getScore(entry).isScoreSet()) {
        scoreboard.resetScores(entry);
      }
    }
  }

  public void sendBuildFFAScoreboard(BuildFFAPlayer buildFFAPlayer) {
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
    lines.add(
        LangUtils.getMessage(
            player,
            gameSettings.isTeaming()
                ? OtherEnum.SCOREBOARD_TEAMING_ALLOW
                : OtherEnum.SCOREBOARD_TEAMING_PROHIBIT));

    setScoreboard(player, lines);
  }
}
