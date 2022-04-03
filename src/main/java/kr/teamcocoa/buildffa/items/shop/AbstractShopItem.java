package kr.teamcocoa.buildffa.items.shop;

import ch.dkrieger.coinsystem.core.CoinSystem;
import ch.dkrieger.coinsystem.core.player.CoinPlayer;
import kr.teamcocoa.buildffa.kit.BffaPlayer;
import kr.teamcocoa.buildffa.utils.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractShopItem {
    protected int price;

    public int getPrice() {
        return price;
    }

    /**
     *
     * supports auto color
     *
     * @param strings Strings to put in the list
     * @return {@code List<String>} list that contains the strings
     */
    protected List<String> getLore(String... strings) {
        List<String> list = new LinkedList<>();
        for(String string : strings) {
            list.add(StringUtils.color(string));
        }
        return list;
    }

    public abstract ItemStack getItemStack(Player player, int count);
    public abstract ItemStack getVoteItemStack(Player player);
    public abstract String getName(Player player);

    public boolean buyItem(BffaPlayer player) {
        Player p = player.getPlayer();
        CoinPlayer coinPlayer = CoinSystem.getInstance().getPlayerManager().getPlayer(p.getUniqueId());
        long coins = coinPlayer.getCoins();
        if(coins - price < 0) {
            return false;
        }
        coinPlayer.addCoins(price * -1);
        return true;
    }

}
