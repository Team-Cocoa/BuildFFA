package kr.teamcocoa.buildffa.prestige;

public class PrestigeManager {

    private static PrestigeManager instance;

    private PrestigeManager() {

    }

    public static PrestigeManager getInstance() {
        if(instance == null) {
            instance = new PrestigeManager();
        }
        return instance;
    }

    public Prestige getPrestige(int kills) {
        if(kills <= Prestige.UNRANKED.getMaximumKills()) {
            return Prestige.UNRANKED;
        }
        if(Prestige.BRONZE.getMinimumKills() <= kills && kills <= Prestige.BRONZE.getMaximumKills()) {
            return Prestige.BRONZE;
        }
        if(Prestige.SILVER.getMinimumKills() <= kills && kills <= Prestige.SILVER.getMaximumKills()) {
            return Prestige.SILVER;
        }
        if(Prestige.GOLD.getMinimumKills() <= kills && kills <= Prestige.GOLD.getMaximumKills()) {
            return Prestige.GOLD;
        }
        if(Prestige.PLATINUM.getMinimumKills() <= kills && kills <= Prestige.PLATINUM.getMaximumKills()) {
            return Prestige.PLATINUM;
        }
        if(Prestige.DIAMOND.getMinimumKills() <= kills && kills <= Prestige.DIAMOND.getMaximumKills()) {
            return Prestige.DIAMOND;
        }
        if(Prestige.MASTER.getMinimumKills() <= kills && kills <= Prestige.MASTER.getMaximumKills()) {
            return Prestige.MASTER;
        }
        if(Prestige.GRAND_MASTER.getMinimumKills() <= kills && kills <= Prestige.GRAND_MASTER.getMaximumKills()) {
            return Prestige.GRAND_MASTER;
        }
        if(Prestige.CHALLENGER.getMinimumKills() <= kills && kills <= Prestige.CHALLENGER.getMaximumKills()) {
            return Prestige.CHALLENGER;
        }
        return Prestige.UNRANKED;
    }

    public int getPrestigeRank(Prestige prestige, int kills) {
        if(prestige == Prestige.UNRANKED) {
            return 0;
        }

        int minimum = prestige.getMinimumKills();
        int maximum = prestige.getMaximumKills();

//        if(m)
    }
}
