package kr.teamcocoa.buildffa.world;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.UUID;

public class WorldManager {

    private static WorldManager instance = null;
    private MultiverseCore core = null;
    private String currentWorldUUID = null;
    private String currentMapName = null;

    MVWorldManager worldManager = null;

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

    public void cloneWorld(String name) {
        currentWorldUUID = UUID.randomUUID().toString();
        worldManager.cloneWorld(name, currentWorldUUID);
    }

    public void unloadWorld() {
        worldManager.unloadWorld(currentWorldUUID);
    }

    public void deleteWorld() {
        worldManager.deleteWorld(currentWorldUUID);
    }

    public String getCurrentWorldUUID() {
        return currentWorldUUID;
    }





}
