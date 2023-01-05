package kr.teamcocoa.buildffa.gui;

import kr.teamcocoa.buildffa.translate.InventoryNode;
import kr.teamcocoa.buildffa.translate.ItemNode;
import kr.teamcocoa.buildffa.translate.MessageNode;
import kr.teamcocoa.buildffa.translate.OtherNode;
import kr.teamcocoa.buildffa.model.BuildFFAInventory;
import kr.teamcocoa.buildffa.utils.ItemManager;
import kr.teamcocoa.buildffa.utils.LangUtils;
import kr.teamcocoa.buildffa.utils.StringUtils;
import kr.teamcocoa.buildffa.world.MapVote;
import kr.teamcocoa.buildffa.world.WorldManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MapVoteInventory extends AbstractGUI {

    public MapVoteInventory() {
        inventory = Bukkit.createInventory(null, 3 * 9);
        name = InventoryNode.VOTE;
        fillInventory();
    }

    @Override
    public void openInventory(Player player) {
        Inventory newInventory = Bukkit.createInventory(null, 3 * 9, LangUtils.getMessage(player, name));
        newInventory.setContents(inventory.getContents());
        MapVote mapVote = MapVote.getInstance();
        int i = 10;
        for(String string : mapVote.getMapList()) {
            newInventory.setItem(i, getVoteItem(player, string));
            i = i + 2;
        }
        newInventory.setItem(26, getCancelItem(player));
        player.openInventory(newInventory);
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        try {
            Player player = ((Player) e.getWhoClicked());
            if(!checkItemStack(e) || !checkThisInventoryClicked(e)) {
                return;
            }

            e.setCancelled(true);

            ItemStack itemStack = e.getCurrentItem();
            String clicked = itemStack.getItemMeta().getDisplayName().replace("§e§l", "");

            MapVote mapVote = MapVote.getInstance();

            if(!MapVote.getInstance().isVoteAble()) {
                player.closeInventory();

                player.playSound(player.getLocation(), Sound.NOTE_BASS, 100F, 0F);
                player.sendMessage(LangUtils.getMessage(player, MessageNode.VOTE_CANNOT_VOTE));
                return;
            }
            if(mapVote.getMapList().contains(clicked)) {
                if(WorldManager.getInstance().getCurrentMap().equals(clicked)) {
                    player.sendMessage(LangUtils.getMessage(player, MessageNode.VOTE_CANNOT_VOTE_MAP));
                    return;
                }
                mapVote.addVote(player, clicked);
                player.closeInventory();
                player.sendMessage(LangUtils.getMessage(player, MessageNode.VOTE_SUCCESS).replace("%map%", clicked));
            }
            if(itemStack.getItemMeta().getDisplayName().equals(LangUtils.getMessage(player, ItemNode.CANCEL_VOTE))) {
                if(mapVote.getWherePlayerVoted(player) != null) {
                    mapVote.removeVote(player, mapVote.getWherePlayerVoted(player));
                    player.closeInventory();
                    player.sendMessage(LangUtils.getMessage(player, MessageNode.VOTE_RESET));
                }
                else {
                    player.sendMessage(LangUtils.getMessage(player, MessageNode.VOTE_INVALID));
                }
            }
        }
        catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onClose(InventoryCloseEvent e) {

    }

    private ItemStack getVoteItem(Player player, String name) {
        MapVote mapVote = MapVote.getInstance();
        if(mapVote.getMapList().contains(name)) {
            String votes = String.valueOf(mapVote.getVote(name));
            List<String> lore = new ArrayList<>();
            lore.add(LangUtils.getMessage(player, OtherNode.LORE_VOTE_COUNT).replace("%int%", votes));
            return ItemManager.createItemStack(Material.PAPER, "&e&l" + name, 1, lore, (byte)0);
        }
        return null;
    }

    private ItemStack getCancelItem(Player player) {
        ItemStack itemStack = new ItemStack(Material.BARRIER);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(LangUtils.getMessage(player, ItemNode.CANCEL_VOTE));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
