package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.main.BuildFFA;
import kr.teamcocoa.buildffa.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.world.WorldManager;
import kr.teamcocoa.core.bukkit.utils.PacketUtils;
import kr.teamcocoa.core.utils.StringUtils;
import kr.teamcocoa.nick.core.model.NickManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class JoinListener implements Listener {

    private static ThreadPoolExecutor executors = new ThreadPoolExecutor(1, 30, 1, TimeUnit.SECONDS, new LinkedBlockingDeque<>(30));


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        BuildFFAPlayer buildFFAPlayer = new BuildFFAPlayer(player);
        BuildFFA.playerData.put(player, buildFFAPlayer);
        player.addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(999999, 1));
        PacketUtils.sendTitle(player, "", StringUtils.color("&7Your data is being loaded..."), 20, 1000, 20);
        e.setJoinMessage(null);
        player.teleport(new Location(Bukkit.getWorld("BuildFFA_world"), 38.5, 201, 0.5, 0, 90));

        Bukkit.getScheduler().runTaskLaterAsynchronously(BuildFFA.getInstance(), () -> {
            buildFFAPlayer.loadStats();
            String uuid = player.getUniqueId().toString();
            boolean nicked = NickManager.getInstance().isNicked(player.getUniqueId());
            for (PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }
            String joinMessage = StringUtils.color("&a[&dBuildFFA&a] &e%name% joined the game!");
            if (nicked) {
                String nickedName = NickManager.getInstance().getNickPlayer(player.getUniqueId()).getFakeNick();
                joinMessage = joinMessage.replace("%name%", nickedName);
            } else {
                joinMessage = joinMessage.replace("%name%", player.getName());
            }

            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(joinMessage);
            }
            BuildFFA.getInstance().stats.createPlayer(uuid);
            BuildFFA.getInstance().scoreboardManager.setScoreboard(player);
            PacketUtils.sendTitle(player, "", "", 0, 0, 0);
            Bukkit.getScheduler().runTaskLater(BuildFFA.getInstance(), () -> {
                Location spawn = WorldManager.getInstance().getSpawnByName(WorldManager.getInstance().getCurrentMap());
                player.teleport(spawn);
                player.setLevel(0);
                player.setHealth(1.0D);
                player.setFoodLevel(20);
                if (!BuildFFA.playerData.get(player).isBuild()) {
                    BuildFFA.playerData.get(player).setInGame(false);
                    BuildFFA.playerData.get(player).setJoinInventory();
                }
            }, 1L);
        }, 5L);

    }
}
