package kr.teamcocoa.buildffa.prestige;

public enum Prestige {
    BEGINNER(0, 99, "&8Beginner"),
    BRONZE(100, 599, "&7Bronze"),
    SILVER(600, 1999, "&fSilver"),
    GOLD(2000, 3999, "&6Gold"),
    PLATINUM(4000, 5999, "&3Platinum"),
    DIAMOND(6000, 7999, "&bDiamond"),
    MASTER(8000, 9999, "&2Master"),
    GRAND_MASTER(10000, 12999, "&5Grand Master"),
    LEGEND(13000, 15999, "&cLegend"),
    CHALLENGER(16000, Integer.MAX_VALUE, "&4Challenger");


    private int minimumKills;
    private int maximumKills;
    private String name;

    private Prestige(int minimumKills, int maximumKills, String name) {
        this.minimumKills = minimumKills;
        this.maximumKills = maximumKills;
        this.name = name;
    }

    public int getMinimumKills() {
        return minimumKills;
    }

    public int getMaximumKills() {
        return maximumKills;
    }

    public String getName() {
        return name;
    }
}
