package kr.teamcocoa.buildffa.listener;

import de.fct.NickSystem.MySQL;
import de.fct.NickSystem.Nick;
import kr.teamcocoa.buildffa.enums.MessageEnum;
import kr.teamcocoa.buildffa.main.Main;
import kr.teamcocoa.buildffa.kit.BffaPlayer;
import kr.teamcocoa.buildffa.utils.*;
import kr.teamcocoa.buildffa.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
        Main.playerData.put(player, new BffaPlayer(player));
        player.addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(999999, 1));
        Title.sendTitle(player, "", StringUtils.color("&7Your data is loading..."), 20, 1000, 20);
        e.setJoinMessage(null);
        player.teleport(new Location(Bukkit.getWorld("BuildFFA_world"), 38.5, 201, 0.5, 0, 90));

        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.inst(), () -> {
            String uuid = String.valueOf(player.getUniqueId());
            boolean nicked = MySQL.containsPlayer(uuid);
            for (PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }
            String joinMessage = StringUtils.color("&a[&dBuildFFA&a] &e%name% joined the game!");
            if (nicked) {
                String nickedName = MySQL.getNick(uuid);
                joinMessage = joinMessage.replace("%name%", nickedName);
            } else {
                joinMessage = joinMessage.replace("%name%", player.getName());
            }

            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(joinMessage);
            }
            Main.inst().stats.createPlayer(uuid);
            Main.inst().scoreboardManager.setScoreboard(player);
            Title.sendTitle(player, "", "", 0, 0, 0);
            Bukkit.getScheduler().runTaskLater(Main.inst(), () -> {
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
