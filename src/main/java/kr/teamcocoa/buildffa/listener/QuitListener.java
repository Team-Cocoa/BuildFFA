package kr.teamcocoa.buildffa.listener;

import de.fct.NickSystem.MySQL;
import kr.teamcocoa.buildffa.kit.BffaPlayer;
import kr.teamcocoa.buildffa.main.Main;
import kr.teamcocoa.buildffa.utils.StringUtils;
import kr.teamcocoa.buildffa.world.MapVote;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        e.setQuitMessage(null);
        Player p = e.getPlayer();
        BffaPlayer bffaPlayer = Main.playerData.get(p);
        MapVote mapVote = MapVote.getInstance();
        if (mapVote.getWherePlayerVoted(p) != null) {
            mapVote.removeVote(p, mapVote.getWherePlayerVoted(p));
        }
        if (bffaPlayer.isInGame()) {
            bffaPlayer.death(true);
        }
        Main.inst().stats.updatePlayer(bffaPlayer);
        Main.playerData.remove(p);

        String uuid = String.valueOf(p.getUniqueId());
        boolean nicked = MySQL.containsPlayer(uuid);
        String joinMessage = StringUtils.color("&a[&dBuildFFA&a] &e%name% left the game!");
        if (nicked) {
            String nickedName = MySQL.getNick(uuid);
            joinMessage = joinMessage.replace("%name%", nickedName);
        } else {
            joinMessage = joinMessage.replace("%name%", p.getName());
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(joinMessage);
        }
    }
}
