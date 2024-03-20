package kr.teamcocoa.buildffa.world;

import kr.teamcocoa.buildffa.enums.InventoryEnum;
import kr.teamcocoa.buildffa.enums.ItemEnum;
import kr.teamcocoa.buildffa.enums.MessageEnum;
import kr.teamcocoa.buildffa.enums.OtherEnum;
import kr.teamcocoa.buildffa.utils.LangUtils;
import kr.teamcocoa.core.bukkit.utils.ComponentUtils;
import kr.teamcocoa.core.bukkit.utils.ItemUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MapVoteInventory {
    private static MapVoteInventory instance = null;


    public static MapVoteInventory getInstance() {
        if(instance == null) {
            instance = new MapVoteInventory();
            return instance;
        }
        return instance;
    }

    public MapVoteInventory() {

    }

    public Inventory getInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 3 * 9, Component.text(LangUtils.getMessage(player, InventoryEnum.VOTE)));
        fillItemInInventory(inventory);
        MapVote mapVote = MapVote.getInstance();
        int i = 10;
        for(String string : mapVote.getMapList()) {
            inventory.setItem(i, getVoteItem(player, string));
            i = i + 2;
        }
        inventory.setItem(26, getCancelItem(player));
        return inventory;
    }

    private ItemStack getVoteItem(Player player, String name) {
        MapVote mapVote = MapVote.getInstance();
        if(mapVote.getMapList().contains(name)) {
            String votes = String.valueOf(mapVote.getVote(name));

            List<String> lore = new ArrayList<>();
            lore.add(LangUtils.getMessage(player, OtherEnum.LORE_VOTE_COUNT).replace("%int%", votes));

            ItemStack itemStack = new ItemStack(Material.PAPER);
            ItemUtils.name(itemStack,"&e&l" + name);
            ItemUtils.lore(itemStack, lore);
            return itemStack;
        }
        return null;
    }

    private ItemStack getCancelItem(Player player) {
        ItemStack itemStack = new ItemStack(Material.BARRIER);
        ItemUtils.name(itemStack, LangUtils.getMessage(player, ItemEnum.CANCEL_VOTE));
        return itemStack;
    }

    private void fillItemInInventory(Inventory inventory) {
        for(int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, ItemUtils.getGUIBackGround());
        }
    }

    public void onClickInventory(InventoryClickEvent e) {
        try {
            Inventory inventory = e.getClickedInventory();
            ItemStack itemStack = e.getCurrentItem();
            ItemMeta itemMeta = itemStack.getItemMeta();
            if(!(e.getWhoClicked() instanceof Player)) {
                return;
            }
            Player player = (Player) e.getWhoClicked();

            if(!ComponentUtils.componentEquals(e.getView().title(), LangUtils.getMessage(player, InventoryEnum.VOTE))) {
                return;
            }

            e.setCancelled(true);

            MapVote mapVote = MapVote.getInstance();

            String clicked = ((TextComponent) itemMeta.displayName()).content().substring(4);

            if(!MapVote.getInstance().isVoteAble()) {
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100F, 0F);
                player.sendMessage(LangUtils.getMessage(player, MessageEnum.VOTE_CANNOT_VOTE));
                return;
            }

            if(mapVote.getMapList().contains(clicked)) {
                if(WorldManager.getInstance().getCurrentMap().equals(clicked)) {
                    player.sendMessage(LangUtils.getMessage(player, MessageEnum.VOTE_CANNOT_VOTE_MAP));
                    return;
                }
                mapVote.addVote(player, clicked);
                player.closeInventory();
                player.sendMessage(LangUtils.getMessage(player, MessageEnum.VOTE_SUCCESS).replace("%map%", clicked));
            }

            if(ComponentUtils.componentEquals(itemMeta.displayName(), LangUtils.getMessage(player, ItemEnum.CANCEL_VOTE))) {
                if(mapVote.getWherePlayerVoted(player) != null) {
                    mapVote.removeVote(player, mapVote.getWherePlayerVoted(player));
                    player.closeInventory();
                    player.sendMessage(LangUtils.getMessage(player, MessageEnum.VOTE_RESET));
                }
                else {
                    player.sendMessage(LangUtils.getMessage(player, MessageEnum.VOTE_INVALID));
                }
            }
        }
        catch(Exception e1) {
            if(e1 instanceof NullPointerException) {
                return;
            }
            else {
                e1.printStackTrace();
            }
        }
    }

}
