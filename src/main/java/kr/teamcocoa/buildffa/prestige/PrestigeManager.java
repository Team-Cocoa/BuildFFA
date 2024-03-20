package kr.teamcocoa.buildffa.prestige;

import kr.teamcocoa.buildffa.enums.MessageEnum;
import kr.teamcocoa.buildffa.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.utils.LangUtils;
import kr.teamcocoa.core.utils.StringUtils;
import kr.teamcocoa.language.languages.LanguageController;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PrestigeManager {

    private static PrestigeManager instance;

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

    public void loadPrestige(BuildFFAPlayer buildFFAPlayer) {
        Prestige prestige = getPrestige(buildFFAPlayer.getBuildFFAStats().getKills());
        int grade = getPrestigeRank(prestige, buildFFAPlayer.getBuildFFAStats().getKills());
        buildFFAPlayer.setPrestige(prestige);
        buildFFAPlayer.setGrade(grade);
    }

    public void updatePrestige(BuildFFAPlayer buildFFAPlayer) {
        Prestige prestige = buildFFAPlayer.getPrestige();
        int grade = buildFFAPlayer.getGrade();

        int minimum = prestige.getMinimumKills();
        int distance = ((prestige.getMaximumKills() + 1) - prestige.getMinimumKills()) / 5;

        int kills = buildFFAPlayer.getBuildFFAStats().getKills();
        Bukkit.getLogger().info(
                (minimum + (distance * (grade - 1))) + " <= " + kills + " <= " + ((minimum + (distance * grade)) -1)
        );
        if(prestige == Prestige.BEGINNER) {
            if(0 <= kills && kills <= 99) {
                return;
            }
            else {
                loadPrestige(buildFFAPlayer);
                for(Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(
                            MessageFormat.format(String.join(",",
                                            LangUtils.getMessage(player, MessageEnum.PRESTIGE_PROMOTE_MESSAGE)
                                                    .replace("[", "")
                                                    .replace("]", "")
                                                    .split(",")),
                                    buildFFAPlayer.getPlayer().getName(), StringUtils.color(getPrestigeName(buildFFAPlayer))));
                }
            }
        }
        else {
            if(minimum + (distance * (grade - 1)) <= kills && kills <= (minimum + (distance * grade)) -1) {
                return;
            }
            else {
                loadPrestige(buildFFAPlayer);
                for(Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(
                            MessageFormat.format(String.join(",",
                                            LangUtils.getMessage(player, MessageEnum.PRESTIGE_PROMOTE_MESSAGE)
                                                    .replace("[", "")
                                                    .replace("]", "")
                                                    .split(",")),
                                    buildFFAPlayer.getPlayer().getName(), StringUtils.color(getPrestigeName(buildFFAPlayer))));
                }
            }
        }
    }

    public String getPrestigeName(int kills) {
        Prestige prestige = getPrestige(kills);
        int grade = getPrestigeRank(prestige, kills);
        return prestige.getName() + (prestige == Prestige.BEGINNER ? "" : " " + arabicToRome(grade));
    }

    public String getPrestigeName(BuildFFAPlayer buildFFAPlayer) {
        return buildFFAPlayer.getPrestige().getName() + (buildFFAPlayer.getPrestige() == Prestige.BEGINNER ? "" : " " + arabicToRome(buildFFAPlayer.getGrade()));
    }

    private String arabicToRome(int num) {
        return switch(num) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            case 6 -> "VI";
            case 7 -> "VII";
            case 8 -> "VIII";
            case 9 -> "IX";
            case 10 -> "X";
            default -> "";
        };
    }


}
