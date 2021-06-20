package kr.teamcocoa.buildffa.main;

import kr.teamcocoa.buildffa.block.RemoveBlockAnimation;
import kr.teamcocoa.buildffa.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import kr.teamcocoa.buildffa.kit.BffaPlayer;
import kr.teamcocoa.buildffa.world.WorldData;
import kr.teamcocoa.buildffa.listener.*;
import kr.teamcocoa.buildffa.utils.Config;
import kr.teamcocoa.buildffa.utils.Locations;
import kr.teamcocoa.buildffa.utils.MYSQL;
import kr.teamcocoa.buildffa.utils.Ranking;
import kr.teamcocoa.buildffa.utils.ScoreboardManager;
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
  
  public void onEnable() {
    System.out.println(" _____________________________________________________________");
    System.out.println("|                                                             |");
    System.out.println("| [BuildFFA] Plugin is Loading...                             |");
    mainInstance = this;
    Config.loadFiles();
    createConfigs();
    MYSQL.connect();
    System.out.println("|_____________________________________________________________|");
    loadListeners();
    loadCommands();

    new RemoveBlockAnimation().runTaskTimer(this, 0L, 10L);

    if (MYSQL.isConnected()) {
      MYSQL.update("CREATE TABLE IF NOT EXISTS Stats(UUID varchar(64), KILLS int, DEATHS int);");
    }
    if (!Config.config.getString("startmap").equals("none")) {
      Locations.setCurrentMap(Config.config.getString("startmap").replaceAll("&", "§"));
    }
    if (Config.config.getBoolean("scoreboard")) {
      ScoreboardManager.ScoreboardUpdater();
    }
    if (Config.config.getBoolean("stats") && Config.config.getBoolean("mysql.support") && MYSQL.isConnected()) {
      Ranking.update();
    }
  }

  public void loadListeners() {
    getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
    getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
    getServer().getPluginManager().registerEvents(new EnderPeralCancelListener(), this);
    getServer().getPluginManager().registerEvents(new EntityDamageListener(), this);
    getServer().getPluginManager().registerEvents(new FoodLevelChangeListener(), this);
    getServer().getPluginManager().registerEvents(new InteractListener(), this);
    getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
    getServer().getPluginManager().registerEvents(new ItemDropListener(), this);
    getServer().getPluginManager().registerEvents(new JoinListener(), this);
    getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
    getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
    getServer().getPluginManager().registerEvents(new PlayerRespawnListener(), this);
    getServer().getPluginManager().registerEvents(new QuitListener(), this);
    getServer().getPluginManager().registerEvents(new WeatherChangeListener(), this);
    getServer().getPluginManager().registerEvents(new InventoryDragListener(), this);
    getServer().getPluginManager().registerEvents(new PlayerPickupItemListener(), this);
  }

  public void loadCommands(){
    getCommand("setspawn").setExecutor((CommandExecutor)new SetSpawn());
    getCommand("setstartmap").setExecutor((CommandExecutor)new SetStartmap());
    getCommand("setdeathheight").setExecutor((CommandExecutor)new SetDeathheight());
    getCommand("setarenaheight").setExecutor((CommandExecutor)new SetArenaheight());
    getCommand("build").setExecutor((CommandExecutor)new Build());
    getCommand("stats").setExecutor((CommandExecutor)new Stats());
    getCommand("teaming").setExecutor((CommandExecutor)new Teaming());
    //getCommand("item").setExecutor((CommandExecutor)new Item());
  }

  public void onDisable() {
    System.out.println(" _____________________________________________________________");
    System.out.println("|                                                             |");
    System.out.println("| [BuildFFA] Plugin is stopping...                            |");
    MYSQL.disconnect();
    System.out.println("|_____________________________________________________________|");
    Main.worldData.removeBlocks();
  }

  public static void printMemory() {
    Runtime r = Runtime.getRuntime();
//    long memUsed = (r.totalMemory() - r.freeMemory()) / 1048576; //Converting
    Bukkit.getLogger().info(String.valueOf(r.totalMemory() - r.freeMemory()));
  }

  public static Main inst() {
    return mainInstance;
  }
  
  public static String getPrefix() {
    return Config.messages.getString("prefix").replaceAll("&", "§");
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
