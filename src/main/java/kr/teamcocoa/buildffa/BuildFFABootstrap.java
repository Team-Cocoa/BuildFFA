package kr.teamcocoa.buildffa;

import com.google.inject.Guice;
import com.google.inject.Injector;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import kr.teamcocoa.buildffa.command.BuildCommand;
import kr.teamcocoa.buildffa.command.StatsCommand;
import kr.teamcocoa.buildffa.command.TeamingCommand;
import kr.teamcocoa.buildffa.command.VoteCommand;
import kr.teamcocoa.buildffa.game.kit.KitLoader;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayerManager;
import kr.teamcocoa.buildffa.game.models.BuildFFAStats;
import kr.teamcocoa.buildffa.listener.AsyncPlayerChatListener;
import kr.teamcocoa.buildffa.listener.BlockBreakListener;
import kr.teamcocoa.buildffa.listener.BlockPlaceListener;
import kr.teamcocoa.buildffa.listener.EnderPeralCancelListener;
import kr.teamcocoa.buildffa.listener.EntityDamageListener;
import kr.teamcocoa.buildffa.listener.FoodLevelChangeListener;
import kr.teamcocoa.buildffa.listener.InteractListener;
import kr.teamcocoa.buildffa.listener.InventoryClickListener;
import kr.teamcocoa.buildffa.listener.ItemDropListener;
import kr.teamcocoa.buildffa.listener.JoinListener;
import kr.teamcocoa.buildffa.listener.LanguageListener;
import kr.teamcocoa.buildffa.listener.PlayerBedEnterListener;
import kr.teamcocoa.buildffa.listener.PlayerDeathListener;
import kr.teamcocoa.buildffa.listener.PlayerMoveListener;
import kr.teamcocoa.buildffa.listener.PlayerPickupItemListener;
import kr.teamcocoa.buildffa.listener.PlayerRespawnListener;
import kr.teamcocoa.buildffa.listener.QuitListener;
import kr.teamcocoa.buildffa.listener.WeatherChangeListener;
import kr.teamcocoa.buildffa.listener.WorldInitListener;
import kr.teamcocoa.buildffa.network.stats.StatsController;
import kr.teamcocoa.buildffa.task.LobbyBarrierTask;
import kr.teamcocoa.buildffa.task.MapChangeTask;
import kr.teamcocoa.buildffa.task.ScoreboardUpdateTask;
import kr.teamcocoa.buildffa.task.StatsAutoSaveTask;
import kr.teamcocoa.buildffa.task.WorldRuleTask;
import kr.teamcocoa.buildffa.world.MapVote;
import kr.teamcocoa.buildffa.world.WorldManager;
import kr.teamcocoa.core.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BuildFFABootstrap extends JavaPlugin {

  private Injector injector;

  private PluginManager pluginManager;

  public static final String PREFIX = StringUtils.color("&a[&dBuildFFA&a] ");

  @Override
  public void onEnable() {
    this.pluginManager = getServer().getPluginManager();

    Bukkit.getLogger().info(" _____________________________________________________________");
    Bukkit.getLogger().info("|                                                             |");
    Bukkit.getLogger().info("| [BuildFFA] Plugin is loading...                             |");
    Bukkit.getLogger().info("|                                                             |");
    Bukkit.getLogger().info(" _____________________________________________________________");

    if (!KitLoader.isAvailable(this)) {
      getLogger().severe("KitManager service is not registered, disabling BuildFFA");
      pluginManager.disablePlugin(this);
      return;
    }

    saveDefaultConfig();

    this.injector = Guice.createInjector(new BuildFFAModule(this));

    loadListeners();
    loadCommands();

    injector
        .getInstance(WorldManager.class)
        .mapChange(injector.getInstance(MapVote.class).getRandomMap());

    // Map change touches worlds, blocks and Bukkit tasks, so it must run on the main thread
    Bukkit.getScheduler().runTaskTimer(this, injector.getInstance(MapChangeTask.class), 0L, 20L);

    // Bukkit scoreboard API is not thread-safe, so update it on the main thread
    Bukkit.getScheduler()
        .runTaskTimer(this, injector.getInstance(ScoreboardUpdateTask.class), 0L, 20L);

    Executors.newSingleThreadScheduledExecutor()
        .scheduleAtFixedRate(injector.getInstance(StatsAutoSaveTask.class), 0, 5, TimeUnit.MINUTES);

    Bukkit.getScheduler().runTaskLater(this, new LobbyBarrierTask(), 10L);

    Bukkit.getScheduler().runTaskLater(this, new WorldRuleTask(), 20L);
  }

  public void loadListeners() {
    registerListener(BlockPlaceListener.class);
    registerListener(EnderPeralCancelListener.class);
    registerListener(BlockBreakListener.class);
    registerListener(EntityDamageListener.class);
    registerListener(FoodLevelChangeListener.class);
    registerListener(InteractListener.class);
    registerListener(InventoryClickListener.class);
    registerListener(ItemDropListener.class);
    registerListener(JoinListener.class);
    registerListener(PlayerDeathListener.class);
    registerListener(PlayerMoveListener.class);
    registerListener(PlayerRespawnListener.class);
    registerListener(QuitListener.class);
    registerListener(WeatherChangeListener.class);
    registerListener(PlayerPickupItemListener.class);
    registerListener(LanguageListener.class);
    registerListener(WorldInitListener.class);
    registerListener(PlayerBedEnterListener.class);
    registerListener(AsyncPlayerChatListener.class);
  }

  private void registerListener(Class<? extends Listener> listenerClass) {
    pluginManager.registerEvents(injector.getInstance(listenerClass), this);
  }

  public void loadCommands() {
    getCommand("build").setExecutor(injector.getInstance(BuildCommand.class));
    getCommand("stats").setExecutor(injector.getInstance(StatsCommand.class));
    getCommand("teaming").setExecutor(injector.getInstance(TeamingCommand.class));
    getCommand("vote").setExecutor(injector.getInstance(VoteCommand.class));
  }

  @Override
  public void onDisable() {
    Bukkit.getLogger().info(" _____________________________________________________________");
    Bukkit.getLogger().info("|                                                             |");
    Bukkit.getLogger().info("| [BuildFFA] Plugin is stopping...                            |");
    Bukkit.getLogger().info("|                                                             |");
    Bukkit.getLogger().info(" _____________________________________________________________");

    if (injector == null) {
      return;
    }

    StatsController statsController = injector.getInstance(StatsController.class);

    // Saved synchronously, otherwise the server can shut down before the requests finish
    for (BuildFFAPlayer buildFFAPlayer :
        injector.getInstance(BuildFFAPlayerManager.class).getPlayers()) {
      BuildFFAStats stats = buildFFAPlayer.getBuildFFAStats();
      if (stats.isEdited() && statsController.upsertStats(stats)) {
        Bukkit.getLogger().info("Saved " + buildFFAPlayer.getPlayer().getName() + " stats.");
      }
    }
  }
}
