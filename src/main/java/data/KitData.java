package data;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import utils.MYSQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class KitData {

    private static ItemStack createItemStack(Material material, String name, int amount, ArrayList lore, byte data) {
        ItemStack itemStack = new ItemStack(material, amount, (short)data);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);
        itemMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES });
        itemMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_UNBREAKABLE });
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static Inventory getKitSelection(){
        Inventory kitSelection = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', "&cKit Selection"));
        for(int i = 0; i < 11; i++){
            kitSelection.setItem(i, createItemStack(Material.STAINED_GLASS_PANE, " ", 1, new ArrayList(), (byte)7));
        }
        for(int i = 11; i < 17; i += 2){
            kitSelection.setItem(i, getSymbol(i - 11));
            kitSelection.setItem(i + 1, createItemStack(Material.STAINED_GLASS_PANE, " ", 1, new ArrayList(), (byte)7));
        }
        for(int i = 17; i < 27; i++){
            kitSelection.setItem(i, createItemStack(Material.STAINED_GLASS_PANE, " ", 1, new ArrayList(), (byte)7));
        }
        return kitSelection;
    }

    public static void setKit(Player player, int kit){
        try{
            MYSQL.update("UPDATE `stats` SET `kit` = \"" + kit + "\" WHERE `UUID` = \"" + player.getUniqueId() + "\";");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static int getKit(Player player){
        int i = -1;
        try{
            ResultSet rs = MYSQL.getResult("SELECT `kit` FROM `stats` WHERE `UUID` = \""+player.getUniqueId()+"\";");
            if(rs.next()){
                i = rs.getInt("kit");
            }
            return i;
        }
        catch(SQLException e){
            e.printStackTrace();
            return -1;
        }
    }
    public static String getKitByInt(int input){
        String output;
        switch(input){
            case 0:
                output = "default";
                break;
            case 1:
                output = "fisher";
                break;
            case 2:
                output = "archer";
                break;
            default:
                output = "error";
                break;
        }
        return output;
    }

    public static int getKitByString(String input){
        int output;
        switch(input){
            case "default":
                output = 0;
                break;
            case "fisher":
                output = 1;
                break;
            case "archer":
                output = 2;
                break;
            default:
                output = -1;
                break;
        }
        return output;
    }

    public static ItemStack getSymbol(int input){
        switch(input){
            case 0:
                ItemStack item = new ItemStack(Material.STICK);
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&bDefault"));
                itemMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);
                item.setItemMeta(itemMeta);
                return item;
            case 1:
                ItemStack item1 = new ItemStack(Material.FISHING_ROD);
                ItemMeta itemMeta1 = item1.getItemMeta();
                itemMeta1.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&bFisher"));
                item1.setItemMeta(itemMeta1);
                return item1;
            case 2:
                ItemStack item2 = new ItemStack(Material.BOW);
                ItemMeta itemMeta2 = item2.getItemMeta();
                itemMeta2.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&bArcher"));
                item2.setItemMeta(itemMeta2);
                return item2;
            default:
                ItemStack item3 = new ItemStack(Material.AIR);
                return item3;
        }
    }

    private static ItemStack getGoldenSword(String kit){
        ItemStack goldenSword = new ItemStack(Material.GOLD_SWORD);
        ItemMeta goldenSwordMeta = goldenSword.getItemMeta();
        goldenSwordMeta.addEnchant(Enchantment.DURABILITY, 5, true);
        if(kit.equals("default")) {
            goldenSwordMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
        }
        goldenSword.setItemMeta(goldenSwordMeta);
        return goldenSword;
    }

    private static ItemStack getKbStick(){
        ItemStack kbStick = new ItemStack(Material.STICK);
        ItemMeta kbStickMeta = kbStick.getItemMeta();
        kbStickMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);
        kbStick.setItemMeta(kbStickMeta);
        return kbStick;
    }

    private static ItemStack getRod(){
        ItemStack rod = new ItemStack(Material.FISHING_ROD);
        ItemMeta rodMeta = rod.getItemMeta();
        ((Damageable) rodMeta).damage(32);
        rod.setItemMeta(rodMeta);
        return rod;
    }

    private static ItemStack getLadder(){
        ItemStack ladder = new ItemStack(Material.LADDER, 5);
        return ladder;
    }

    private static ItemStack getPearl(){
        ItemStack pearl = new ItemStack(Material.ENDER_PEARL, 2);
        return pearl;
    }

    private static ItemStack getWeb(){
        ItemStack web = new ItemStack(Material.WEB, 3);
        return web;
    }

    private static ItemStack getBlock(){
        ItemStack block = new ItemStack(Material.SANDSTONE, 64);
        return block;
    }

    private static ItemStack getArrow(){
        ItemStack arrow = new ItemStack(Material.ARROW, 7);
        return arrow;
    }

    private static ItemStack getBow(){
        ItemStack bow = new ItemStack(Material.BOW);
        return bow;
    }

    private static ItemStack getNull(){
        ItemStack air = new ItemStack(Material.AIR);
        return air;
    }

    public static ItemStack[] getDefaultKit(String kit){
        ItemStack[] kitList = new ItemStack[9];
        kitList[1] = getKbStick();
        kitList[2] = getBlock();
        kitList[6] = getLadder();
        kitList[7] = getWeb();
        kitList[8] = getPearl();
        switch(kit){
            case "default":
                kitList[0] = getGoldenSword("default");
                for(int i = 3; i < 6; i++){
                    kitList[i] = getNull();
                }
                break;
            case "fisher":
                kitList[0] = getGoldenSword("fisher");
                kitList[3] = getRod();
                for(int i = 4; i < 6; i++){
                    kitList[i] = getNull();
                }
                break;
            case "archer":
                kitList[0] = getGoldenSword("archer");
                kitList[3] = getBow();
                kitList[4] = getNull();
                kitList[5] = getArrow();
                break;
            default:
                break;
        }
        return kitList;
    }

    public static ItemStack[] getArmor(){
        ItemStack[] armorList = new ItemStack[4];
        ItemMeta[] armorMetaList = new ItemMeta[4];
        armorList[0] = new ItemStack(Material.LEATHER_BOOTS);
        armorList[1] = new ItemStack(Material.LEATHER_LEGGINGS);
        armorList[2] = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        armorList[3] = new ItemStack(Material.LEATHER_HELMET);
        for(int i = 0; i < 4; i++){
            armorMetaList[i] = armorList[i].getItemMeta();
            armorMetaList[i].addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true);
            armorMetaList[i].addEnchant(Enchantment.DURABILITY, 2, true);
        }
        return armorList;
    }
}
