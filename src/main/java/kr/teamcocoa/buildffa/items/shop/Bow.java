package kr.teamcocoa.buildffa.items.shop;

import kr.teamcocoa.buildffa.enums.OtherEnum;
import kr.teamcocoa.buildffa.utils.LangUtils;
import kr.teamcocoa.buildffa.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.MessageFormat;

public class Bow extends AbstractExtraItem {

    private static Bow instance;

    private Bow() {
        super.price = 500;
    }

    public static Bow getInstance() {
        if(instance == null) {
            instance = new Bow();
        }
        return instance;
    }

    @Override
    public ItemStack getItemStack(Player player, int count) {
        ItemStack itemStack = new ItemStack(Material.BOW, count);
        return itemStack;
    }

    @Override
    public ItemStack getVoteItemStack(Player player) {
        ItemStack itemStack = getItemStack(player, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(getLore(MessageFormat.format(LangUtils.getMessage(player, OtherEnum.LORE_PRICE), price)));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public String getName(Player player) {
        return StringUtils.color("&cBow");
    }
}
