package kr.teamcocoa.buildffa.world;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimeProperty;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import kr.teamcocoa.buildffa.enums.MessageEnum;
import kr.teamcocoa.buildffa.kit.BffaPlayer;
import kr.teamcocoa.buildffa.main.Main;
import kr.teamcocoa.buildffa.utils.Bar;
import kr.teamcocoa.buildffa.utils.LangUtils;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Locale;

public class WorldManager {

    private static WorldManager instance = null;
    private HashMap<String, SlimeWorld> worlds = new HashMap<>();
//    private MultiverseCore core = null;
    private SlimePlugin core;
    private SlimeLoader loader;
    private String currentWorldUUID = null;
    private String currentMapName = null;
    private String temp;
    private int sec = 600;
//    private int i = (int)(Math.random() * 3) + 1;
    private boolean pvpAble = true;
    private boolean placeAble = true;

    public static WorldManager getInstance() {
        if(instance == null) {
            instance = new WorldManager();
            return instance;
        }
        return instance;
    }

    public void setCurrentMapName(String currentMapName) {
        this.currentMapName = currentMapName;
    }

    private WorldManager() {
//        core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
//        worldManager = core.getMVWorldManager();
        core = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
        loader = core.getLoader("file");
    }

    public String cloneWorld(String name) {
        temp = name;
//        if(worldManager.cloneWorld(name.toLowerCase(Locale.ROOT), "b-" + name)) {
//            return "b-" + name;
//        }
        return null;
    }

