package utils;

import main.Main;
import utils.Stats;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Inventory {
  public static void setJoinInventory(Player p) {
    p.getInventory().clear();
    p.getInventory().setArmorContents(null);
    if (Config.config.getBoolean("kits")) {
      p.getInventory().setItem(1, createItem(Material.BLAZE_ROD, 1, "§cInventorySorting"));
      p.getInventory().setItem(4, createItem(Material.CHEST, 1, "§cKits"));
      if (Config.player.get("players." + p.getUniqueId().toString()) != null) {
        if (Config.player.get("players." + p.getUniqueId().toString() + ".kitselected") != null) {
          String Kitname = Config.player.getString("players." + p.getUniqueId().toString() + ".kitselected");
          String Kitprefix = Config.kits.getString(String.valueOf(Kitname) + ".prefix");
          ItemStack Kitsymbol = Config.kits.getItemStack(String.valueOf(Kitname) + ".symbol");
          Material KitsymbolMaterial = Kitsymbol.getType();
          p.getInventory().setItem(0, createItem(KitsymbolMaterial, 1, Kitprefix));
        } else {
          p.getInventory().setItem(0, createItem(Material.BARRIER, 1, "§cNo kit selected"));
        } 
      } else {
        p.getInventory().setItem(0, createItem(Material.BARRIER, 1, "§cNo kit selected"));
      } 
    } 
    if (Config.config.getBoolean("stats"))
      p.getInventory().setItem(6, createItem(Material.BOOK, 1, "§cStats")); 
    if (Config.config.getBoolean("lobbyitem"))
      p.getInventory().setItem(8, createItem(Material.SLIME_BALL, 1, "§cBack to the lobby"));
  }
  
  public static void saveInventoryHotbar(Player p, String Kitname) {
    ItemStack[] contents = p.getInventory().getContents();
    for (int i = 0; i < 9; i++) {
      if (contents[i] != null && contents[i].getType() != Material.AIR) {
        Config.kits.set(String.valueOf(Kitname) + ".items." + i, contents[i]);
      } else {
        Config.kits.set(String.valueOf(Kitname) + ".items." + i, null);
      } 
    } 
    try {
      Config.kits.save(Config.kitsFile);
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }
  
  public static void saveInventoryArmor(Player p, String Kitname) {
    ItemStack[] contents = p.getInventory().getArmorContents();
    for (int i = 0; i < contents.length; i++) {
      if (contents[i] != null && contents[i].getType() != Material.AIR) {
        Config.kits.set(String.valueOf(Kitname) + ".armor." + i, contents[i]);
      } else {
        Config.kits.set(String.valueOf(Kitname) + ".armor." + i, null);
      } 
    } 
    try {
      Config.kits.save(Config.kitsFile);
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }
  
  public static ItemStack[] getInventoryHotbar(String Kitname) {
    ItemStack[] contents = new ItemStack[9];
    for (int i = 0; i < contents.length; i++) {
      ItemStack inv = Config.kits.getItemStack(String.valueOf(Kitname) + ".items." + i, contents[i]);
      if (inv != null && inv.getAmount() != 0) {
        contents[i] = inv;
      } else if (inv != null) {
        if (inv.getAmount() == 0) {
          inv.setAmount(1);
          contents[i] = inv;
        } 
      } 
    } 
    return contents;
  }
  
  public static ItemStack[] getInventoryArmorContents(String Kitname) {
    ItemStack[] contents = new ItemStack[4];
    for (int i = 0; i < contents.length; i++) {
      ItemStack inv = Config.kits.getItemStack(String.valueOf(Kitname) + ".armor." + i, contents[i]);
      if (inv != null && inv.getAmount() != 0) {
        contents[i] = inv;
      } else if (inv != null) {
        if (inv.getAmount() == 0) {
          inv.setAmount(1);
          contents[i] = inv;
        } 
      } 
    } 
    return contents;
  }
  
  public static ItemStack createItem(Material mat, int anzahl, String name) {
    ItemStack i = new ItemStack(mat, anzahl);
    ItemMeta m = i.getItemMeta();
    m.setDisplayName(name);
    i.setItemMeta(m);
    return i;
  }
  
  public static void setKitAuswahlInventory(Player p) {
    Boolean Kit1 = Boolean.valueOf(false);
    Boolean Kit2 = Boolean.valueOf(false);
    Boolean Kit3 = Boolean.valueOf(false);
    Boolean Kit4 = Boolean.valueOf(false);
    org.bukkit.inventory.Inventory inv = Bukkit.createInventory(null, 27, "§cKit Selection");
    if (Config.kits.getString("kits.view") != null) {
      if (Config.kits.getString("kits.view.selection.1") != null) {
        String Kitname = Config.kits.getString("kits.view.selection.1");
        if (Config.kits.getString(String.valueOf(Kitname) + ".symbol") != null) {
          ItemStack symbol = Config.kits.getItemStack(String.valueOf(Kitname) + ".symbol");
          if (Config.kits.getString(String.valueOf(Kitname) + ".prefix") != null) {
            String prefix = Config.kits.getString(String.valueOf(Kitname) + ".prefix");
            ItemMeta newItemMeta = null;
            ItemMeta otherItemMeta = null;
            if (symbol.hasItemMeta()) {
              newItemMeta = symbol.getItemMeta();
            } else {
              p.getItemInHand().setItemMeta(otherItemMeta);
              newItemMeta = symbol.getItemMeta();
            } 
            newItemMeta.setDisplayName(prefix);
            symbol.setItemMeta(newItemMeta);
            inv.setItem(10, symbol);
            Kit1 = Boolean.valueOf(true);
          } else {
            p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nokitprefix").replaceAll("&", "§"));
          } 
        } else {
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nokitsymbol").replaceAll("&", "§"));
        } 
      } else {
        inv.setItem(10, createItem(Material.BARRIER, 1, "§cKit(1) is coming soon..."));
        Kit1 = Boolean.valueOf(true);
      } 
      if (Config.kits.getString("kits.view.selection.2") != null) {
        String kitname = Config.kits.getString("kits.view.selection.2");
        if (Config.kits.getString(String.valueOf(kitname) + ".symbol") != null) {
          ItemStack symbol = Config.kits.getItemStack(String.valueOf(kitname) + ".symbol");
          if (Config.kits.getString(String.valueOf(kitname) + ".prefix") != null) {
            String prefix = Config.kits.getString(String.valueOf(kitname) + ".prefix");
            ItemMeta newItemMeta = null;
            ItemMeta otherItemMeta = null;
            if (symbol.hasItemMeta()) {
              newItemMeta = symbol.getItemMeta();
            } else {
              p.getItemInHand().setItemMeta(otherItemMeta);
              newItemMeta = symbol.getItemMeta();
            } 
            newItemMeta.setDisplayName(prefix);
            symbol.setItemMeta(newItemMeta);
            inv.setItem(12, symbol);
            Kit2 = Boolean.valueOf(true);
          } else {
            p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nokitprefix").replaceAll("&", "§"));
          } 
        } else {
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nokitsymbol").replaceAll("&", "§"));
        } 
      } else {
        inv.setItem(12, createItem(Material.BARRIER, 1, "§cKit(2) is coming soon..."));
        Kit2 = Boolean.valueOf(true);
      } 
      if (Config.kits.getString("kits.view.selection.3") != null) {
        String Kitname = Config.kits.getString("kits.view.selection.3");
        if (Config.kits.getString(String.valueOf(Kitname) + ".symbol") != null) {
          ItemStack symbol = Config.kits.getItemStack(String.valueOf(Kitname) + ".symbol");
          if (Config.kits.getString(String.valueOf(Kitname) + ".prefix") != null) {
            String prefix = Config.kits.getString(String.valueOf(Kitname) + ".prefix");
            ItemMeta newItemMeta = null;
            ItemMeta otherItemMeta = null;
            if (symbol.hasItemMeta()) {
              newItemMeta = symbol.getItemMeta();
            } else {
              p.getItemInHand().setItemMeta(otherItemMeta);
              newItemMeta = symbol.getItemMeta();
            } 
            newItemMeta.setDisplayName(prefix);
            symbol.setItemMeta(newItemMeta);
            inv.setItem(14, symbol);
            Kit3 = Boolean.valueOf(true);
          } else {
            p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nokitprefix").replaceAll("&", "§"));
          } 
        } else {
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nokitsymbol").replaceAll("&", "§"));
        } 
      } else {
        inv.setItem(14, createItem(Material.BARRIER, 1, "§cKit(3) is coming soon..."));
        Kit3 = Boolean.valueOf(true);
      } 
      if (Config.kits.getString("kits.view.selection.4") != null) {
        String Kitname = Config.kits.getString("kits.view.selection.4");
        if (Config.kits.getString(String.valueOf(Kitname) + ".symbol") != null) {
          ItemStack symbol = Config.kits.getItemStack(String.valueOf(Kitname) + ".symbol");
          if (Config.kits.getString(String.valueOf(Kitname) + ".prefix") != null) {
            String prefix = Config.kits.getString(String.valueOf(Kitname) + ".prefix");
            ItemMeta newItemMeta = null;
            ItemMeta otherItemMeta = null;
            if (symbol.hasItemMeta()) {
              newItemMeta = symbol.getItemMeta();
            } else {
              p.getItemInHand().setItemMeta(otherItemMeta);
              newItemMeta = symbol.getItemMeta();
            } 
            newItemMeta.setDisplayName(prefix);
            symbol.setItemMeta(newItemMeta);
            inv.setItem(16, symbol);
            Kit4 = Boolean.valueOf(true);
          } else {
            p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nokitprefix").replaceAll("&", "§"));
          } 
        } else {
          p.sendMessage(String.valueOf(Main.getPrefix()) + Config.messages.getString("nokitsymbol").replaceAll("&", "§"));
        } 
      } else {
        inv.setItem(16, createItem(Material.BARRIER, 1, "§cKit(4) is coming soon..."));
        Kit4 = Boolean.valueOf(true);
      } 
      int i;
      for (i = 0; i < 10; i++)
        inv.setItem(i, createItem(Material.STAINED_GLASS, 1, " ")); 
      inv.setItem(11, createItem(Material.STAINED_GLASS, 1, " "));
      inv.setItem(13, createItem(Material.STAINED_GLASS, 1, " "));
      inv.setItem(15, createItem(Material.STAINED_GLASS, 1, " "));
      for (i = 17; i < 26; i++)
        inv.setItem(i, createItem(Material.STAINED_GLASS, 1, " ")); 
      if (Config.player.getString("players." + p.getUniqueId().toString() + ".kitselected") != null) {
        inv.setItem(26, createItem(Material.BLAZE_ROD, 1, "§cReset§7-§6Sorting§7-§c" + Config.player.getString("players." + p.getUniqueId().toString() + ".kitselected")));
      } else {
        inv.setItem(26, createItem(Material.BLAZE_ROD, 1, "§cReset§7-§6Sorting§7-§cNone"));
      } 
      if (Kit1.booleanValue() && Kit2.booleanValue() && Kit3.booleanValue() && Kit4.booleanValue()) {
//        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 2147483647, 2));
//        p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 2147483647, 1));
        p.openInventory(inv);
      } 
    } 
  }
  
  public static void saveSortInventoryHotbar(Player p, String Kitname, org.bukkit.inventory.Inventory inv) {
    ItemStack[] contents = inv.getContents();
    for (int i = 0; i < contents.length; i++) {
      if (contents[i] != null && contents[i].getType() != Material.AIR) {
        Config.player.set("players." + p.getUniqueId().toString() + "." + Kitname + ".items." + i, contents[i]);
      } else {
        Config.player.set("players." + p.getUniqueId().toString() + "." + Kitname + ".items." + i, null);
      } 
    } 
    try {
      Config.player.save(Config.playerFile);
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }
  
  public static ItemStack[] getSortInventoryHotbar(Player p, String Kitname) {
    ItemStack[] contents = new ItemStack[9];
    for (int i = 0; i < contents.length; i++) {
      ItemStack inv = Config.player.getItemStack("players." + p.getUniqueId().toString() + "." + Kitname + ".items." + i, contents[i]);
      if (inv != null && inv.getAmount() != 0) {
        contents[i] = inv;
      } else if (inv != null) {
        if (inv.getAmount() == 0) {
          inv.setAmount(1);
          contents[i] = inv;
        } 
      } 
    } 
    return contents;
  }
  
  public static void setStatsInventory(Player p) {
    org.bukkit.inventory.Inventory inv = Bukkit.createInventory(null, InventoryType.BREWING, "§cStats");
    String uuid = String.valueOf(p.getUniqueId());
    ItemStack killsItemStack = createItem(Material.RED_ROSE, 1, "§cKills:");
    String[] killsString = { "§6" + String.valueOf(Stats.getKills(uuid)) };
    setItemLore(killsItemStack, killsString);
    inv.setItem(0, killsItemStack);
    ItemStack deathsItemStack = createItem(Material.BONE, 1, "§cDeaths:");
    String[] deathsString = { "§6" + String.valueOf(Stats.getDeaths(uuid)) };
    setItemLore(deathsItemStack, deathsString);
    inv.setItem(2, deathsItemStack);
    ItemStack kdItemStack = createItem(Material.FLINT, 1, "§cKD:");
    String KD = String.valueOf(Stats.getKills(uuid));
    if (Stats.getKills(uuid).intValue() != 0 && Stats.getDeaths(uuid).intValue() != 0) {
      double killsDouble = Stats.getKills(uuid).intValue();
      double deathsDouble = Stats.getDeaths(uuid).intValue();
      double killsdeaths = killsDouble / deathsDouble;
      KD = (new DecimalFormat("#0.00")).format(killsdeaths);
    } 
    String KDString = KD;
    String[] kdLore = { "§6" + KDString };
    setItemLore(kdItemStack, kdLore);
    inv.setItem(1, kdItemStack);
    ItemStack infoItemStack = createItem(Material.BOOK_AND_QUILL, 1, "§cStats of");
    String[] infoString = { "§6" + p.getName() };
    setItemLore(infoItemStack, infoString);
    inv.setItem(3, infoItemStack);
    p.openInventory(inv);
  }
  
  public static void setItemLore(ItemStack itemStack, String... lore) {
    List<String> loreList = new ArrayList<>();
    ItemMeta meta = itemStack.getItemMeta();
    byte b;
    int i;
    String[] arrayOfString;
    for (i = (arrayOfString = lore).length, b = 0; b < i; ) {
      String lores = arrayOfString[b];
      loreList.add(lores);
      b++;
    } 
    meta.setLore(loreList);
    itemStack.setItemMeta(meta);
  }
}
