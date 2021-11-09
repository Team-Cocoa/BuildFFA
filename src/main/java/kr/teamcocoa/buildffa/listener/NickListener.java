package kr.teamcocoa.buildffa.listener;

import de.fct.NickSystem.events.PlayerNickEvent;
import de.fct.NickSystem.events.PlayerUnNickEvent;
import kr.teamcocoa.buildffa.kit.BffaPlayer;
import kr.teamcocoa.buildffa.main.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NickListener implements Listener {

    @EventHandler
    public void onNick(PlayerNickEvent e) {
        Player player = e.getPlayer();
        BffaPlayer bffaPlayer = Main.playerData.get(player);
        if(!bffaPlayer.isNicked()) {
            bffaPlayer.addNicked();
        }
    }

    @EventHandler
    public void onUnNick(PlayerUnNickEvent e) {
        Player player = e.getPlayer();
        BffaPlayer bffaPlayer = Main.playerData.get(player);
        if(bffaPlayer.isNicked()) {
            bffaPlayer.removeNicked();
        }
    }
}
