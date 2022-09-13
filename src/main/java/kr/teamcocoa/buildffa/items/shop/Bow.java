package kr.teamcocoa.buildffa.items.shop;

import kr.teamcocoa.buildffa.enums.OtherEnum;
import kr.teamcocoa.buildffa.kit.BffaPlayer;
import kr.teamcocoa.buildffa.utils.LangUtils;
import kr.teamcocoa.buildffa.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(getName(player));
        itemMeta.setLore(getLore(MessageFormat.format(LangUtils.getMessage(player, OtherEnum.LORE_PRICE), price)));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public String getName(Player player) {
        return StringUtils.color("&cBow");
    }

    public BuyStatus purchase(BffaPlayer player) {
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
