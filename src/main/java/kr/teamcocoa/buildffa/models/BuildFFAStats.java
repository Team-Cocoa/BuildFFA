package kr.teamcocoa.buildffa.models;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public class BuildFFAStats {

    private final UUID uuid;

    private int kills;
    private int deaths;
    private int killStreaks;

    @Setter
    private int bestKillStreaks;

    @Setter
    private boolean edited;

    public BuildFFAStats(UUID uuid) {
        this.uuid = uuid;
    }

    public void init(int kills, int deaths, int killStreaks, int bestKillStreaks) {
        this.kills = kills;
        this.deaths = deaths;
        this.killStreaks = killStreaks;
        this.bestKillStreaks = bestKillStreaks;
        this.edited = false;
    }

    public void addKills(int i) {
        this.kills += i;
        addKillStreaks(i);
        this.edited = true;
    }

    public void addDeaths(int i) {
        this.deaths += i;
        resetKillStreaks();
        this.edited = true;
    }

    public void addKillStreaks(int i) {
        this.killStreaks += i;
        if(killStreaks > bestKillStreaks) {
            this.bestKillStreaks = killStreaks;
        }
    }

    public void resetKillStreaks() {
        this.killStreaks = 0;
    }

}
