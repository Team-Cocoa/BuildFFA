package kr.teamcocoa.buildffa.items.shop;

import kr.teamcocoa.buildffa.enums.OtherEnum;
import kr.teamcocoa.buildffa.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.utils.LangUtils;
import kr.teamcocoa.core.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.text.MessageFormat;

public class SnowBall extends AbstractShopItem {

    private static SnowBall instance;

    private SnowBall() {
        super.price = 200;
    }

    public static SnowBall getInstance() {
        if(instance == null) {
            instance = new SnowBall();
        }
        return instance;
    }

    @Override
    public ItemStack getItemStack(Player player, int count) {
        ItemStack itemStack = new ItemStack(Material.SNOWBALL, count);
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
        return StringUtils.color("&cSnowBall");
    }

    public BuyStatus purchase(BuildFFAPlayer player) {
        if(player.isSnowBallBought()) {
            return BuyStatus.ALREADY_BOUGHT;
        }
        if(super.buyItem(player)) {
            player.setSnowBallBought(true);
            return BuyStatus.SUCCESS;
        }
        else {
            return BuyStatus.FAILED;
        }
    }
}