    public void loadWorld(String name) {
//        worldManager.loadWorld(name.toLowerCase(Locale.ROOT));
        try {
            if(!worlds.containsKey(name)) {
                SlimePropertyMap map = new SlimePropertyMap();
                map.setInt(SlimeProperties.SPAWN_X, 0);
                map.setInt(SlimeProperties.SPAWN_Y, 218);
                map.setInt(SlimeProperties.SPAWN_Z, 0);
                map.setString(SlimeProperties.DIFFICULTY, "easy");
                map.setBoolean(SlimeProperties.ALLOW_ANIMALS, false);
                map.setBoolean(SlimeProperties.ALLOW_MONSTERS, false);
                map.setBoolean(SlimeProperties.PVP, true);
                SlimeWorld world = core.loadWorld(loader, name, false, map);
                worlds.put(name, world);
                core.generateWorld(world);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void unloadWorld(String string) {
//        worldManager.unloadWorld(string.toLowerCase(Locale.ROOT));
//        WorldServer world = ((CraftWorld) Bukkit.getWorld(string)).getHandle();
        Bukkit.unloadWorld(string, true);


//        MinecraftServer server = MinecraftServer.getServer();
//        server.worlds.remove(world);

//        System.gc();

    }


    public String getCurrentWorldUUID() {
        return currentWorldUUID;
    }

    public void mapChange(String name) {
        if(temp != currentMapName) {
            String t = currentMapName;
            currentMapName = temp;
            Location spawn = getSpawnByName(name);
            Main.worldData.removeBlocks();
            long deadTime = System.currentTimeMillis();
            Bukkit.getScheduler().runTaskLater(Main.inst(), () -> {
                for(Player player : Bukkit.getOnlinePlayers()) {
                    player.teleport(spawn);
                    BffaPlayer bffaPlayer = Main.playerData.get(player);
                    bffaPlayer.setJoinInventory();
                    bffaPlayer.setInGame(false);
                    bffaPlayer.setLatestDeadTime(deadTime);
                    Main.playerData.put(player, bffaPlayer);
                }
                placeAble = true;
            }, 0L);
//            Bukkit.getScheduler().runTaskLater(Main.inst(), () -> {
//                unloadWorld(t);
//            }, 5L);
        }
//        else {
//            for(Player player : Bukkit.getOnlinePlayers()) {
//                player.sendMessage(LangUtils.getMessage(player, MessageEnum.VOTE_MAP_NOT_SELECTED));
//            }
//        }
    }

    public void mapChangeUpdater() {

        Bukkit.getScheduler().runTaskTimer(Main.inst(), () -> {
            --sec;
            LocalTime localTime = LocalTime.ofSecondOfDay(sec);
            String time = localTime.toString();
            MapVote mapVote = MapVote.getInstance();
            for(Player player : Bukkit.getOnlinePlayers()) {
                Bar.sendDefaultBar(player, time);
            }
            if(sec == 5) {
//                Location spawn = Locations.getSpawnLocation(Locations.getMapNameByInt(i = i + 1 < 4 ? i + 1 : 1));
//                if(!spawn.getChunk().isLoaded()) {
//                    spawn.getChunk().load();
//                }
                pvpAble = false;
                placeAble = false;
            }
            switch(sec) {
                case 600:
                case 300:
                case 180:
                case 60:
                case 30:
                    sendCountdownMessage();
                    for(Player player : Bukkit.getOnlinePlayers()) {
                        player.sendMessage(mapVote.getVotingStatusMessage(player));
                    }
                    break;
                case 310:
                case 70:
                case 40:
                case 20:
                case 15:
                case 14:
                case 13:
                case 12:
                case 11:
                    sendVoteEndMessage();
                    break;
                case 10:
                    mapVote.setVoteAble(false);
                    String map = mapVote.getMostVoted();
                    Bukkit.getScheduler().runTaskLaterAsynchronously(Main.inst(), () -> {
                        loadWorld(map);
                        cloneWorld(map);
                    }, 1L);
                    for(Player player : Bukkit.getOnlinePlayers()) {
                        player.sendMessage(LangUtils.getMessage(player, MessageEnum.VOTE_ENDED));
                        player.sendMessage(LangUtils.getMessage(player, MessageEnum.VOTE_MAP_SELECTED).replace("%map%", map));
                    }
                case 5:
                case 4:
                case 3:
                case 2:
                    sendCountdownMessage();
//                    for(Player player : Bukkit.getOnlinePlayers()) {
//                        mapVote.getVotingStatusMessage(player);
//                    }
                    break;
                case 1:
                    sendCountdownMessage();
//                    for(Player player : Bukkit.getOnlinePlayers()) {
//                        mapVote.getVotingStatusMessage(player);
//                    }
                    Main.worldData.removeBlocks();
                    break;
                case 0:
                    sec = 600;
//                    i = i + 1 < 4 ? i + 1 : 1;
//                    Locations.MapChange(i);
                    pvpAble = true;
                    mapVote.setVoteAble(true);
                    mapVote.resetVotes();
                    Bukkit.getScheduler().runTaskLaterAsynchronously(Main.inst(), () -> mapChange(temp), 1L);
                    break;
                default:
                    break;
            }
        }, 0L, 20L);
    }

    public void sendAllPlayer(MessageEnum node, int sec) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(LangUtils.getMessage(player, node).replace("%time%", String.valueOf(sec)));
        }
    }

    public void sendAllVotePlayer(MessageEnum node, int sec) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(LangUtils.getMessage(player, node).replace("%time%", String.valueOf(sec)));
        }
    }

    public void sendCountdownMessage() {
        if(sec >= 60) {
            sendAllPlayer(sec / 60 == 1 ? MessageEnum.MAP_CHANGE_MINUTE : MessageEnum.MAP_CHANGE_MINUTES, sec / 60);
        }
        else {
            sendAllPlayer(sec != 1 ? MessageEnum.MAP_CHANGE_SECONDS : MessageEnum.MAP_CHANGE_SECOND, sec);
        }
    }

    public void sendVoteEndMessage() {
        if(sec - 10 >= 60) {
            sendAllVotePlayer((sec - 10) / 60 == 1 ? MessageEnum.VOTE_END_MINUTE : MessageEnum.VOTE_END_MINUTES, (sec - 10) / 60);
        }
        else {
            sendAllVotePlayer((sec - 10) != 1 ? MessageEnum.VOTE_END_SECONDS : MessageEnum.VOTE_END_SECOND, sec - 10);
        }
    }

    public Location getSpawnByName(String name) {
        World world = null;
        switch(name){
            case "CWBW" :
            case "Spring":
            case "FlatLand":
            case "Architecture":
//                world = Bukkit.getWorld("b-" + name);
                world = Bukkit.getWorld(name);
                Location location = new Location(world,0.5F, 218F, 0.5F, 0F, 90F);
                return location;
        }
        return null;
    }

    public String getCurrentMap() {
        return currentMapName;
    }

    public boolean isPlaceAble() {
        return placeAble;
    }

    public boolean isPvpAble() {
        return pvpAble;
    }

    public void setPvpAble(boolean pvpAble) {
        this.pvpAble = pvpAble;
    }

    public void setPlaceAble(boolean placeAble) {
        this.placeAble = placeAble;
    }

    public double getArenaHeight() {
        //        String map = WorldManager.getInstance().getCurrentMap();

        //usually 217
        return 207.0;
    }

    public double getDeathHeight() {
        String map = WorldManager.getInstance().getCurrentMap();
        //usually 0
        switch(map.toLowerCase(Locale.ROOT)) {
            case "cwbw":
            case "spring":
                return 0.0;
            case "flatland":
                return 85.0;
            case "architecture":
                return 64.0;
        }
        return 0.0;
    }
}
