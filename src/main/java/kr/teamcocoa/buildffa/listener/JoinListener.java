package kr.teamcocoa.buildffa.listener;

import com.google.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import kr.teamcocoa.buildffa.game.kit.KitLoader;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayerManager;
import kr.teamcocoa.buildffa.game.models.BuildFFAStats;
import kr.teamcocoa.buildffa.game.models.BuildFFAStatsManager;
import kr.teamcocoa.buildffa.game.prestige.PrestigeManager;
import kr.teamcocoa.buildffa.game.tabs.TabManager;
import kr.teamcocoa.buildffa.network.stats.StatsController;
import kr.teamcocoa.buildffa.world.WorldManager;
import kr.teamcocoa.core.bukkit.utils.PacketUtils;
import kr.teamcocoa.core.utils.StringUtils;
import kr.teamcocoa.core.utils.ThreadUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class JoinListener implements Listener {

  private final Plugin plugin;

  private final BuildFFAPlayerManager buildFFAPlayerManager;

  private final BuildFFAStatsManager buildFFAStatsManager;

  private final StatsController statsController;

  private final PrestigeManager prestigeManager;

  private final TabManager tabManager;

  private final WorldManager worldManager;

  private final KitLoader kitLoader;

  private final Executor mainThread;

  @Inject
  public JoinListener(
      Plugin plugin,
      BuildFFAPlayerManager buildFFAPlayerManager,
      BuildFFAStatsManager buildFFAStatsManager,
      StatsController statsController,
      PrestigeManager prestigeManager,
      TabManager tabManager,
      WorldManager worldManager,
      KitLoader kitLoader) {
    this.plugin = plugin;
    this.buildFFAPlayerManager = buildFFAPlayerManager;
    this.buildFFAStatsManager = buildFFAStatsManager;
    this.statsController = statsController;
    this.prestigeManager = prestigeManager;
    this.tabManager = tabManager;
    this.worldManager = worldManager;
    this.kitLoader = kitLoader;
    this.mainThread = task -> Bukkit.getScheduler().runTask(plugin, task);
  }

  private final ThreadPoolExecutor executors =
      ThreadUtils.getThreadPool(Bukkit.getMaxPlayers(), 1, TimeUnit.SECONDS, "bffa-dataload-pool");

  @EventHandler
  public void onJoin(PlayerJoinEvent e) {
    Player player = e.getPlayer();
    e.setJoinMessage("");
    prepareLoading(player);

    CompletableFuture<BuildFFAStats> statsFuture =
        CompletableFuture.supplyAsync(() -> loadStats(player), executors);
    CompletableFuture<ItemStack[]> kitFuture =
        CompletableFuture.supplyAsync(() -> loadKit(player), executors);

    // Registered on the main thread so it cannot interleave with QuitListener
    statsFuture
        .thenAcceptBothAsync(
            kitFuture,
            (stats, kit) -> {
              if (!player.isOnline()) {
                return;
              }
              completeJoin(registerPlayer(player, stats, kit));
            },
            mainThread)
        .exceptionally(
            error -> {
              failJoin(player, error);
              return null;
            });
  }

  private void prepareLoading(Player player) {
    player.addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(999999, 1));
    PacketUtils.sendTitle(
        player, "", StringUtils.color("&7Your data is being loaded..."), 20, 1000, 20);
    player.teleport(new Location(Bukkit.getWorld("ArenaWorld"), 0.5, 5.0, 0.5));
  }

  private BuildFFAStats loadStats(Player player) {
    BuildFFAStats buildFFAStats = buildFFAStatsManager.getCache().readData(player.getUniqueId());
    if (buildFFAStats != null) {
      return buildFFAStats;
    }

    buildFFAStats = new BuildFFAStats(player.getUniqueId());
    try {
      statsController.loadStats(buildFFAStats);
    } catch (IllegalStateException e) {
      throw new JoinLoadException(
          "An error has occurred while loading the stats. Contact to developer.", e);
    }
    buildFFAStatsManager.getCache().createData(player.getUniqueId(), buildFFAStats);
    return buildFFAStats;
  }

  private ItemStack[] loadKit(Player player) {
    ItemStack[] kit = kitLoader.load(player);
    if (kit == null) {
      throw new JoinLoadException(
          "An error has occurred while loading the kit. Contact to developer.", null);
    }
    return kit;
  }

  private BuildFFAPlayer registerPlayer(Player player, BuildFFAStats stats, ItemStack[] kit) {
    buildFFAPlayerManager.addPlayer(player, stats);

    BuildFFAPlayer buildFFAPlayer = buildFFAPlayerManager.getPlayer(player);
    buildFFAPlayer.setKit(kit);
    prestigeManager.loadPrestige(buildFFAPlayer);
    return buildFFAPlayer;
  }

  private void completeJoin(BuildFFAPlayer buildFFAPlayer) {
    Player player = buildFFAPlayer.getPlayer();

    String joinMessage =
        StringUtils.color("&a[&dBuildFFA&a] &e" + player.getName() + " joined the game!");
    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
      onlinePlayer.sendMessage(joinMessage);
    }

    PacketUtils.sendTitle(player, "", "", 0, 0, 0);

    for (PotionEffect effect : player.getActivePotionEffects()) {
      player.removePotionEffect(effect.getType());
    }
    player.teleport(worldManager.getCurrentMap().getSpawn());
    player.setLevel(0);
    player.setHealth(20);
    player.setFoodLevel(20);
    buildFFAPlayer.setJoinInventory();
    tabManager.updateNameTags(player);
  }

  private void failJoin(Player player, Throwable error) {
    Throwable cause = error instanceof CompletionException ? error.getCause() : error;
    plugin.getLogger().log(Level.SEVERE, "Failed to load " + player.getName() + "'s data", cause);

    String kickMessage =
        cause instanceof JoinLoadException
            ? cause.getMessage()
            : "An error has occurred while loading the data. Contact to developer.";
    mainThread.execute(() -> player.kickPlayer(kickMessage));
  }

  private static class JoinLoadException extends RuntimeException {
    JoinLoadException(String kickMessage, Throwable cause) {
      super(kickMessage, cause);
    }
  }
}
