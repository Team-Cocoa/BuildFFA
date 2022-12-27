package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.main.BuildFFA;
import kr.teamcocoa.buildffa.managers.PlayerManager;
import kr.teamcocoa.buildffa.model.BuildFFAPlayer;
import kr.teamcocoa.buildffa.utils.*;
import kr.teamcocoa.buildffa.world.WorldManager;
import kr.teamcocoa.nick.core.model.NickManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.text.MessageFormat;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PlayerJoinListener implements Listener {

    private ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 20, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<>(20));

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);

        Player player = e.getPlayer();
        player.addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(999999, 1));
        player.teleport(new Location(Bukkit.getWorld("BuildFFA_world"), 38.5, 201, 0.5, 0, 90));
        Title.sendTitle(player, "", StringUtils.color("&7Your data is loading..."), 20, 1000, 20);

        executor.execute(() -> {
            BuildFFAPlayer buildFFAPlayer = PlayerManager.createPlayer(player);
            buildFFAPlayer.loadStats();
            boolean nicked = NickManager.getInstance().isNicked(player.getUniqueId());

            String joinMessage = MessageFormat.format(BuildFFA.PREFIX + StringUtils.color("&e{0} &ajoined the game!"),
                    nicked ? NickManager.getInstance().getNickPlayer(player.getUniqueId()).getFakeNick() : player.getName());

            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(joinMessage);
            }

            BuildFFA.getInstance().statsDatabase.initNewPlayer(player.getUniqueId());
            BuildFFA.getInstance().scoreboardManager.setScoreboard(player);
            Title.sendTitle(player, "", "", 0, 0, 0);
            Bukkit.getScheduler().runTask(BuildFFA.getInstance(), () -> {
                for (PotionEffect effect : player.getActivePotionEffects()) {
                    player.removePotionEffect(effect.getType());
                }
                Location spawn = WorldManager.getInstance().getSpawnByName(WorldManager.getInstance().getCurrentMap());
                player.teleport(spawn);
                player.setLevel(0);
                player.setHealth(20.0D);
                player.setFoodLevel(20);
                if (!buildFFAPlayer.isBuild()) {
                    buildFFAPlayer.setInGame(false);
                    buildFFAPlayer.setJoinInventory();
                }
            });
        });

    }
}
