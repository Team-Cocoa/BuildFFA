package kr.teamcocoa.buildffa.prestige;

import kr.teamcocoa.buildffa.enums.MessageEnum;
import kr.teamcocoa.buildffa.kit.BffaPlayer;
import kr.teamcocoa.buildffa.utils.LangUtils;
import kr.teamcocoa.buildffa.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

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
        if(kills <= Prestige.BEGINNER.getMaximumKills()) {
            return Prestige.BEGINNER;
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
        if(Prestige.LEGEND.getMinimumKills() <= kills && kills <= Prestige.LEGEND.getMaximumKills()) {
            return Prestige.LEGEND;
        }
        if(Prestige.CHALLENGER.getMinimumKills() <= kills && kills <= Prestige.CHALLENGER.getMaximumKills()) {
            return Prestige.CHALLENGER;
        }
        return Prestige.BEGINNER;
    }

    public int getPrestigeRank(Prestige prestige, int kills) {
        if(prestige == Prestige.BEGINNER) {
            return 0;
        }
        int minimum = prestige.getMinimumKills();
        int distance = ((prestige.getMaximumKills() + 1) - prestige.getMinimumKills()) / 5;
        for(int i = 0; i < 5; i++) {
            if(minimum + (distance * i) <= kills && kills <= (minimum + (distance * (i + 1))) -1) {
                return i + 1;
            }
        }
        return 0;
    }

    public void loadPrestige(BffaPlayer bffaPlayer) {
        Prestige prestige = getPrestige(bffaPlayer.getKills());
        int grade = getPrestigeRank(prestige, bffaPlayer.getKills());
        bffaPlayer.setPrestige(prestige);
        bffaPlayer.setGrade(grade);
    }

    public void updatePrestige(BffaPlayer bffaPlayer) {
        Prestige prestige = bffaPlayer.getPrestige();
        int grade = bffaPlayer.getGrade();

        int minimum = prestige.getMinimumKills();
        int distance = ((prestige.getMaximumKills() + 1) - prestige.getMinimumKills()) / 5;

        int kills = bffaPlayer.getKills();
        Bukkit.getLogger().info(
                (minimum + (distance * (grade - 1))) + " <= " + kills + " <= " + ((minimum + (distance * grade)) -1)
        );
        if(prestige == Prestige.BEGINNER) {
            if(0 <= kills && kills <= 99) {
                return;
            }
            else {
                loadPrestige(bffaPlayer);
                for(Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(
                            MessageFormat.format(StringUtils.getListByString(LangUtils.getMessage(player, MessageEnum.PRESTIGE_PROMOTE_MESSAGE)), bffaPlayer.getPlayer().getName(), StringUtils.color(getPrestigeName(bffaPlayer)))
                    );
                }
            }
        }
        else {
            if(minimum + (distance * (grade - 1)) <= kills && kills <= (minimum + (distance * grade)) -1) {
                return;
            }
            else {
                loadPrestige(bffaPlayer);
                if(!bffaPlayer.isNicked()) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendMessage(
                                MessageFormat.format(StringUtils.getListByString(LangUtils.getMessage(player, MessageEnum.PRESTIGE_PROMOTE_MESSAGE)), bffaPlayer.getPlayer().getName(), StringUtils.color(getPrestigeName(bffaPlayer)))
                        );
                    }
                }
            }
        }
    }

    public String getPrestigeName(int kills) {
        Prestige prestige = getPrestige(kills);
        int grade = getPrestigeRank(prestige, kills);
        return prestige.getName() + (prestige == Prestige.BEGINNER ? "" : " " + arabicToRome(grade));
    }

    public String getPrestigeName(BffaPlayer bffaPlayer) {
        return bffaPlayer.getPrestige().getName() + (bffaPlayer.getPrestige() == Prestige.BEGINNER ? "" : " " + arabicToRome(bffaPlayer.getGrade()));
    }

    private String arabicToRome(int num) {
        switch (num) {
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
            case 6:
                return "VI";
            case 7:
                return "VII";
            case 8:
                return "VIII";
            case 9:
                return "IX";
            case 10:
                return "X";
            default:
                return "";
        }
    }


}
