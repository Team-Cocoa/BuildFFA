package kr.teamcocoa.buildffa;

import dev.derklaro.aerogel.Inject;
import dev.derklaro.aerogel.Singleton;
import eu.cloudnetservice.driver.event.EventManager;
import eu.cloudnetservice.driver.permission.PermissionManagement;
import eu.cloudnetservice.ext.platforminject.api.PlatformEntrypoint;
import eu.cloudnetservice.ext.platforminject.api.stereotype.Command;
import eu.cloudnetservice.ext.platforminject.api.stereotype.Dependency;
import eu.cloudnetservice.ext.platforminject.api.stereotype.PlatformPlugin;
import kr.teamcocoa.buildffa.commands.BuildCommand;
import kr.teamcocoa.buildffa.commands.StatsCommand;
import kr.teamcocoa.buildffa.commands.TeamingCommand;
import kr.teamcocoa.buildffa.commands.VoteCommand;
import kr.teamcocoa.buildffa.databases.BuildFFADatabase;
import kr.teamcocoa.buildffa.databases.StatsDatabase;
import kr.teamcocoa.buildffa.listener.*;
import kr.teamcocoa.buildffa.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.models.BuildFFAPlayerManager;
import kr.teamcocoa.buildffa.models.BuildFFAStats;
import kr.teamcocoa.buildffa.tabs.TabListener;
import kr.teamcocoa.buildffa.utils.ScoreboardManager;
import kr.teamcocoa.buildffa.world.MapChangeScheduler;
import kr.teamcocoa.buildffa.world.MapVote;
import kr.teamcocoa.buildffa.world.WorldManager;
import kr.teamcocoa.buildffa.world.maps.Maps;
import kr.teamcocoa.core.utils.StringUtils;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BuildFFABootstrap extends JavaPlugin{

    @Getter
    private static JavaPlugin instance;

    private PluginManager pluginManager;

    public static boolean teaming = false;

    public static final String PREFIX = StringUtils.color("&a[&dBuildFFA&a] ");

    @Inject
    private BuildFFABootstrap(
            @NonNull JavaPlugin javaPlugin,
            @NonNull PluginManager pluginManager,
            @NonNull PermissionManagement permissionManagement,
            @NonNull EventManager eventManager
            ) {
        BuildFFABootstrap.instance = javaPlugin;
        BuildFFABootstrap.permissionManagement = permissionManagement;
        BuildFFABootstrap.teaming = false;
        BuildFFABootstrap.eventManager = eventManager;
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

        Maps map = MapVote.getInstance().getRandomMap();
        WorldManager.getInstance().mapChange(map);

        MapChangeScheduler.getInstance().mapChangeUpdater();

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            for (BuildFFAPlayer buildFFAPlayer : BuildFFAPlayerManager.getPlayerTable().values()) {
                ScoreboardManager.sendBuildFFAScoreboard(buildFFAPlayer);
//                Bukkit.getLogger().info(buildFFAPlayer.getPlayer().getName() + " | ingame : " + buildFFAPlayer.isInGame());
            }
        }, 0, 1, TimeUnit.SECONDS);

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            for (BuildFFAPlayer buildFFAPlayer : BuildFFAPlayerManager.getPlayerTable().values()) {
                BuildFFAStats stats = buildFFAPlayer.getBuildFFAStats();
                if(stats.isEdited()) {
                    StatsDatabase.upsertStats(stats);
                    stats.setEdited(false);
                    Bukkit.getLogger().info("Auto saved " + buildFFAPlayer.getPlayer().getName() + " stats.");
                }
            }
        }, 0, 5, TimeUnit.MINUTES);

        Bukkit.getScheduler().runTaskLater(instance, () -> {
            World world = Bukkit.getWorld("ArenaWorld");
            world.getBlockAt(0, 4, 0).setType(Material.BARRIER);
            world.getBlockAt(0, 7, 0).setType(Material.BARRIER);
            world.getBlockAt(1, 6, 0).setType(Material.BARRIER);
            world.getBlockAt(-1, 6, 0).setType(Material.BARRIER);
            world.getBlockAt(0, 6, 1).setType(Material.BARRIER);
            world.getBlockAt(0, 6, -1).setType(Material.BARRIER);
        }, 10L);

        Bukkit.getScheduler().runTaskLater(instance, () -> {
            for (World world : Bukkit.getWorlds()) {
                world.setGameRuleValue("announceAdvancements", "false");
                world.setGameRuleValue("logAdminCommands", "false");
                world.setGameRuleValue("doDaylightCycle", "false");
                world.setTime(0);
            }
        }, 20L);

    }

    public void loadListeners() {
        pluginManager.registerEvents(new BlockPlaceListener(), instance);
        pluginManager.registerEvents(new EnderPeralCancelListener(), instance);
        pluginManager.registerEvents(new BlockBreakListener(), instance);
        pluginManager.registerEvents(new EntityDamageListener(), instance);
        pluginManager.registerEvents(new FoodLevelChangeListener(), instance);
        pluginManager.registerEvents(new InteractListener(), instance);
        pluginManager.registerEvents(new InventoryClickListener(), instance);
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

        eventManager.registerListener(TabListener.class);
    }

    public void loadCommands() {
        instance.getCommand("build").setExecutor(new BuildCommand());
        instance.getCommand("stats").setExecutor(new StatsCommand());
        instance.getCommand("teaming").setExecutor(new TeamingCommand());
        instance.getCommand("vote").setExecutor(new VoteCommand());
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info(" _____________________________________________________________");
        Bukkit.getLogger().info("|                                                             |");
        Bukkit.getLogger().info("| [BuildFFA] Plugin is stopping...                            |");
        Bukkit.getLogger().info("|                                                             |");
        Bukkit.getLogger().info(" _____________________________________________________________");

        eventManager.unregisterListener(TabListener.class);

        Executors.newSingleThreadExecutor().execute(() -> {
            for (BuildFFAPlayer buildFFAPlayer : BuildFFAPlayerManager.getPlayerTable().values()) {
                BuildFFAStats stats = buildFFAPlayer.getBuildFFAStats();
                if(stats.isEdited()) {
                    StatsDatabase.upsertStats(stats);
                    stats.setEdited(false);
                    Bukkit.getLogger().info("Auto saved " + buildFFAPlayer.getPlayer().getName() + " stats.");
                }
            }
        });
    }

}
