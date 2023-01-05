package kr.teamcocoa.buildffa.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Dye;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemManager {

    public static ItemStack grayGlassPane =
            hideAttributes(createItemStack(Material.STAINED_GLASS_PANE, " ", 1, new ArrayList(), (byte)7));

    public static ItemStack createItemStack(Material mat, int amount, String name) {
        ItemStack itemStack = new ItemStack(mat, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(StringUtils.color(name));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack createItemStack(Material material, String name, int amount, List<String> lore, byte data) {
        ItemStack itemStack = new ItemStack(material, amount, data);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(StringUtils.color(name));
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack createDye(String name, DyeColor dyeColor) {
        Dye dye = new Dye(dyeColor);
        ItemStack itemStack = dye.toItemStack();
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(StringUtils.color(name));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack setUnbreakable(ItemStack itemStack){
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("Unbreakable", true);
        net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy(itemStack);
        stack.setTag(tag);
        itemStack = CraftItemStack.asCraftMirror(stack);
        return itemStack;
    }

    public static ItemStack addEnchant(ItemStack itemStack, Enchantment enchantment, int weight) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addEnchant(enchantment, weight, true);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack hideAttributes(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES });
        itemMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_UNBREAKABLE });
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
