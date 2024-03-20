package kr.teamcocoa.buildffa.main;

import dev.derklaro.aerogel.Inject;
import dev.derklaro.aerogel.Singleton;
import eu.cloudnetservice.driver.permission.PermissionManagement;
import eu.cloudnetservice.ext.platforminject.api.PlatformEntrypoint;
import eu.cloudnetservice.ext.platforminject.api.stereotype.Command;
import eu.cloudnetservice.ext.platforminject.api.stereotype.Dependency;
import eu.cloudnetservice.ext.platforminject.api.stereotype.PlatformPlugin;
import kr.teamcocoa.buildffa.commands.*;
import kr.teamcocoa.buildffa.databases.BuildFFADatabase;
import kr.teamcocoa.buildffa.listener.*;
import kr.teamcocoa.buildffa.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.models.BuildFFAPlayerManager;
import kr.teamcocoa.buildffa.utils.ScoreboardManager;
import kr.teamcocoa.buildffa.world.MapVote;
import kr.teamcocoa.buildffa.world.WorldData;
import kr.teamcocoa.buildffa.world.WorldManager;
import kr.teamcocoa.core.utils.StringUtils;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@Singleton
@PlatformPlugin(
        platform = "bukkit",
        name = "BuildFFA",
        version = "1.0",
        authors = "fixca",
        dependencies = {
                @Dependency(name = "Language"),
                @Dependency(name = "MySQL")
        },
        pluginFileNames = "plugin.yml",
        commands = {
                @Command(name = "build"),
                @Command(name = "stats"),
                @Command(name = "teaming"),
                @Command(name = "vote")
        },
        api = "1.13"
)
public class BuildFFA implements PlatformEntrypoint {

    @Getter
    private static JavaPlugin instance;

    @Getter
    private static PermissionManagement permissionManagement;

    private PluginManager pluginManager;

    public static WorldData worldData = new WorldData();
    public static boolean teaming;

    public static final String PREFIX = StringUtils.color("&a[&dBuildFFA&a] ");

    @Inject
    private BuildFFA(
        @NonNull JavaPlugin javaPlugin,
        @NonNull PluginManager pluginManager,
        @NonNull PermissionManagement permissionManagement
    ) {
        BuildFFA.instance = javaPlugin;
        BuildFFA.permissionManagement = permissionManagement;
        BuildFFA.teaming = false;
        this.pluginManager = pluginManager;
    }

    @Override
    public void onLoad() {
        Bukkit.getLogger().info(" _____________________________________________________________");
        Bukkit.getLogger().info("|                                                             |");
        Bukkit.getLogger().info("| [BuildFFA] Plugin is loading...                             |");
        Bukkit.getLogger().info("|                                                             |");
        Bukkit.getLogger().info(" _____________________________________________________________");

        BuildFFADatabase.init();

        loadListeners();
        loadCommands();

        WorldManager worldManager = WorldManager.getInstance();
        String map = MapVote.getInstance().getRandomMap();
        worldManager.setCurrentMapName(map);
        WorldManager.getInstance().loadWorld(map);
        worldManager.cloneWorld(map);
        Bukkit.getWorld(map).loadChunk(worldManager.getSpawnByName(map).getChunk());
        WorldManager.getInstance().mapChangeUpdater();

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            for (BuildFFAPlayer buildFFAPlayer : BuildFFAPlayerManager.getPlayerTable().values()) {
                ScoreboardManager.sendBuildFFAScoreboard(buildFFAPlayer);
            }
        }, 0, 1, TimeUnit.SECONDS);

    }

    public void loadListeners() {
        pluginManager.registerEvents(new BlockPlaceListener(), instance);
        pluginManager.registerEvents(new EnderPeralCancelListener(), instance);
        pluginManager.registerEvents(new BlockBreakListener(), instance);
        pluginManager.registerEvents(new EntityDamageListener(), instance);
        pluginManager.registerEvents(new FoodLevelChangeListener(), instance);
        pluginManager.registerEvents(new InteractListener(), instance);
        pluginManager.registerEvents(new InventoryClickListener(), instance);
        pluginManager.registerEvents(new InventoryCloseListener(), instance);
        pluginManager.registerEvents(new ItemDropListener(), instance);
        pluginManager.registerEvents(new JoinListener(), instance);
        pluginManager.registerEvents(new PlayerDeathListener(), instance);
        pluginManager.registerEvents(new PlayerMoveListener(), instance);
        pluginManager.registerEvents(new PlayerRespawnListener(), instance);
        pluginManager.registerEvents(new QuitListener(), instance);
        pluginManager.registerEvents(new WeatherChangeListener(), instance);
        pluginManager.registerEvents(new PlayerPickupItemListener(), instance);
        pluginManager.registerEvents(new LanguageListener(), instance);
        pluginManager.registerEvents(new WorldInitListener(), instance);
        pluginManager.registerEvents(new NickListener(), instance);
        pluginManager.registerEvents(new PlayerBedEnterListener(), instance);
        pluginManager.registerEvents(new AsyncPlayerChatListener(), instance);
    }

    public void loadCommands() {
        instance.getCommand("build").setExecutor(new BuildCommand());
        instance.getCommand("stats").setExecutor(new StatsCommand());
        instance.getCommand("teaming").setExecutor(new TeamingCommand());
        instance.getCommand("vote").setExecutor(new VoteCommand());

        instance.getCommand("stats").setTabCompleter(new StatsCommand());
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info(" _____________________________________________________________");
        Bukkit.getLogger().info("|                                                             |");
        Bukkit.getLogger().info("| [BuildFFA] Plugin is stopping...                             |");
        Bukkit.getLogger().info("|                                                             |");
        Bukkit.getLogger().info(" _____________________________________________________________");
        BuildFFA.worldData.removeBlocks();
    }

}
