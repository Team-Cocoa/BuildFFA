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
        final Player p = e.getPlayer();
        Main.playerData.put(p, new BffaPlayer(p));
        p.addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(999999, 1));
        Title.sendTitle(p, "", StringUtils.color("&7Your data is loading..."), 20, 1000, 20);
        e.setJoinMessage(null);

        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.inst(), () -> {
            String uuid = String.valueOf(p.getUniqueId());
            boolean nicked = MySQL.containsPlayer(uuid);
            for (PotionEffect effect : p.getActivePotionEffects()) {
                p.removePotionEffect(effect.getType());
            }
            String joinMessage = StringUtils.color("&a[&dBuildFFA&a] &e%name% joined the game!");
            if (nicked) {
                String nickedName = MySQL.getNick(uuid);
                joinMessage = joinMessage.replace("%name%", nickedName);
            } else {
                joinMessage = joinMessage.replace("%name%", p.getName());
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(joinMessage);
            }
            Main.inst().stats.createPlayer(uuid);
            Main.inst().scoreboardManager.setScoreboard(p);
            Title.sendTitle(p, "", "", 0, 0, 0);
            Bukkit.getScheduler().runTaskLater(Main.inst(), () -> {
                Location spawn = WorldManager.getInstance().getSpawnByName(WorldManager.getInstance().getCurrentMap());
                p.teleport(spawn);
                p.setLevel(0);
                p.setHealth(1.0D);
                p.setFoodLevel(20);
                if (!Main.playerData.get(p).isBuild()) {
                    Main.playerData.get(p).setInGame(false);
                    Main.playerData.get(p).setJoinInventory();
                }
            }, 1L);
        }, 5L);

    }
}
