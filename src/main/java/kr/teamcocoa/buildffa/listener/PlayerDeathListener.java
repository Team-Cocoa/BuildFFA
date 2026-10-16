package kr.teamcocoa.buildffa.listener;

import com.google.inject.Inject;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;

public class PlayerDeathListener implements Listener {

  private final Plugin plugin;

  private final BuildFFAPlayerManager buildFFAPlayerManager;

  @Inject
  public PlayerDeathListener(Plugin plugin, BuildFFAPlayerManager buildFFAPlayerManager) {
    this.plugin = plugin;
    this.buildFFAPlayerManager = buildFFAPlayerManager;
  }

  // 이건 수정 보류

  // PlayerDeathEvent can't be cancelled in 1.8, so lethal damage is cancelled instead
  // and the player never actually dies.
  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onLethalDamage(EntityDamageEvent e) {
    if (!(e.getEntity() instanceof Player)) {
      return;
    }

    Player player = (Player) e.getEntity();
    if (player.getHealth() - e.getFinalDamage() > 0) {
      return;
    }

    BuildFFAPlayer buildFFAPlayer = buildFFAPlayerManager.getPlayer(player);
    if (buildFFAPlayer == null) {
      return;
    }

    e.setCancelled(true);
    handleDeath(buildFFAPlayer);
  }

  // Fallback for deaths that skip the damage event (e.g. setHealth(0))
  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent e) {
    Player player = e.getEntity();

    e.setDeathMessage(null);
    e.getDrops().clear();
    e.setDroppedExp(0);

    BuildFFAPlayer buildFFAPlayer = buildFFAPlayerManager.getPlayer(player);
    if (buildFFAPlayer == null) {
      return;
    }

    Bukkit.getScheduler()
        .runTask(
            plugin,
            () -> {
              player.spigot().respawn();
              handleDeath(buildFFAPlayer);
            });
  }

  private void handleDeath(BuildFFAPlayer buildFFAPlayer) {
    Player killer = buildFFAPlayer.getLastHitPlayer();

    if (killer != null) {
      BuildFFAPlayer killerBuildFFAPlayer = buildFFAPlayerManager.getPlayer(killer);
      killerBuildFFAPlayer.kill(buildFFAPlayer);
    }

    buildFFAPlayer.reset();
    buildFFAPlayer.death(false);
  }
}
