package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.BuildFFABootstrap;
import kr.teamcocoa.buildffa.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.models.BuildFFAPlayerManager;
import kr.teamcocoa.language.bukkit.events.PlayerChangeLanguageEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LanguageListener implements Listener {

    @EventHandler
    public void onLangChange(PlayerChangeLanguageEvent e) {
        try {
            Player player = e.getPlayer();
            BuildFFAPlayer buildFFAPlayer = BuildFFAPlayerManager.getPlayer(player);

            if(!buildFFAPlayer.isInGame()) {
                player.getInventory().clear();
                Bukkit.getScheduler().runTaskLater(BuildFFABootstrap.getInstance(), () -> buildFFAPlayer.setJoinInventory(), 5L);
            }
        }
        catch(NullPointerException e1) {

        }
    }

}
