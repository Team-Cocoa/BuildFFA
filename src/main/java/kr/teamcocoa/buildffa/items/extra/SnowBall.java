package kr.teamcocoa.buildffa.items.extra;

import kr.teamcocoa.buildffa.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SnowBall extends AbstractExtraItem {
    private static SnowBall instance;

    private SnowBall() {

    }

    public static SnowBall getInstance() {
        if(instance == null) {
            instance = new SnowBall();
        }
        return instance;
    }

    @Override
    public ItemStack getItemStack(Player player, int count) {
        ItemStack itemStack = new ItemStack(Material.SNOW_BALL, count);
        return itemStack;
    }

    @Override
    public String getName(Player player) {
        return StringUtils.color("&cSnowBall");
    }
}
