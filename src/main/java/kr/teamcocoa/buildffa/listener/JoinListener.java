package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.main.Main;
import kr.teamcocoa.buildffa.kit.BffaPlayer;
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

public class JoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        BffaPlayer bffaPlayer = new BffaPlayer(player);
        Main.playerData.put(player, bffaPlayer);
        player.addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(999999, 1));
        Title.sendTitle(player, "", StringUtils.color("&7Your data is loading..."), 20, 1000, 20);
        e.setJoinMessage(null);
        player.teleport(new Location(Bukkit.getWorld("BuildFFA_world"), 38.5, 201, 0.5, 0, 90));

        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getInstance(), () -> {
            bffaPlayer.loadStats();
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
            Main.getInstance().stats.createPlayer(uuid);
            Main.getInstance().scoreboardManager.setScoreboard(player);
            Title.sendTitle(player, "", "", 0, 0, 0);
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                Location spawn = WorldManager.getInstance().getSpawnByName(WorldManager.getInstance().getCurrentMap());
                player.teleport(spawn);
                player.setLevel(0);
                player.setHealth(1.0D);
                player.setFoodLevel(20);
                if (!Main.playerData.get(player).isBuild()) {
                    Main.playerData.get(player).setInGame(false);
                    Main.playerData.get(player).setJoinInventory();
                }
            }, 1L);
        }, 5L);

    }
}
