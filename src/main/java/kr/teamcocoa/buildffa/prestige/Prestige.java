package kr.teamcocoa.buildffa.prestige;

public enum Prestige {
    UNRANKED(0, 399, "&8Unranked"),
    BRONZE(400, 1999, "&7Bronze"),
    SILVER(2000, 3999, "&fSilver"),
    GOLD(4000, 5999, "&6Gold"),
    PLATINUM(6000, 7999, "&3Platinum"),
    DIAMOND(8000, 9999, "&bDiamond"),
    MASTER(10000, 11999, "&2Master"),
    GRAND_MASTER(12000, 13999, "&5Grand Master"),
    CHALLENGER(14000, Integer.MAX_VALUE, "&4Challenger");


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
