package kr.teamcocoa.buildffa.main;

import kr.teamcocoa.buildffa.commands.*;
import kr.teamcocoa.buildffa.listener.*;
import kr.teamcocoa.buildffa.database.BuildFFADatabase;
import kr.teamcocoa.buildffa.managers.GUIManager;
import kr.teamcocoa.buildffa.managers.PlayerManager;
import kr.teamcocoa.buildffa.model.BuildFFAPlayer;
import kr.teamcocoa.buildffa.utils.ScoreboardExecutor;
import kr.teamcocoa.buildffa.utils.StringUtils;
import kr.teamcocoa.buildffa.world.MapVote;
import kr.teamcocoa.buildffa.world.WorldData;
import kr.teamcocoa.buildffa.world.WorldManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BuildFFA extends JavaPlugin {

    public static final String PREFIX = StringUtils.color("&a[&dBuildFFA&a] ");

    @Getter
    private static BuildFFA instance;

    private ScheduledExecutorService rankingUpdater = Executors.newSingleThreadScheduledExecutor();

    public static HashMap<Player, BuildFFAPlayer> playerData = new HashMap<>();

    @Getter
    @Setter
    private static boolean teaming = false;

    @Override
    public void onEnable() {
        instance = this;

        Bukkit.getLogger().info("_______________________________________________________________");
        Bukkit.getLogger().info("|                                                             |");
        Bukkit.getLogger().info("| [BuildFFA] Plugin is Loading...                             |");
        Bukkit.getLogger().info("|                                                             |");
        Bukkit.getLogger().info("_______________________________________________________________");

        init();

        WorldManager worldManager = WorldManager.getInstance();
        String map = MapVote.getInstance().getRandomMap();
        worldManager.setCurrentMapName(map);
        WorldManager.getInstance().loadWorld(map);
        worldManager.cloneWorld(map);
        Bukkit.getWorld(map).loadChunk(worldManager.getSpawnByName(map).getChunk());
        WorldManager.getInstance().mapChangeUpdater();

    }

    private void init() {
        loadListeners();
        loadCommands();
        BuildFFADatabase.init();
        GUIManager.init();
        loadUpdater();
    }

    private void loadListeners() {
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new EnderPeralCancelListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageListener(), this);
        getServer().getPluginManager().registerEvents(new FoodLevelChangeListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryCloseListener(), this);
        getServer().getPluginManager().registerEvents(new ItemDropListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawnListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        getServer().getPluginManager().registerEvents(new WeatherChangeListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerPickupItemListener(), this);
        getServer().getPluginManager().registerEvents(new LanguageListener(), this);
        getServer().getPluginManager().registerEvents(new WorldInitListener(), this);
        getServer().getPluginManager().registerEvents(new NickListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerBedEnterListener(), this);
        getServer().getPluginManager().registerEvents(new AsyncPlayerChatListener(), this);
    }

    private void loadCommands() {
        getCommand("build").setExecutor(new BuildCommand());
        getCommand("stats").setExecutor(new StatsCommand());
        getCommand("teaming").setExecutor(new TeamingCommand());
        getCommand("vote").setExecutor(new VoteCommand());
        getCommand("stats").setTabCompleter(new StatsCommand());
    }

    private void loadUpdater() {
        rankingUpdater.scheduleAtFixedRate(() -> {
            for (BuildFFAPlayer buildFFAPlayer : PlayerManager.getAllPlayers()) {
                statsDatabase.updatePlayer(buildFFAPlayer);
            }
        }, 0, 1, TimeUnit.HOURS);

        ScoreboardExecutor.startUpdater();
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("_______________________________________________________________");
        Bukkit.getLogger().info("|                                                             |");
        Bukkit.getLogger().info("| [BuildFFA] Plugin is stopping...                            |");
        Bukkit.getLogger().info("|                                                             |");
        Bukkit.getLogger().info("_______________________________________________________________");

        rankingUpdater.shutdown();
        ScoreboardExecutor.stopUpdater();
    }
}
