package kr.teamcocoa.buildffa.items;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import kr.teamcocoa.buildffa.enums.ItemEnum;
import kr.teamcocoa.buildffa.enums.OtherEnum;
import kr.teamcocoa.buildffa.kit.BffaPlayer;
import kr.teamcocoa.buildffa.utils.LangUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.UUID;

public class GoldenHead extends AbstractExtraItem {

    private static GoldenHead instance;

    private GoldenHead() {
        super.price = 250;
    }

    public static GoldenHead getInstance() {
        if(instance == null) {
            instance = new GoldenHead();
        }
        return instance;
    }

    @Override
    public ItemStack getItemStack(Player player, int count) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, count, (short)3);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        profile.getProperties().put("textures", new Property("textures", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmRhZWJkY2U4Y2YzNWJhOWJkYjkwNzA1NjM1MDU4NzUyMDMwYzBiZTRlNjU4OTg3Yjc1YzhhZTY1MzMwMWMwOCJ9fX0="));
        try {
            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        meta.setDisplayName(LangUtils.getMessage(player, ItemEnum.GOLDEN_HEAD));
        head.setItemMeta(meta);
        return head;
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

    @Override
    public boolean buyItem(BffaPlayer player) {
        Player p = player.getPlayer();
        CoinPlayer coinPlayer = CoinSystem.
    }
}
