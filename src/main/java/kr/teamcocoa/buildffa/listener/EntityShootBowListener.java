package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.enums.MessageEnum;
import kr.teamcocoa.buildffa.kit.BffaPlayer;
import kr.teamcocoa.buildffa.main.Main;
import kr.teamcocoa.buildffa.utils.LangUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

public class EntityShootBowListener implements Listener {

//    @EventHandler
//    public void onShoot(EntityShootBowEvent e) {
//        if(e.getEntity() instanceof Player) {
//            Player player = (Player) e.getEntity();
//            BffaPlayer bffaPlayer = Main.playerData.get(player);
//            if(!bffaPlayer.isShootAble()) {
//                player.sendMessage(LangUtils.getMessage(player, MessageEnum.BOW_CANNOT_USE));
//                e.setCancelled(true);
//                return;
//            }
//            bffaPlayer.setShootAble(false);
//            Bukkit.getScheduler().runTaskTimer(Main.inst(), () -> {
//                bffaPlayer.setShootAble(true);
//                player.sendMessage(LangUtils.getMessage(player, MessageEnum.BOW_CAN_USE));
//            }, 1L, 20L);
//        }
//    }
}
