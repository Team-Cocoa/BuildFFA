package kr.teamcocoa.buildffa.game.prestige;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum Prestige {
  BEGINNER(0, 99, "Beginner", "\uD83D\uDD30", ChatColor.WHITE),
  BRONZE(100, 599, "Bronze", "B", ChatColor.GOLD),
  SILVER(600, 1999, "Silver", "S", ChatColor.GRAY),
  GOLD(2000, 3999, "Gold", "G", ChatColor.YELLOW),
  PLATINUM(4000, 5999, "Platinum", "P", ChatColor.AQUA),
  DIAMOND(6000, 7999, "Diamond", "D", ChatColor.LIGHT_PURPLE),
  MASTER(8000, 9999, "Master", "M", ChatColor.DARK_GREEN),
  GRAND_MASTER(10000, 12999, "Grand Master", "GM", ChatColor.DARK_PURPLE),
  LEGEND(13000, 15999, "Legend", "L", ChatColor.RED),
  CHALLENGER(16000, Integer.MAX_VALUE, "Challenger", "C", ChatColor.YELLOW);

  private int minimumKills;
  private int maximumKills;
  private String name;
  private String shortName;
  private ChatColor color;

  public String toBukkitColor() {
    return color.toString();
  }
}
