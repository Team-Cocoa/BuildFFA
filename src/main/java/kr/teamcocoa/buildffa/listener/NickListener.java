package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.managers.PlayerManager;
import kr.teamcocoa.buildffa.model.BuildFFAPlayer;
import kr.teamcocoa.nick.bukkit.events.PlayerNickEvent;
import kr.teamcocoa.nick.bukkit.events.PlayerUnNickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NickListener implements Listener {

    @EventHandler
    public void onNick(PlayerNickEvent e) {
        Player player = e.getPlayer();
        BuildFFAPlayer buildFFAPlayer = PlayerManager.getPlayer(player);

        if(buildFFAPlayer == null) {
            return;
        }

        if(!buildFFAPlayer.isNicked()) {
            buildFFAPlayer.setNicked(true);
        }
    }

    @EventHandler
    public void onUnNick(PlayerUnNickEvent e) {
        Player player = e.getPlayer();
        BuildFFAPlayer buildFFAPlayer = PlayerManager.getPlayer(player);

        if(buildFFAPlayer == null) {
            return;
        }

        if(buildFFAPlayer.isNicked()) {
            buildFFAPlayer.setNicked(false);
        }
    }
}
