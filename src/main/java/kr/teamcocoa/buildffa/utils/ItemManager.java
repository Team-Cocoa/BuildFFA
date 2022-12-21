package kr.teamcocoa.buildffa.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

import static kr.teamcocoa.buildffa.kit.KitData.createItemStack;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemManager {

    public static ItemStack grayGlassPane = createItemStack(Material.STAINED_GLASS_PANE, " ", 1, new ArrayList(), (byte)7);

    public static ItemStack createItem(Material mat, int amount, String name) {
        ItemStack i = new ItemStack(mat, amount);
        ItemMeta m = i.getItemMeta();
        m.setDisplayName(name);
        i.setItemMeta(m);
        return i;
    }
}
