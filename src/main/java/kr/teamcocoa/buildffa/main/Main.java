package kr.teamcocoa.buildffa.main;

import kr.teamcocoa.buildffa.block.RemoveBlockAnimation;
import kr.teamcocoa.buildffa.commands.*;
import kr.teamcocoa.buildffa.kit.KitData;
import kr.teamcocoa.buildffa.utils.*;
import kr.teamcocoa.buildffa.utils.Stats;
import kr.teamcocoa.buildffa.world.MapVote;
import kr.teamcocoa.buildffa.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import kr.teamcocoa.buildffa.kit.BffaPlayer;
import kr.teamcocoa.buildffa.world.WorldData;
import kr.teamcocoa.buildffa.listener.*;

import java.io.IOException;
import java.util.HashMap;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

public class Main extends JavaPlugin {
    private static Main mainInstance;
    public static HashMap<Player, BffaPlayer> playerData = new HashMap<>();
    public static WorldData worldData = new WorldData();
    public MYSQL mysql;
    public Stats stats;
    public KitData kitData;
    public ScoreboardManager scoreboardManager;
    public static boolean teaming;

    public void onEnable() {
        System.out.println(" _____________________________________________________________");
        System.out.println("|                                                             |");
        System.out.println("| [BuildFFA] Plugin is Loading...                             |");
        mainInstance = this;
        Config.loadFiles();
        createConfigs();
        mysql = new MYSQL();
        stats = new Stats();
        kitData = new KitData();
        teaming = false;
        scoreboardManager = new ScoreboardManager();
        mysql.connect();
        System.out.println("|_____________________________________________________________|");
        loadListeners();
        loadCommands();

        WorldManager worldManager = WorldManager.getInstance();
        String map = MapVote.getInstance().getRandomMap();
        worldManager.setCurrentMapName(map);
        WorldManager.getInstance().loadWorld(map);
        worldManager.cloneWorld(map);
        Bukkit.getWorld(map).loadChunk(worldManager.getSpawnByName(map).getChunk());
        WorldManager.getInstance().mapChangeUpdater();

        stats.updateRanking();
//        new RemoveBlockAnimation().runTaskTimer(this, 0L, 10L);
        scoreboardManager.ScoreboardUpdater();

    }

    public void loadListeners() {
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new EnderPeralCancelListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageListener(), this);
        getServer().getPluginManager().registerEvents(new FoodLevelChangeListener(), this);
        getServer().getPluginManager().registerEvents(new InteractListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryCloseListener(), this);
        getServer().getPluginManager().registerEvents(new ItemDropListener(), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
//        getServer().getPluginManager().registerEvents(new PlayerRespawnListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);
        getServer().getPluginManager().registerEvents(new WeatherChangeListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerPickupItemListener(), this);
        getServer().getPluginManager().registerEvents(new LanguageListener(), this);
        getServer().getPluginManager().registerEvents(new WorldInitListener(), this);
        getServer().getPluginManager().registerEvents(new EntityShootBowListener(), this);
        getServer().getPluginManager().registerEvents(new NickListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerBedEnterListener(), this);
    }

    public void loadCommands() {
        getCommand("build").setExecutor(new Build());
        getCommand("stats").setExecutor(new kr.teamcocoa.buildffa.commands.Stats());
        getCommand("teaming").setExecutor(new Teaming());
        getCommand("vote").setExecutor(new Vote());
    }

    public void onDisable() {
        System.out.println(" _____________________________________________________________");
        System.out.println("|                                                             |");
        System.out.println("| [BuildFFA] Plugin is stopping...                            |");
        mysql.disconnect();
        System.out.println("|_____________________________________________________________|");
        Main.worldData.removeBlocks();
    }

    public static Main inst() {
        return mainInstance;
    }

    public static String getPrefix() {
        return StringUtils.color("&a[&dBuildFFA&a] ");
    }

    public static void createConfigs() {
        if (!Config.checkIfExtists(Config.configFile))
            try {
                Config.createFile(Config.configFile, Config.config);
            } catch (IOException e) {
                e.printStackTrace();
            }
        if (!Config.checkIfExtists(Config.messagesFile))
            try {
                Config.createFile(Config.messagesFile, Config.messages);
            } catch (IOException e) {
                e.printStackTrace();
            }
        if (!Config.checkIfExtists(Config.permissionsFile))
            try {
                Config.createFile(Config.permissionsFile, Config.permissions);
            } catch (IOException e) {
                e.printStackTrace();
            }
        if (!Config.checkIfExtists(Config.locationsFile))
            try {
                Config.createFile(Config.locationsFile, Config.locations);
            } catch (IOException e) {
                e.printStackTrace();
            }
        if (!Config.checkIfExtists(Config.playerFile))
            try {
                Config.createFile(Config.playerFile, Config.player);
            } catch (IOException e) {
                e.printStackTrace();
            }
        if (!Config.checkIfExtists(Config.statsFile))
            try {
                Config.createFile(Config.statsFile, Config.stats);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
