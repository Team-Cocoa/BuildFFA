package kr.teamcocoa.buildffa.items.shop;

import kr.teamcocoa.buildffa.enums.OtherEnum;
import kr.teamcocoa.buildffa.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.utils.LangUtils;
import kr.teamcocoa.core.bukkit.utils.ItemUtils;
import kr.teamcocoa.core.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.MessageFormat;

public class Bow extends AbstractShopItem {

    private static Bow instance;

    private Bow() {
        super.price = 1000;
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
        itemStack.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
        return itemStack;
    }

    @Override
    public ItemStack getVoteItemStack(Player player) {
        ItemStack itemStack = getItemStack(player, 1);
        ItemUtils.name(itemStack, getName(player));
        ItemUtils.lore(itemStack, getLore(MessageFormat.format(LangUtils.getMessage(player, OtherEnum.LORE_PRICE), price)));
        return itemStack;
    }

    @Override
    public String getName(Player player) {
        return StringUtils.color("&cBow");
    }

    public BuyStatus purchase(BuildFFAPlayer player) {
        if(player.isBowBought()) {
            return BuyStatus.ALREADY_BOUGHT;
        }
        if(super.buyItem(player)) {
            player.setBowBought(true);
            return BuyStatus.SUCCESS;
        }
        else {
            return BuyStatus.FAILED;
        }
    }
}
