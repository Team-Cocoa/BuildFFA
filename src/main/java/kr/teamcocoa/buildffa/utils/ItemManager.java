package kr.teamcocoa.buildffa.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemManager {
  public static ItemStack createItem(Material mat, int amount, String name) {
    ItemStack i = new ItemStack(mat, amount);
    ItemMeta m = i.getItemMeta();
    m.setDisplayName(name);
    i.setItemMeta(m);
    return i;
  }
}
