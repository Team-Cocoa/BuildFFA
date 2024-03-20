package kr.teamcocoa.buildffa.models;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class BuildFFAPlayerManager {

    @Getter
    private static Map<Player, BuildFFAPlayer> playerTable = new HashMap<>();

    public static boolean addPlayer(Player player) {
        if(playerTable.containsKey(player)) {
            return false;
        }
        BuildFFAPlayer buildFFAPlayer = new BuildFFAPlayer(player);
        playerTable.put(player, buildFFAPlayer);
        return true;
    }

    public static BuildFFAPlayer getPlayer(Player player) {
        return playerTable.getOrDefault(player, null);
    }

    public static boolean removePlayer(Player player) {
        if(!playerTable.containsKey(player)) {
            return false;
        }
        playerTable.remove(player);
        return true;
    }

}
