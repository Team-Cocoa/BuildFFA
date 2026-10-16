package kr.teamcocoa.buildffa.listener;

import com.google.inject.Inject;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayerManager;
import kr.teamcocoa.language.bukkit.events.PlayerChangeLanguageEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class LanguageListener implements Listener {

  private final Plugin plugin;

  private final BuildFFAPlayerManager buildFFAPlayerManager;

  @Inject
  public LanguageListener(Plugin plugin, BuildFFAPlayerManager buildFFAPlayerManager) {
    this.plugin = plugin;
    this.buildFFAPlayerManager = buildFFAPlayerManager;
  }

  @EventHandler
  public void onLangChange(PlayerChangeLanguageEvent e) {
    try {
      Player player = e.getPlayer();
      BuildFFAPlayer buildFFAPlayer = buildFFAPlayerManager.getPlayer(player);

      if (!buildFFAPlayer.isInGame()) {
        player.getInventory().clear();
        Bukkit.getScheduler().runTaskLater(plugin, buildFFAPlayer::setJoinInventory, 5L);
      }
    } catch (NullPointerException e1) {

    }
  }
}
