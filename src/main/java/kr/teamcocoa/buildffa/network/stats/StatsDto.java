package kr.teamcocoa.buildffa.network.stats;

import kr.teamcocoa.buildffa.game.models.BuildFFAStats;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StatsDto {

  private String uuid;
  private int kills;
  private int deaths;
  private int killStreaks;
  private int bestKillStreaks;

  public StatsDto(BuildFFAStats stats) {
    this.uuid = stats.getUuid().toString();
    this.kills = stats.getKills();
    this.deaths = stats.getDeaths();
    this.killStreaks = stats.getKillStreaks();
    this.bestKillStreaks = stats.getBestKillStreaks();
  }
}
