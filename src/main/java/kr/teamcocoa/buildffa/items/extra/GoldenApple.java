package kr.teamcocoa.buildffa.items.extra;

import kr.teamcocoa.core.utils.StringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GoldenApple extends AbstractExtraItem {
    private static GoldenApple instance;

    public static GoldenApple getInstance() {
        if(instance == null) {
            instance = new GoldenApple();
        }
        return instance;
    }

    @Override
    public ItemStack getItemStack(Player player, int count) {
        return new ItemStack(Material.GOLDEN_APPLE, count);
    }

    @Override
    public String getName(Player player) {
        return StringUtils.color("&cGolden Apple");
    }
}
