package kr.teamcocoa.buildffa.prestige;

import kr.teamcocoa.core.utils.StringUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.*;
import java.text.MessageFormat;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum Prestige {
    BEGINNER(0, 99, "Beginner", "\uD83D\uDD30", Color.WHITE),
    BRONZE(100, 599, "Bronze", "B", Color.decode("#cd7f32")),
    SILVER(600, 1999, "Silver", "S", Color.decode("#aba9ad")),
    GOLD(2000, 3999, "Gold", "G", Color.decode("#ecbd00")),
    PLATINUM(4000, 5999, "Platinum", "P", Color.decode("#6abac3")),
    DIAMOND(6000, 7999, "Diamond", "D", Color.decode("#bf00ff")),
    MASTER(8000, 9999, "Master", "M", Color.decode("#00aa00")),
    GRAND_MASTER(10000, 12999, "Grand Master", "GM", Color.decode("#aa00aa")),
    LEGEND(13000, 15999, "Legend", "L", Color.decode("#ff5555")),
    CHALLENGER(16000, Integer.MAX_VALUE, "Challenger", "C", Color.YELLOW);


    private int minimumKills;
    private int maximumKills;
    private String name;
    private String shortName;
    private Color color;

    public String toBukkitColor() {
        char[] hex = String.format("%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue()).toCharArray();
        return StringUtils.color(MessageFormat.format(
                "&x&{0}&{1}&{2}&{3}&{4}&{5}",
                hex[0], hex[1], hex[2], hex[3], hex[4], hex[5]));
    }

}
