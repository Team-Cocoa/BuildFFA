package utils;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemManager {
  static ItemStack item;
  
  static ItemMeta itemmeta;
  
  public ItemManager(ItemStack item) {
    ItemManager.item = item;
    itemmeta = item.getItemMeta();
  }
  
  public Builder modify() {
    return new Builder();
  }
  
  public static final class Builder {
    public Builder setLore(String... lore) {
      List<String> loreList = new ArrayList<>();
      byte b;
      int i;
      String[] arrayOfString;
      for (i = (arrayOfString = lore).length, b = 0; b < i; ) {
        String lores = arrayOfString[b];
        loreList.add(lores);
        b++;
      } 
      ItemManager.itemmeta.setLore(loreList);
      return this;
    }
    
    public Builder addEnchantment(Enchantment entchantment, int level) {
      ItemManager.itemmeta.addEnchant(entchantment, level, false);
      return this;
    }
    
    public Builder removeEnchantment(Enchantment entchantment) {
      ItemManager.itemmeta.removeEnchant(entchantment);
      return this;
    }
    
    public Builder setDisplayName(String display) {
      ItemManager.itemmeta.setDisplayName(display);
      return this;
    }
    
    public Builder setUnbreakable(boolean unbreakable) {
      ItemManager.itemmeta.spigot().setUnbreakable(unbreakable);
      return this;
    }
    
    public Builder HideFlag(ItemFlag itemflag) {
      ItemManager.itemmeta.addItemFlags(new ItemFlag[] { itemflag });
      return this;
    }
    
    public Builder ShowFlag(ItemFlag itemflag) {
      ItemManager.itemmeta.removeItemFlags(new ItemFlag[] { itemflag });
      return this;
    }
    
    public Builder HideFlagsExcept(ItemFlag itemflag) {
      HideFlags();
      ShowFlag(itemflag);
      return this;
    }
    
    public Builder ShowFlagsExcept(ItemFlag itemflag) {
      ShowFlags();
      HideFlag(itemflag);
      return this;
    }
    
    public Builder HideFlags() {
      ItemManager.itemmeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES });
      ItemManager.itemmeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DESTROYS });
      ItemManager.itemmeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
      ItemManager.itemmeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_PLACED_ON });
      ItemManager.itemmeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_POTION_EFFECTS });
      ItemManager.itemmeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_UNBREAKABLE });
      return this;
    }
    
    public Builder ShowFlags() {
      ItemManager.itemmeta.removeItemFlags(new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES });
      ItemManager.itemmeta.removeItemFlags(new ItemFlag[] { ItemFlag.HIDE_DESTROYS });
      ItemManager.itemmeta.removeItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
      ItemManager.itemmeta.removeItemFlags(new ItemFlag[] { ItemFlag.HIDE_PLACED_ON });
      ItemManager.itemmeta.removeItemFlags(new ItemFlag[] { ItemFlag.HIDE_POTION_EFFECTS });
      ItemManager.itemmeta.removeItemFlags(new ItemFlag[] { ItemFlag.HIDE_UNBREAKABLE });
      return this;
    }
    
    public ItemStack build() {
      ItemManager.item.setItemMeta(ItemManager.itemmeta);
      return ItemManager.item;
    }
  }
}
