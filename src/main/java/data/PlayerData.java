package data;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerData {
    public HashMap<Player, Integer> playerKillStreak = new HashMap<>();
    public HashMap<Player, Long> throwPeralTime = new HashMap<>();
    public HashMap<Player, Long> latestDeadTime = new HashMap<>();
    public HashMap<Player, Boolean> playerBuild = new HashMap<>();

    public boolean isPlayerBuild(Player player){
        try{
            boolean returnValue = playerBuild.get(player);
            if(!returnValue){
                return false;
            }
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    public void addPlayerBuildStatus(Player player){
        playerBuild.put(player, true);
    }

    public void removePlayerBuildStatue(Player player){
        playerBuild.remove(player);
    }

    public void addKillStreak(Player player){
        int currentKillStreak = (playerKillStreak.get(player)) + 1;
        if(currentKillStreak % 3 == 0){ // 3킬 마다 엔더펄 하나씩 지급
            /*
            * 여기에 엔더펄 지급하는 코드 작성
            * */
        }
    }

    public void removeKillStreak(Player player){
        playerKillStreak.put(player, 0);
    }
}
