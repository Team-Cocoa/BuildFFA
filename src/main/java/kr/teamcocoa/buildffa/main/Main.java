package kr.teamcocoa.buildffa.main;

import kr.teamcocoa.buildffa.commands.*;
import kr.teamcocoa.buildffa.kit.KitData;
import kr.teamcocoa.buildffa.utils.*;
import kr.teamcocoa.buildffa.utils.Stats;
import kr.teamcocoa.buildffa.world.MapVote;
import kr.teamcocoa.buildffa.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import kr.teamcocoa.buildffa.kit.BffaPlayer;
import kr.teamcocoa.buildffa.world.WorldData;
import kr.teamcocoa.buildffa.listener.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Main instance;
    public static HashMap<Player, BffaPlayer> playerData = new HashMap<>();
    public static WorldData worldData = new WorldData();
    public MYSQL mysql;
    public Stats stats;
    public KitData kitData;
    public ScoreboardManager scoreboardManager;
    public static boolean teaming;

    @Override
    public void onEnable() {
        System.out.println(" _____________________________________________________________");
        System.out.println("|                                                             |");
        System.out.println("| [BuildFFA] Plugin is Loading...                             |");
        instance = this;
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
        scoreboardManager.ScoreboardUpdater();

        // Task to prevent from the connection disconnected
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            try(    PreparedStatement preparedStatement = mysql.getPreparedStatement("select 1");
                    ResultSet rs = preparedStatement.executeQuery()) {

            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }, 0L, 20 * 60 * 60);
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

    public void loadCommands() {
        getCommand("build").setExecutor(new Build());
        getCommand("stats").setExecutor(new kr.teamcocoa.buildffa.commands.Stats());
        getCommand("teaming").setExecutor(new Teaming());
        getCommand("vote").setExecutor(new Vote());

        getCommand("stats").setTabCompleter(new kr.teamcocoa.buildffa.commands.Stats());
    }

    @Override
    public void onDisable() {
        System.out.println(" _____________________________________________________________");
        System.out.println("|                                                             |");
        System.out.println("| [BuildFFA] Plugin is stopping...                            |");
        mysql.disconnect();
        System.out.println("|_____________________________________________________________|");
        Main.worldData.removeBlocks();
    }

    public static Main getInstance() {
        return instance;
    }

    public static String getPrefix() {
        return StringUtils.color("&a[&dBuildFFA&a] ");
    }
}
