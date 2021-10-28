package kr.teamcocoa.buildffa.listener;

import es.minetsii.languages.events.custom.LangsLoadEvent;
import es.minetsii.languages.events.custom.PlayerChangesLanguageEvent;
import kr.teamcocoa.buildffa.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LanguageListener implements Listener {
    @EventHandler
    public void onLangLoad(LangsLoadEvent e) {
        e.addPlugin(Main.inst());
    }

    @EventHandler
    public void onLangChange(PlayerChangesLanguageEvent e) {
        try {
            Player player = e.getPlayer().getBukkitPlayer();
            if(!Main.playerData.get(player).isInGame()) {
                player.getInventory().clear();
                Bukkit.getScheduler().runTaskLater(Main.inst(), () -> Main.playerData.get(player).setJoinInventory(), 5L);
            }
        }
        catch(NullPointerException e1) {

        }
    }
}
