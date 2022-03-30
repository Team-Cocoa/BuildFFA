package kr.teamcocoa.buildffa.items;

import kr.teamcocoa.buildffa.enums.ItemEnum;
import kr.teamcocoa.buildffa.enums.OtherEnum;
import kr.teamcocoa.buildffa.utils.LangUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.text.MessageFormat;

public class GoldenApple extends AbstractExtraItem {

    private static GoldenApple instance;

    private GoldenApple() {
        super.price = 250;
    }

    public static GoldenApple getInstance() {
        if(instance == null) {
            instance = new GoldenApple();
        }
        return instance;
    }

    @Override
    public ItemStack getItemStack(Player player, int count) {
        ItemStack itemStack = new ItemStack(Material.GOLDEN_APPLE, count);
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
        return LangUtils.getMessage(player, ItemEnum.GOLDEN_HEAD);
    }
}
