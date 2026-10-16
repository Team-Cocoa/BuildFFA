package kr.teamcocoa.buildffa.listener;

import com.google.inject.Inject;
import java.text.MessageFormat;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayerManager;
import kr.teamcocoa.buildffa.game.prestige.PrestigeManager;
import kr.teamcocoa.core.utils.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatListener implements Listener {

  private final BuildFFAPlayerManager buildFFAPlayerManager;

  private final PrestigeManager prestigeManager;

  @Inject
  public AsyncPlayerChatListener(
      BuildFFAPlayerManager buildFFAPlayerManager, PrestigeManager prestigeManager) {
    this.buildFFAPlayerManager = buildFFAPlayerManager;
    this.prestigeManager = prestigeManager;
  }

  private String format = "§8[{0}§8] §7{1} §8> §f%2$s";

  /** 0 : Prestige 1 : Name */
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onChat(AsyncPlayerChatEvent e) {
    Player player = e.getPlayer();
    String message = e.getMessage();
    BuildFFAPlayer buildFFAPlayer = buildFFAPlayerManager.getPlayer(player);

    String prestige =
        buildFFAPlayer.getPrestige().toBukkitColor()
            + StringUtils.color(prestigeManager.getPrestigeName(buildFFAPlayer));

    if (player.hasPermission("teamcocoa.premium")) {
      message = (StringUtils.color(message));
    }
    e.setFormat(MessageFormat.format(format, prestige, player.getName()));
    e.setMessage(message);
  }
}
