package kr.teamcocoa.buildffa.main;

import kr.teamcocoa.buildffa.commands.*;
import kr.teamcocoa.buildffa.listener.*;
import kr.teamcocoa.buildffa.database.BuildFFADatabase;
import kr.teamcocoa.buildffa.model.BuildFFAPlayer;
import kr.teamcocoa.buildffa.kit.KitData;
import kr.teamcocoa.buildffa.utils.ScoreboardManager;
import kr.teamcocoa.buildffa.database.StatsDatabase;
import kr.teamcocoa.buildffa.utils.StringUtils;
import kr.teamcocoa.buildffa.world.MapVote;
import kr.teamcocoa.buildffa.world.WorldData;
import kr.teamcocoa.buildffa.world.WorldManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class BuildFFA extends JavaPlugin {

    public static final String PREFIX = StringUtils.color("&a[&dBuildFFA&a] ");

    @Getter
    private static BuildFFA instance;

    public static HashMap<Player, BuildFFAPlayer> playerData = new HashMap<>();
    public static WorldData worldData = new WorldData();
    public StatsDatabase statsDatabase;
    public KitData kitData;
    public ScoreboardManager scoreboardManager;
    public static boolean teaming;

    @Override
    public void onEnable() {
        instance = this;

        Bukkit.getLogger().info("_______________________________________________________________");
        Bukkit.getLogger().info("|                                                             |");
        Bukkit.getLogger().info("| [BuildFFA] Plugin is Loading...                             |");
        Bukkit.getLogger().info("|                                                             |");
        Bukkit.getLogger().info("|_____________________________________________________________|");

        BuildFFADatabase.init();
        statsDatabase = new StatsDatabase();
        kitData = new KitData();
        teaming = false;
        scoreboardManager = new ScoreboardManager();
        loadListeners();
        loadCommands();

        WorldManager worldManager = WorldManager.getInstance();
        String map = MapVote.getInstance().getRandomMap();
        worldManager.setCurrentMapName(map);
        WorldManager.getInstance().loadWorld(map);
        worldManager.cloneWorld(map);
        Bukkit.getWorld(map).loadChunk(worldManager.getSpawnByName(map).getChunk());
        WorldManager.getInstance().mapChangeUpdater();

        statsDatabase.updateRanking();
        scoreboardManager.ScoreboardUpdater();

    }

    private void init() {
        loadListeners();
        loadCommands();
        BuildFFADatabase.init();
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
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawnListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);
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

    @Override
    public void onDisable() {
        System.out.println(" _____________________________________________________________");
        System.out.println("|                                                             |");
        System.out.println("| [BuildFFA] Plugin is stopping...                            |");
        System.out.println("|_____________________________________________________________|");
    }
}
