package kr.teamcocoa.buildffa.game.tabs;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.text.MessageFormat;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayerManager;
import kr.teamcocoa.buildffa.game.prestige.Prestige;
import kr.teamcocoa.buildffa.game.prestige.PrestigeManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

@Singleton
public class TabManager {

  private final BuildFFAPlayerManager buildFFAPlayerManager;

  private final PrestigeManager prestigeManager;

  @Inject
  public TabManager(BuildFFAPlayerManager buildFFAPlayerManager, PrestigeManager prestigeManager) {
    this.buildFFAPlayerManager = buildFFAPlayerManager;
    this.prestigeManager = prestigeManager;
  }

  public void updateNameTags(Player player) {
    Preconditions.checkNotNull(player);

    initScoreboard(player);

    BuildFFAPlayer targetBuildFFAPlayer = buildFFAPlayerManager.getPlayer(player);

    Bukkit.getOnlinePlayers()
        .forEach(
            all -> {
              initScoreboard(all);

              BuildFFAPlayer receiveBuildFFAPlayer = buildFFAPlayerManager.getPlayer(all);

              addTeamEntry(
                  player,
                  all,
                  targetBuildFFAPlayer.getPrestige(),
                  targetBuildFFAPlayer.getBuildFFAStats().getKills());

              addTeamEntry(
                  all,
                  player,
                  receiveBuildFFAPlayer.getPrestige(),
                  receiveBuildFFAPlayer.getBuildFFAStats().getKills());
            });
  }

  private void addTeamEntry(Player target, Player all, Prestige prestige, int kills) {
    String teamName = prestige.getShortName();

    if (teamName.length() > 16) {
      teamName = teamName.substring(0, 16);
    }

    Team team = all.getScoreboard().getTeam(teamName);
    if (team == null) {
      team = all.getScoreboard().registerNewTeam(teamName);
    }

    String suffix =
        prestige == Prestige.BEGINNER
            ? MessageFormat.format("&8 | {0}{1}", prestige.toBukkitColor(), prestige.getShortName())
            : MessageFormat.format(
                "&8 | {0}{1} &8| {2}{3}",
                prestige.toBukkitColor(),
                prestige.getShortName(),
                prestige.toBukkitColor(),
                prestigeManager.arabicToRome(prestigeManager.getPrestigeRank(prestige, kills)));

    team.setPrefix(ChatColor.GRAY.toString());

    team.setSuffix(limitTeamText(ChatColor.translateAlternateColorCodes('&', suffix)));

    team.addEntry(target.getName());

    target.setDisplayName(ChatColor.GRAY + target.getName());
  }

  // 1.8 throws if a team prefix or suffix is longer than 16 characters
  private static String limitTeamText(String text) {
    return text.length() > 16 ? text.substring(0, 16) : text;
  }

  private static void initScoreboard(Player all) {
    if (all.getScoreboard().equals(all.getServer().getScoreboardManager().getMainScoreboard())) {
      all.setScoreboard(all.getServer().getScoreboardManager().getNewScoreboard());
    }
  }
}
