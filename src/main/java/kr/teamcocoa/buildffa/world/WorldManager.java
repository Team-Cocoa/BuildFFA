package kr.teamcocoa.buildffa.world;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import kr.teamcocoa.buildffa.enums.MessageEnum;
import kr.teamcocoa.buildffa.kit.BffaPlayer;
import kr.teamcocoa.buildffa.main.Main;
import kr.teamcocoa.buildffa.utils.Bar;
import kr.teamcocoa.buildffa.utils.LangUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.time.LocalTime;
import java.util.Locale;

public class WorldManager {

    private static WorldManager instance = null;
    private MultiverseCore core = null;
    private String currentWorldUUID = null;
    private String currentMapName = null;
    private String temp;
    private MVWorldManager worldManager = null;
    private int sec = 60;
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

    private WorldManager() {
        core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
        worldManager = core.getMVWorldManager();
    }

    public String cloneWorld(String name) {
        temp = name;
//        if(worldManager.cloneWorld(name.toLowerCase(Locale.ROOT), "b-" + name)) {
//            return "b-" + name;
//        }
        return null;
    }

    public void loadWorld(String name) {
        worldManager.loadWorld(name);
    }

    public void unloadWorld() {
        worldManager.unloadWorld(currentMapName.toLowerCase(Locale.ROOT));
    }

    public void unloadWorld(String string) {
        worldManager.unloadWorld(string.toLowerCase(Locale.ROOT));
    }

    public void deleteWorld() {
        worldManager.deleteWorld(currentMapName);
    }

    public String getCurrentWorldUUID() {
        return currentWorldUUID;
    }

    public void mapChange(String name) {
        if(temp != currentMapName) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(Main.inst(), () -> {
                unloadWorld();
            }, 0L);
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
            }, 0L);
        }
        else {
            for(Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(LangUtils.getMessage(player, MessageEnum.VOTE_MAP_NOT_SELECTED));
            }
        }
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
                        mapVote.getVotingStatusMessage(player);
                    }
                    break;
                case 10:
                    mapVote.setVoteAble(false);
                    String map = mapVote.getMostVoted();
                    Bukkit.getScheduler().runTaskLaterAsynchronously(Main.inst(), () -> {
                        loadWorld(map);
                        cloneWorld(map);
                    }, 0L);
                case 5:
                case 4:
                case 3:
                case 2:
                    sendCountdownMessage();
                    for(Player player : Bukkit.getOnlinePlayers()) {
                        mapVote.getVotingStatusMessage(player);
                    }
                    break;
                case 1:
                    sendCountdownMessage();
                    for(Player player : Bukkit.getOnlinePlayers()) {
                        mapVote.getVotingStatusMessage(player);
                    }
                    Main.worldData.removeBlocks();
                    break;
                case 0:
                    sec = 60;
//                    i = i + 1 < 4 ? i + 1 : 1;
//                    Locations.MapChange(i);
                    mapChange(temp);
                    pvpAble = true;
                    placeAble = true;
                    mapVote.setVoteAble(true);
                    mapVote.resetVotes();
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

    public void sendCountdownMessage() {
        if(sec >= 60) {
            sendAllPlayer(sec / 60 == 1 ? MessageEnum.MAP_CHANGE_MINUTE : MessageEnum.MAP_CHANGE_MINUTES, sec / 60);
        }
        else {
            sendAllPlayer(sec != 1 ? MessageEnum.MAP_CHANGE_SECONDS : MessageEnum.MAP_CHANGE_SECOND, sec);
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
}
