package kr.teamcocoa.buildffa.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class BuildFFAStats {

    private int kills;
    private int deaths;

    @Setter
    private int bestKillStreak;

    public void addKills() {
        this.kills++;
    }

    public void addDeaths() {
        this.deaths++;
    }

}
