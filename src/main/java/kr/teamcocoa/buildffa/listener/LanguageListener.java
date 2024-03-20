package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.main.BuildFFA;
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
            if(!BuildFFA.playerData.get(player).isInGame()) {
                player.getInventory().clear();
                Bukkit.getScheduler().runTaskLater(BuildFFA.getInstance(), () -> BuildFFA.playerData.get(player).setJoinInventory(), 5L);
            }
        }
        catch(NullPointerException e1) {

        }
    }
}
