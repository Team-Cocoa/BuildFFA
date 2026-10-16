package kr.teamcocoa.buildffa.task;

import com.google.inject.Inject;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayerManager;
import kr.teamcocoa.buildffa.utils.ScoreboardManager;

public class ScoreboardUpdateTask implements Runnable {

  private final BuildFFAPlayerManager buildFFAPlayerManager;

  private final ScoreboardManager scoreboardManager;

  @Inject
  public ScoreboardUpdateTask(
      BuildFFAPlayerManager buildFFAPlayerManager, ScoreboardManager scoreboardManager) {
    this.buildFFAPlayerManager = buildFFAPlayerManager;
    this.scoreboardManager = scoreboardManager;
  }

  @Override
  public void run() {
    for (BuildFFAPlayer buildFFAPlayer : buildFFAPlayerManager.getPlayers()) {
      scoreboardManager.sendBuildFFAScoreboard(buildFFAPlayer);
    }
  }
}
