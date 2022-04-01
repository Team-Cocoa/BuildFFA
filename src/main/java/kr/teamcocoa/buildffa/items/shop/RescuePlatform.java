package kr.teamcocoa.buildffa.items.shop;

import kr.teamcocoa.buildffa.enums.ItemEnum;
import kr.teamcocoa.buildffa.enums.OtherEnum;
import kr.teamcocoa.buildffa.utils.LangUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.MessageFormat;

public class RescuePlatform extends AbstractExtraItem {

    private static RescuePlatform instance;

    private RescuePlatform() {
        super.price = 500;
    }

    public static RescuePlatform getInstance() {
        if(instance == null) {
            instance = new RescuePlatform();
        }
        return instance;
    }

    @Override
    public ItemStack getItemStack(Player player, int count) {
        ItemStack itemStack = new ItemStack(Material.BLAZE_ROD, count);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(LangUtils.getMessage(player, ItemEnum.RESCUE_PLATFORM));
        itemStack.setItemMeta(itemMeta);
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
        return LangUtils.getMessage(player, ItemEnum.RESCUE_PLATFORM);
    }
}
