package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.model.BuildFFAPlayer;
import kr.teamcocoa.buildffa.main.BuildFFA;
import kr.teamcocoa.nick.bukkit.events.PlayerNickEvent;
import kr.teamcocoa.nick.bukkit.events.PlayerUnNickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NickListener implements Listener {

    @EventHandler
    public void onNick(PlayerNickEvent e) {
        Player player = e.getPlayer();
        BuildFFAPlayer buildFFAPlayer = BuildFFA.playerData.get(player);
        if(!buildFFAPlayer.isNicked()) {
            buildFFAPlayer.addNicked();
        }
    }

    @EventHandler
    public void onUnNick(PlayerUnNickEvent e) {
        Player player = e.getPlayer();
        BuildFFAPlayer buildFFAPlayer = BuildFFA.playerData.get(player);
        if(buildFFAPlayer.isNicked()) {
            buildFFAPlayer.removeNicked();
        }
    }
}
