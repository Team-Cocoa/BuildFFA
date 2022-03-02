package kr.teamcocoa.buildffa.kit;

import kr.teamcocoa.buildffa.enums.InventoryEnum;
import kr.teamcocoa.buildffa.enums.ItemEnum;
import kr.teamcocoa.buildffa.enums.MessageEnum;
import kr.teamcocoa.buildffa.main.Main;
import kr.teamcocoa.buildffa.utils.LangUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static kr.teamcocoa.buildffa.kit.KitData.createItemStack;

public class KitEdit {

    private static KitEdit instance;

    public static KitEdit getInstance() {
        if(instance == null) {
            instance = new KitEdit();
        }
        return instance;

    }

    private KitEdit() {

    }

    public void openInventorySorting(BffaPlayer bffaPlayer) {
        if(bffaPlayer.isInGame()) {
            return;
        }
        Player player = bffaPlayer.getPlayer();
        ItemStack[] kitSorting = bffaPlayer.getInventory();
        ItemStack[] inventories = new ItemStack[36];
        Inventory inventorySorting = Bukkit.createInventory(null, 9, LangUtils.getMessage(player, InventoryEnum.INVENTORY_SORTING));
        for(int i = 0; i < 9; i++){
            inventorySorting.setItem(i, createItemStack(Material.STAINED_GLASS_PANE, " ", 1, new ArrayList(), (byte)7));
            inventories[i] = kitSorting[i];
        }
        inventorySorting.setItem(3, createDye(LangUtils.getMessage(player, ItemEnum.SAVE), Material.INK_SACK, (short)10));
        inventorySorting.setItem(5, createDye(LangUtils.getMessage(player, ItemEnum.RESET), Material.INK_SACK, (short)1));
        for(int i = 9; i < 36; i++) {
            inventories[i] = createItemStack(Material.STAINED_GLASS_PANE, " ", 1, new ArrayList(), (byte)7);
        }
        player.getInventory().setContents(inventories);
        player.openInventory(inventorySorting);
    }

//    public void sorting(BffaPlayer bffaPlayer) {
//        if(bffaPlayer.isInGame()) {
//            return;
//        }
//        Player player = bffaPlayer.getPlayer();
//        try {
//            setInventorySetting(player, player.getInventory().getContents(), Main.inst().kitData.getKitByInt(bffaPlayer.getKit()));
//            player.sendMessage(LangUtils.getMessage(player, MessageEnum.SETTING_SAVED));
//        }
//        catch(Exception e) {
//            e.printStackTrace();
//            player.sendMessage(LangUtils.getMessage(player, MessageEnum.SETTING_ERROR));
//        }
//        finally {
//            player.closeInventory();
//        }
//    }


    public boolean setInventorySetting(Player player, ItemStack[] inventorySorting, String kitName){
        int[] inventory = new int[9];
        String sql;
        String[] kitString;
        List<ItemStack> inventorySortingList = Arrays.asList(inventorySorting);
        switch(kitName){
            case "default":
                kitString = new String[]{"sword", "stick", "block", "web", "pearl", "ladder"};
                sql = "UPDATE kit_default SET sword = {0}, stick = {1}, block = {2}, web = {3}, pearl = {4}, ladder = {5} WHERE uuid = '" + player.getUniqueId().toString() + "';";
                for(int i = 0; i < 6; i++) {
                    inventory[i] = inventorySortingList.indexOf(Main.inst().kitData.getItemByString(kitString[i], "default"));
                    sql = sql.replace("{" + i + "}", String.valueOf(inventory[i]));
                }
                break;
            case "fisher":
                kitString = new String[]{"sword", "stick", "block", "web", "pearl", "ladder", "rod"};
                sql = "UPDATE kit_fisher SET sword = {0}, stick = {1}, block = {2}, web = {3}, pearl = {4}, ladder = {5}, rod = {6} WHERE uuid = '" + player.getUniqueId().toString() + "';";
                for(int i = 0; i < 7; i++){
                    inventory[i] = inventorySortingList.indexOf(Main.inst().kitData.getItemByString(kitString[i], "fisher"));
                    sql = sql.replace("{" + i + "}", String.valueOf(inventory[i]));
                }
                break;
            case "archer":
                kitString = new String[]{"sword", "stick", "block", "web", "pearl", "ladder", "arrow", "bow"};
                sql = "UPDATE `kit_archer` SET sword = {0}, stick = {1}, block = {2}, web = {3}, pearl = {4}, ladder = {5}, arrow = {6}, bow = {7} WHERE uuid = '" + player.getUniqueId().toString() + "';";
                for(int i = 0; i < 8; i++){
                    inventory[i] = inventorySortingList.indexOf(Main.inst().kitData.getItemByString(kitString[i], "archer"));
                    sql = sql.replace("{" + i + "}", String.valueOf(inventory[i]));
                }
                break;
            default:
                return false;
        }
        if(sql.contains("-1")) {
//            setInventorySetting(player, Main.inst().kitData.getDefaultKit(kitName), kitName);
            return false;
        }
        Main.inst().mysql.update(sql);
        return true;
    }

    public ItemStack createDye(String name, Material material, short s) {
        ItemStack itemStack = material.equals(Material.INK_SACK) ? new ItemStack(material, 1, s) : new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public void addItemInInventory(Player player, ItemStack itemStack) {
        player.getInventory().addItem(itemStack);
    }
}
