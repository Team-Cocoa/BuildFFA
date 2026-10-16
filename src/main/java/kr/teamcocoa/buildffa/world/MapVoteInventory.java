package kr.teamcocoa.buildffa.world;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import kr.teamcocoa.buildffa.translation.InventoryEnum;
import kr.teamcocoa.buildffa.translation.ItemEnum;
import kr.teamcocoa.buildffa.translation.LangUtils;
import kr.teamcocoa.buildffa.translation.MessageEnum;
import kr.teamcocoa.buildffa.translation.OtherEnum;
import kr.teamcocoa.buildffa.world.maps.Maps;
import kr.teamcocoa.core.bukkit.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Singleton
public class MapVoteInventory {

  private final MapVote mapVote;

  private final WorldManager worldManager;

  @Inject
  public MapVoteInventory(MapVote mapVote, WorldManager worldManager) {
    this.mapVote = mapVote;
    this.worldManager = worldManager;
  }

  public Inventory getInventory(Player player) {
    Inventory inventory =
        Bukkit.createInventory(null, 3 * 9, LangUtils.getMessage(player, InventoryEnum.VOTE));
    fillItemInInventory(inventory);
    int i = 10;
    for (Maps maps : mapVote.getMapList()) {
      inventory.setItem(i, getVoteItem(player, maps));
      i = i + 2;
    }
    inventory.setItem(26, getCancelItem(player));
    return inventory;
  }

  private ItemStack getVoteItem(Player player, Maps maps) {
    List<String> lore = new ArrayList<>();
    lore.add(
        MessageFormat.format(
            LangUtils.getMessage(player, OtherEnum.LORE_VOTE_COUNT), mapVote.getVote(maps)));

    ItemStack itemStack = new ItemStack(Material.PAPER);
    ItemUtils.name(itemStack, "&e&l" + maps.getName());
    ItemUtils.lore(itemStack, lore);
    return itemStack;
  }

  private ItemStack getCancelItem(Player player) {
    ItemStack itemStack = new ItemStack(Material.BARRIER);
    ItemUtils.name(itemStack, LangUtils.getMessage(player, ItemEnum.CANCEL_VOTE));
    return itemStack;
  }

  private void fillItemInInventory(Inventory inventory) {
    for (int i = 0; i < inventory.getSize(); i++) {
      inventory.setItem(i, ItemUtils.getGUIBackGround());
    }
  }

  public void onClickInventory(InventoryClickEvent e) {
    try {
      ItemStack itemStack = e.getCurrentItem();
      ItemMeta itemMeta = itemStack.getItemMeta();
      if (!(e.getWhoClicked() instanceof Player)) {
        return;
      }
      Player player = (Player) e.getWhoClicked();

      if (!e.getView().getTitle().equals(LangUtils.getMessage(player, InventoryEnum.VOTE))) {
        return;
      }

      e.setCancelled(true);

      if (!mapVote.isVoteAble()) {
        player.closeInventory();
        player.playSound(player.getLocation(), Sound.NOTE_BASS, 100F, 0F);
        player.sendMessage(LangUtils.getMessage(player, MessageEnum.VOTE_CANNOT_VOTE));
        return;
      }

      if (itemMeta.getDisplayName().equals(LangUtils.getMessage(player, ItemEnum.CANCEL_VOTE))) {
        if (mapVote.getWhatPlayerVoted(player) != null) {
          mapVote.removeVote(player, mapVote.getWhatPlayerVoted(player));
          player.closeInventory();
          player.sendMessage(LangUtils.getMessage(player, MessageEnum.VOTE_RESET));
        } else {
          player.sendMessage(LangUtils.getMessage(player, MessageEnum.VOTE_INVALID));
        }
        return;
      }

      String clicked = itemMeta.getDisplayName().substring(4);

      Maps votedMap = Maps.getByName(clicked);

      if (worldManager.getCurrentMap().getMaps() == votedMap) {
        player.sendMessage(LangUtils.getMessage(player, MessageEnum.VOTE_CANNOT_VOTE_MAP));
      } else {
        mapVote.addVote(player, votedMap);
        player.closeInventory();
        player.sendMessage(
            MessageFormat.format(
                LangUtils.getMessage(player, MessageEnum.VOTE_SUCCESS), votedMap.getName()));
      }

    } catch (Exception e1) {
      if (e1 instanceof NullPointerException) {
        return;
      } else {
        e1.printStackTrace();
      }
    }
  }
}
