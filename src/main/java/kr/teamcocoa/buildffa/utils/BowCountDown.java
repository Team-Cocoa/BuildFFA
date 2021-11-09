package kr.teamcocoa.buildffa.utils;

import kr.teamcocoa.buildffa.enums.MessageEnum;
import kr.teamcocoa.buildffa.kit.BffaPlayer;
import kr.teamcocoa.buildffa.listener.EntityShootBowListener;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class BowCountDown extends BukkitRunnable {

    private BffaPlayer bffaPlayer;
    private Player player;

    public BowCountDown(BffaPlayer player) {
        this.bffaPlayer = player;
        this.player = player.getPlayer();
    }

    @Override
    public void run() {
        try {
            int i = EntityShootBowListener.bowHashMap.get(bffaPlayer);
            if (i == 0) {
                bffaPlayer.setShootAble(true);
                player.sendMessage(LangUtils.getMessage(player, MessageEnum.BOW_CAN_USE));
                EntityShootBowListener.bowHashMap.remove(bffaPlayer);
                cancel();
                return;
            }
            if (i != 1) {
                player.sendMessage(LangUtils.getMessage(player, MessageEnum.BOW_COUNT_SECONDS).replace("%int%", String.valueOf(i)));
            } else {
                player.sendMessage(LangUtils.getMessage(player, MessageEnum.BOW_COUNT_SECOND).replace("%int%", String.valueOf(i)));
            }
            EntityShootBowListener.bowHashMap.put(bffaPlayer, i - 1);
        }
        catch(Exception e) {

        }
    }
}
