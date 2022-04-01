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

import java.text.MessageFormat;
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

    public boolean setInventorySetting(Player player, ItemStack[] inventorySorting){
        int[] inventory = new int[9];
        String sql = "UPDATE inventory SET sword = {0}, stick = {1}, block = {2}, web = {3}, pearl = {4}, ladder = {5} WHERE uuid = '" + player.getUniqueId().toString() + "';";
        String[] kitString = new String[]{"sword", "stick", "block", "web", "pearl", "ladder"};
        List<ItemStack> inventorySortingList = Arrays.asList(inventorySorting);
        for (int i = 0; i < 6; i++) {
            int index = inventorySortingList.indexOf(Main.inst().kitData.getItemByString(kitString[i]));
            if (index == -1) {
                return false;
            }
            inventory[i] = index;
            sql = sql.replace("{" + i + "}", String.valueOf(inventory[i]));
        }
        Main.inst().mysql.update(sql);
        return true;
    }

    public boolean resetInventorySetting(Player player) {
        try {
            String sql = "INSERT INTO `inventory`(uuid) VALUES (\"" + player.getUniqueId().toString() + "\")" +
                    " ON DUPLICATE KEY " +
                    "UPDATE sword = 0, stick = 1, block = 2, ladder = 6, web = 7, pearl = 8;";
            Main.inst().mysql.update(sql);
            return true;
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }
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
