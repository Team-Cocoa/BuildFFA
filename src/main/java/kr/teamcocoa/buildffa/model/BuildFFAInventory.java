package kr.teamcocoa.buildffa.model;

import kr.teamcocoa.buildffa.utils.ItemManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

@Getter
public class BuildFFAInventory {

    @Getter
    private final static ItemStack goldenSword;

    @Getter
    private final static ItemStack kbStick;

    @Getter
    private final static ItemStack pearl;

    @Getter
    private final static ItemStack web;

    @Getter
    private final static ItemStack block;

    @Getter
    private final static ItemStack air;

    @Getter
    private final static ItemStack[] armors;

    private BuildFFAPlayer buildFFAPlayer;

    @Setter
    private int swordIndex;

    @Setter
    private int stickIndex;

    @Setter
    private int blockIndex;

    @Setter
    private int webIndex;

    @Setter
    private int pearlIndex;

    static {
        goldenSword = ItemManager.hideAttributes(
                ItemManager.addEnchant(new ItemStack(Material.GOLD_SWORD), Enchantment.DURABILITY, 5));
        kbStick = ItemManager.hideAttributes(
                ItemManager.addEnchant(new ItemStack(Material.STICK), Enchantment.KNOCKBACK, 1));
        pearl = new ItemStack(Material.ENDER_PEARL, 2);
        web = new ItemStack(Material.WEB, 5);
        block = new ItemStack(Material.SANDSTONE, 32);
        air = new ItemStack(Material.AIR);
        armors = new ItemStack[4];
        armors[0] = new ItemStack(Material.LEATHER_BOOTS);
        armors[1] = new ItemStack(Material.LEATHER_LEGGINGS);
        armors[2] = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        armors[3] = new ItemStack(Material.LEATHER_HELMET);
        for (ItemStack itemStack : armors) {
            ItemManager.addEnchant(itemStack, Enchantment.PROTECTION_ENVIRONMENTAL, 2);
            ItemManager.addEnchant(itemStack, Enchantment.DURABILITY, 2);
            ItemManager.hideAttributes(itemStack);
        }
    }

    public BuildFFAInventory(BuildFFAPlayer buildFFAPlayer) {
        this.buildFFAPlayer = buildFFAPlayer;
    }

    public static void setDefaultKit(BuildFFAInventory buildFFAInventory){
        synchronized (buildFFAInventory) {
            buildFFAInventory.setSwordIndex(0);
            buildFFAInventory.setStickIndex(1);
            buildFFAInventory.setBlockIndex(2);
            buildFFAInventory.setWebIndex(7);
            buildFFAInventory.setPearlIndex(8);
        }
    }

    public static ItemStack getItemByString(String string){
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
            case "pearl":
                item = getPearl();
                break;
            default:
                item = getAir();
                break;
        }
        return item;
    }

    public static boolean setKit(BuildFFAInventory buildFFAInventory, ItemStack[] itemStacks) {
        List<ItemStack> list = Arrays.asList(itemStacks);
        int swordIndex = list.indexOf(goldenSword);
        int stickIndex = list.indexOf(kbStick);
        int pearlIndex = list.indexOf(pearl);
        int webIndex = list.indexOf(web);
        int blockIndex = list.indexOf(block);

        if(swordIndex == -1 || stickIndex == -1 || pearlIndex == -1 || webIndex == -1 || blockIndex == -1) {
            return false;
        }

        synchronized (buildFFAInventory) {
            buildFFAInventory.setSwordIndex(swordIndex);
            buildFFAInventory.setStickIndex(stickIndex);
            buildFFAInventory.setPearlIndex(pearlIndex);
            buildFFAInventory.setWebIndex(webIndex);
            buildFFAInventory.setBlockIndex(blockIndex);
        }

        return true;
    }

}
