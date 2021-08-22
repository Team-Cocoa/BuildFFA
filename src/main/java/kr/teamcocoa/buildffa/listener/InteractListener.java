package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.enums.ItemEnum;
import kr.teamcocoa.buildffa.kit.KitData;
import kr.teamcocoa.buildffa.main.Main;
import kr.teamcocoa.buildffa.utils.Config;
import kr.teamcocoa.buildffa.utils.LangUtils;
import kr.teamcocoa.buildffa.utils.Locations;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractListener implements Listener {
  @EventHandler
  public void onInteract(PlayerInteractEvent e) {
    Player p = e.getPlayer();
    try {
      if (e.getClickedBlock().getType() == Material.STONE_PLATE) {
        return;
      }
    }
    catch(Exception e2){

    }

    if (p.getItemInHand().hasItemMeta() && p.getItemInHand().getItemMeta().getDisplayName() != null) {
      if (p.getItemInHand().getItemMeta().getDisplayName().equals(LangUtils.getMessage(p, ItemEnum.KIT))) {
        p.openInventory(Main.inst().kitData.getKitSelection(p));
      }
      if (p.getItemInHand().getItemMeta().getDisplayName().equals(LangUtils.getMessage(p, ItemEnum.INVENTORY_SORTING))) {
        p.openInventory(Main.inst().kitData.getInventorySorting(p, Main.playerData.get(p).getKit()));
      }
      if (p.getItemInHand().getItemMeta().getDisplayName().equals(LangUtils.getMessage(p, ItemEnum.LEAVE_ITEM))) {
        p.kickPlayer("");
      }
      if (p.getItemInHand().getItemMeta().getDisplayName().equals("§cKillEffects")){
        p.performCommand("killeffect");
      }
      if (p.getLocation().getY() >= Config.locations.getDouble(String.valueOf(Locations.CurrentMapname) + ".arenaheight")) {
        e.setCancelled(true);
      }
    }
  }
}
