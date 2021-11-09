package kr.teamcocoa.buildffa.kit;

import org.bukkit.entity.Player;

public class NickedBffaPlayer {

    /* Stats */
    private int kills;
    private int deaths;
    private int bestKillStreaks;

    public NickedBffaPlayer(Player player){
        this.kills = 0;
        this.bestKillStreaks = 0;
        this.deaths = 0;
    }

    /*Getter*/

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getBestKillStreaks() {
        return bestKillStreaks;
    }

    /*Setter*/

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setBestKillStreaks(int bestKillStreaks) {
        this.bestKillStreaks = bestKillStreaks;
    }

    /*Stats Adder*/
    public void addKills() {
        this.kills += 1;
    }

    public void addDeaths() {
        this.deaths += 1;
    }

    @Override
    public String toString() {
        return "NickedBffaPlayer{" +
                "kills=" + kills +
                ", deaths=" + deaths +
                ", bestKillStreaks=" + bestKillStreaks +
                '}';
    }
}