package kr.teamcocoa.buildffa.items.shop;

import kr.teamcocoa.buildffa.enums.OtherEnum;
import kr.teamcocoa.buildffa.kit.BffaPlayer;
import kr.teamcocoa.buildffa.utils.LangUtils;
import kr.teamcocoa.buildffa.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.text.MessageFormat;

public class GoldenApple extends AbstractShopItem {

    private static GoldenApple instance;

    private GoldenApple() {
        super.price = 500;
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
        itemMeta.setDisplayName(getName(player));
        itemMeta.setLore(getLore(MessageFormat.format(LangUtils.getMessage(player, OtherEnum.LORE_PRICE), price)));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public String getName(Player player) {
        return StringUtils.color("&cGolden Apple");
    }

    public BuyStatus purchase(BffaPlayer player) {
        if(player.isGappleBought()) {
            return BuyStatus.ALREADY_BOUGHT;
        }
        if(super.buyItem(player)) {
            player.setGappleBought(true);
            return BuyStatus.SUCCESS;
        }
        else {
            return BuyStatus.FAILED;
        }
    }
}
