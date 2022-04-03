package kr.teamcocoa.buildffa.items.extra;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractExtraItem {
    public abstract ItemStack getItemStack(Player player, int amount);
    public abstract String getName(Player player);
}
