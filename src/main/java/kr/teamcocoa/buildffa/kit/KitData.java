package kr.teamcocoa.buildffa.kit;

import kr.teamcocoa.buildffa.main.Main;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class KitData {

    public ItemStack[] getPlayerKit(Player player){
        ItemStack[] inventory = new ItemStack[9];
        String sql = "SELECT * FROM `inventory` WHERE `uuid` = \"" + player.getUniqueId().toString() + "\";";
        String[] kitString = new String[]{"sword", "stick", "block", "web", "pearl", "ladder"};
        try(    PreparedStatement preparedStatement = Main.getInstance().mysql.getPreparedStatement(sql);
                ResultSet rs = preparedStatement.executeQuery()) {
            if (rs.next()) {
                for (int i = 0; i < kitString.length; i++) {
                    inventory[rs.getInt(kitString[i])] = getItemByString(kitString[i]);
                }
            }
            else {
                KitEdit.getInstance().resetInventorySetting(player);
                inventory = getDefaultKit();
            }
        }
        catch(Exception e){
            e.printStackTrace();
            KitEdit.getInstance().resetInventorySetting(player);
            inventory = getDefaultKit();
        }
        return inventory;
    }

    public ItemStack[] getDefaultKit(){
        ItemStack[] kitList = new ItemStack[9];
        kitList[0] = getGoldenSword();
        kitList[1] = getKbStick();
        kitList[2] = getBlock();
        kitList[6] = getLadder();
        kitList[7] = getWeb();
        kitList[8] = getPearl();
        return kitList;
    }

    public ItemStack[] getArmor(){
        ItemStack[] armorList = new ItemStack[4];
        ItemMeta[] armorMetaList = new ItemMeta[4];
        armorList[0] = getUnbreakable(Material.LEATHER_BOOTS);
        armorList[1] = getUnbreakable(Material.LEATHER_LEGGINGS);
        armorList[2] = getUnbreakable(Material.CHAINMAIL_CHESTPLATE);
        armorList[3] = getUnbreakable(Material.LEATHER_HELMET);
        for(int i = 0; i < 4; i++){
            armorMetaList[i] = armorList[i].getItemMeta();
            armorMetaList[i].addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true);
            armorMetaList[i].addEnchant(Enchantment.DURABILITY, 2, true);
            armorList[i].setItemMeta(armorMetaList[i]);
        }
        return armorList;
    }

    public ItemStack getItemByString(String string){
        ItemStack item;
        switch(string){
            case "sword":
                item = getGoldenSword();
                break;
            case "stick":
                item = getKbStick();
                break;
            case "block":
                item = getBlock();
                break;
            case "web":
                item = getWeb();
                break;
            case "ladder":
                item = getLadder();
                break;
            case "pearl":
                item = getPearl();
                break;
            case "rod":
                item = getRod();
                break;
            case "bow":
                item = getBow();
                break;
            case "arrow":
                item = getArrow();
                break;
            default:
                item = getNull();
                break;
        }
        return item;
    }

    private ItemStack getGoldenSword(){
        ItemStack goldenSword = getUnbreakable(Material.GOLD_SWORD);
        ItemMeta goldenSwordMeta = goldenSword.getItemMeta();
        goldenSwordMeta.addEnchant(Enchantment.DURABILITY, 5, true);
        goldenSword.setItemMeta(goldenSwordMeta);
        return goldenSword;
    }

    private ItemStack getKbStick(){
        ItemStack kbStick = new ItemStack(Material.STICK);
        ItemMeta kbStickMeta = kbStick.getItemMeta();
        kbStickMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);
        kbStick.setItemMeta(kbStickMeta);
        return kbStick;
    }

    private ItemStack getRod(){
        ItemStack rod = new ItemStack(Material.FISHING_ROD);
//        ItemMeta rodMeta = rod.getItemMeta();
        rod.setDurability((short)40);
//        ((Damageable) rodMeta).damage(32);
//        rod.setItemMeta(rodMeta);
        return rod;
    }

    private ItemStack getLadder(){
        ItemStack ladder = new ItemStack(Material.LADDER, 5);
        return ladder;
    }

    private ItemStack getPearl(){
        ItemStack pearl = new ItemStack(Material.ENDER_PEARL, 2);
        return pearl;
    }

    private ItemStack getWeb(){
        ItemStack web = new ItemStack(Material.WEB, 3);
        return web;
    }

    private ItemStack getBlock(){
        ItemStack block = new ItemStack(Material.SANDSTONE, 64);
        return block;
    }

    private ItemStack getArrow(){
        ItemStack arrow = new ItemStack(Material.ARROW, 16);
        return arrow;
    }

    private ItemStack getBow(){
        ItemStack bow = new ItemStack(Material.BOW);
        return bow;
    }

    private ItemStack getNull(){
        ItemStack air = new ItemStack(Material.AIR);
        return air;
    }

    public static ItemStack createItemStack(Material material, String name, int amount, ArrayList lore, byte data) {
        ItemStack itemStack = new ItemStack(material, amount, (short)data);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);
        itemMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES });
        itemMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_UNBREAKABLE });
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack createDye(String name, Material material, short s) {
        ItemStack itemStack = material.equals(Material.INK_SACK) ? new ItemStack(material, 1, s) : new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack getUnbreakable(Material material){
        ItemStack itemStack = new ItemStack(material);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("Unbreakable", true);
        net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy(itemStack);
        stack.setTag(tag);
        itemStack = CraftItemStack.asCraftMirror(stack);
        return itemStack;
    }
}
