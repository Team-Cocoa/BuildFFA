package kr.teamcocoa.buildffa.items.shop;

import kr.teamcocoa.buildffa.enums.OtherEnum;
import kr.teamcocoa.buildffa.utils.LangUtils;
import kr.teamcocoa.buildffa.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.MessageFormat;

public class SnowBall extends AbstractExtraItem {
    private static SnowBall instance;

    private SnowBall() {
        super.price = 500;
    }

    public static SnowBall getInstance() {
        if(instance == null) {
            instance = new SnowBall();
        }
        return instance;
    }

    @Override
    public ItemStack getItemStack(Player player, int count) {
        ItemStack itemStack = new ItemStack(Material.SNOW_BALL, count);
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
        return StringUtils.color("&cSnowBall");
    }
}
