package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.databases.StatsDatabase;
import kr.teamcocoa.buildffa.BuildFFABootstrap;
import kr.teamcocoa.buildffa.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.models.BuildFFAPlayerManager;
import kr.teamcocoa.buildffa.models.BuildFFAStats;
import kr.teamcocoa.buildffa.models.BuildFFAStatsManager;
import kr.teamcocoa.buildffa.prestige.PrestigeManager;
import kr.teamcocoa.buildffa.tabs.TabManager;
import kr.teamcocoa.buildffa.world.WorldManager;
import kr.teamcocoa.core.bukkit.utils.PacketUtils;
import kr.teamcocoa.core.utils.StringUtils;
import kr.teamcocoa.core.utils.ThreadUtils;
import net.kyori.adventure.text.Component;
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

    private static ThreadPoolExecutor executors =
            ThreadUtils.getThreadPool(Bukkit.getMaxPlayers(), 1, TimeUnit.SECONDS, "bffa-dataload-pool");

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        e.setJoinMessage("");
        player.addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(999999, 1));
        PacketUtils.sendTitle(player, "", StringUtils.color("&7Your data is being loaded..."), 20, 1000, 20);
        player.teleport(new Location(Bukkit.getWorld("ArenaWorld"), 0.5, 5.0, 0.5));

        executors.execute(() -> {
            BuildFFAStats buildFFAStats = BuildFFAStatsManager.getCache().readData(player.getUniqueId());
            if(buildFFAStats == null) {
                buildFFAStats = new BuildFFAStats(player.getUniqueId());
                try {
                    StatsDatabase.loadStats(buildFFAStats);
                }
                catch (IllegalStateException e1) {
                    player.kickPlayer("An error has occurred while loading the stats. Contact to developer.");
                    return;
                }
                BuildFFAStatsManager.getCache().createData(player.getUniqueId(), buildFFAStats);
            }

            BuildFFAPlayerManager.addPlayer(player, buildFFAStats);

            BuildFFAPlayer buildFFAPlayer = BuildFFAPlayerManager.getPlayer(player);

            PrestigeManager.getInstance().loadPrestige(buildFFAPlayer);

            String joinMessage = StringUtils.color("&a[&dBuildFFA&a] &e" + player.getName() + " joined the game!");
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.sendMessage(joinMessage);
            }

            PacketUtils.sendTitle(player, "", "", 0, 0, 0);

            Bukkit.getScheduler().runTask(BuildFFABootstrap.getInstance(), () -> {
                for (PotionEffect effect : player.getActivePotionEffects()) {
                    player.removePotionEffect(effect.getType());
                }
                player.teleport(WorldManager.getInstance().getCurrentMap().getSpawn());
                player.setLevel(0);
                player.setHealth(20);
                player.setFoodLevel(20);
                buildFFAPlayer.setJoinInventory();
                TabManager.updateNameTags(player);
            });

        });

    }
}
