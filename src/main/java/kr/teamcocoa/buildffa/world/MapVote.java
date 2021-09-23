package kr.teamcocoa.buildffa.world;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class MapVote {
    private ArrayList<String> mapList = new ArrayList<>();
    private HashMap<String, Integer> voteList = new HashMap<>();
    private HashMap<Player, String> playerVoteList = new HashMap<>();
    private static MapVote instance = null;

    public static MapVote getInstance() {
        if(instance == null) {
            instance = new MapVote();
            return instance;
        }
        return instance;
    }

    public MapVote() {
        mapList.add("CWBW");
        mapList.add("Spring");
        mapList.add("FlatLand");
        mapList.add("Architecture");
        voteList.put("CWBW", 0);
        voteList.put("Spring", 0);
        voteList.put("FlatLand", 0);
        voteList.put("Architecture", 0);
    }

    public void addVote(Player player, String name) {
        if(mapList.contains(name)) {
            if(playerVoteList.containsKey(player)) {
                removeVote(player, name);
            }
            playerVoteList.put(player, name);
            int votes = voteList.get(name);
            ++votes;
            voteList.put(name, votes);
        }
    }

    public void removeVote(Player player, String name) {
        if(mapList.contains(name)) {
            if(playerVoteList.containsKey(player)) {
                int votes = voteList.get(name);
                if(votes - 1 >= 0) {
                    playerVoteList.remove(player);
                    --votes;
                    voteList.put(name, votes);
                }
            }
        }
    }

    public String getWherePlayerVoted(Player player) {
        if(playerVoteList.containsKey(player)) {
            return playerVoteList.get(player);
        }
        else {
            return null;
        }
    }

    public int getVote(String name) {
        if(mapList.contains(name)) {
            return voteList.get(name);
        }
        return 0;
    }

    public ArrayList<String> getMapList() {
        return mapList;
    }

    public void resetVotes() {
        voteList.put("CWBW", 0);
        voteList.put("Spring", 0);
        voteList.put("FlatLand", 0);
        voteList.put("Architecture", 0);
    }



}
