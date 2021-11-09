package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.enums.MessageEnum;
import kr.teamcocoa.buildffa.kit.BffaPlayer;
import kr.teamcocoa.buildffa.main.Main;
import kr.teamcocoa.buildffa.utils.BowCountDown;
import kr.teamcocoa.buildffa.utils.LangUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

import java.util.HashMap;

public class EntityShootBowListener implements Listener {

    public static HashMap<BffaPlayer, Integer> bowHashMap = new HashMap<>();

    @EventHandler
    public void onShoot(EntityShootBowEvent e) {
        if(e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            BffaPlayer bffaPlayer = Main.playerData.get(player);
            if(!bffaPlayer.isShootAble()) {
                player.sendMessage(LangUtils.getMessage(player, MessageEnum.BOW_CANNOT_USE));
                e.setCancelled(true);
                return;
            }
            bffaPlayer.setShootAble(false);
            bowHashMap.put(bffaPlayer, 3);
//            Bukkit.getScheduler().runTaskTimer(Main.inst(), () -> {
//
//            }, 1L, 20L);
            new BowCountDown(bffaPlayer).runTaskTimer(Main.inst(), 1L, 20L);
        }
    }
}
