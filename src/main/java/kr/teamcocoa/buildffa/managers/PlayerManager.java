package kr.teamcocoa.buildffa.managers;

import kr.teamcocoa.buildffa.model.BuildFFAPlayer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerManager {

    private static ConcurrentHashMap<Player, BuildFFAPlayer> map = new ConcurrentHashMap<>();

    public static BuildFFAPlayer createPlayer(Player player) {
        if(map.containsKey(player)) {
            return null;
        }
        BuildFFAPlayer buildFFAPlayer = new BuildFFAPlayer(player);
        map.put(player, buildFFAPlayer);
        return buildFFAPlayer;
    }

    public static BuildFFAPlayer getPlayer(Player player) {
        return map.getOrDefault(player, null);
    }

    public static void removePlayer(Player player) {
        if(!map.containsKey(player)) {
            return;
        }
        map.remove(player);
    }

    public static Collection<BuildFFAPlayer> getAllPlayers() {
        return map.values();
    }

}
