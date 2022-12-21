package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.model.BuildFFAPlayer;
import kr.teamcocoa.buildffa.main.BuildFFA;
import kr.teamcocoa.buildffa.utils.StringUtils;
import kr.teamcocoa.buildffa.world.MapVote;
import kr.teamcocoa.nick.core.model.NickManager;
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
        BuildFFAPlayer buildFFAPlayer = BuildFFA.playerData.get(p);
        MapVote mapVote = MapVote.getInstance();
        if (mapVote.getWherePlayerVoted(p) != null) {
            mapVote.removeVote(p, mapVote.getWherePlayerVoted(p));
        }
        if (buildFFAPlayer.isInGame()) {
            buildFFAPlayer.death(true);
        }
        Bukkit.getScheduler().runTaskAsynchronously(BuildFFA.getInstance(), () -> {
            BuildFFA.getInstance().statsDatabase.updatePlayer(buildFFAPlayer);
            BuildFFA.playerData.remove(p);

            String uuid = p.getUniqueId().toString();
            boolean nicked = NickManager.getInstance().isNicked(p.getUniqueId());
            String joinMessage = StringUtils.color("&a[&dBuildFFA&a] &e%name% left the game!");
            if (nicked) {
                String nickedName = NickManager.getInstance().getNickPlayer(p.getUniqueId()).getFakeNick();
                joinMessage = joinMessage.replace("%name%", nickedName);
            } else {
                joinMessage = joinMessage.replace("%name%", p.getName());
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(joinMessage);
            }
        });
    }
}
