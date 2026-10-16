package kr.teamcocoa.buildffa.game.items.extra;

import com.google.inject.Singleton;
import kr.teamcocoa.core.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Singleton
public class GoldenApple extends AbstractExtraItem {

  @Override
  public ItemStack getItemStack(Player player, int count) {
    return new ItemStack(Material.GOLDEN_APPLE, count);
  }

  @Override
  public String getName(Player player) {
    return StringUtils.color("&cGolden Apple");
  }
}
