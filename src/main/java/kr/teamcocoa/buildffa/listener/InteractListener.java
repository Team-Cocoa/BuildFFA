package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.enums.InventoryEnum;
import kr.teamcocoa.buildffa.enums.ItemEnum;
import kr.teamcocoa.buildffa.items.extra.RescuePlatform;
import kr.teamcocoa.buildffa.items.shop.ShopInventory;
import kr.teamcocoa.buildffa.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.models.BuildFFAPlayerManager;
import kr.teamcocoa.buildffa.utils.LangUtils;
import kr.teamcocoa.buildffa.world.MapVoteInventory;
import kr.teamcocoa.core.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.concurrent.TimeUnit;

public class InteractListener implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        try {
            if (e.getClickedBlock().getType() == Material.STONE_PRESSURE_PLATE) {
                return;
            }
        }

        catch(Exception e2){

        }

        BuildFFAPlayer buildFFAPlayer = BuildFFAPlayerManager.getPlayer(player);

        if(buildFFAPlayer == null) {
            return;
        }

        if (player.getInventory().getItemInMainHand().hasItemMeta() &&
                player.getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) {
            String displayName = player.getInventory().getItemInMainHand().getItemMeta().getDisplayName();
            if (displayName.equals(LangUtils.getMessage(player, ItemEnum.LEAVE_ITEM))) {
                if(System.currentTimeMillis() - buildFFAPlayer.getLatestDeadTime() > TimeUnit.SECONDS.toMillis(1)) {
                    player.kickPlayer("");
                }
                return;
            }
            if (displayName.equals(LangUtils.getMessage(player, ItemEnum.SHOP))) {
                ShopInventory.openShopInventory(player);
                return;
            }
            if (displayName.equals(LangUtils.getMessage(player, InventoryEnum.VOTE))) {
                player.openInventory(MapVoteInventory.getInstance().getInventory(player));
                return;
            }
            if (displayName.equals(LangUtils.getMessage(player, ItemEnum.RESCUE_PLATFORM))) {
                RescuePlatform.getInstance().onClick(e);
                return;
            }
            if (displayName.equals(StringUtils.color("&cKillEffects"))){
                player.performCommand("killeffect");
                return;
            }
        }
    }
}
