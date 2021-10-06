package kr.teamcocoa.buildffa.world;

import kr.teamcocoa.buildffa.enums.InventoryEnum;
import kr.teamcocoa.buildffa.enums.ItemEnum;
import kr.teamcocoa.buildffa.enums.MessageEnum;
import kr.teamcocoa.buildffa.enums.OtherEnum;
import kr.teamcocoa.buildffa.kit.KitData;
import kr.teamcocoa.buildffa.utils.LangUtils;
import kr.teamcocoa.buildffa.utils.StringUtils;
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
        Inventory inventory = Bukkit.createInventory(null, 3 * 9, LangUtils.getMessage(player, InventoryEnum.VOTE));
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
            ItemStack itemStack = new ItemStack(Material.PAPER);
            ItemMeta itemMeta = itemStack.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add(LangUtils.getMessage(player, OtherEnum.LORE_VOTE_COUNT).replace("%int%", votes));
            itemMeta.setLore(lore);
            itemMeta.setDisplayName(StringUtils.color("&e&l" + name));
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
        return null;
    }

    private ItemStack getCancelItem(Player player) {
        ItemStack itemStack = new ItemStack(Material.BARRIER);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(LangUtils.getMessage(player, ItemEnum.CANCEL_VOTE));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private void fillItemInInventory(Inventory inventory) {
        for(int i = 0; i < 27; i++) {
            inventory.setItem(i, KitData.createItemStack(Material.STAINED_GLASS_PANE, " ", 1, new ArrayList(), (byte)7));
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
            if(!inventory.getName().equals(LangUtils.getMessage(player, InventoryEnum.VOTE))) {
                return;
            }
            MapVote mapVote = MapVote.getInstance();
            String clicked = itemMeta.getDisplayName().replace("§e§l", "");
            e.setCancelled(true);
            if(!MapVote.getInstance().isVoteAble()) {
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.NOTE_BASS, 100F, 0F);
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
            if(itemMeta.getDisplayName().equals(LangUtils.getMessage(player, ItemEnum.CANCEL_VOTE))) {
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
