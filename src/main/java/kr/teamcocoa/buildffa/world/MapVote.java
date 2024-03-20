package kr.teamcocoa.buildffa.world;

import kr.teamcocoa.buildffa.enums.MessageEnum;
import kr.teamcocoa.buildffa.utils.LangUtils;
import kr.teamcocoa.core.utils.StringUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
public class MapVote {

    private ArrayList<String> mapList = new ArrayList<>();
    private HashMap<String, Integer> voteList = new HashMap<>();
    private HashMap<Player, String> playerVoteList = new HashMap<>();

    @Setter
    private boolean voteAble = true;

    private static MapVote instance = null;

    public static MapVote getInstance() {
        if(instance == null) {
            instance = new MapVote();
            return instance;
        }
        return instance;
    }

    private MapVote() {
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
                removeVote(player, playerVoteList.get(player));
            }
            playerVoteList.put(player, name);
            int votes = voteList.get(name);
            votes++;
            voteList.put(name, votes);
        }
    }

    public void removeVote(Player player, String name) {
        if(mapList.contains(name)) {
            if(playerVoteList.containsKey(player)) {
                int votes = voteList.get(name);
                if(votes - 1 >= 0) {
                    playerVoteList.remove(player);
                    votes--;
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

    public void resetVotes() {
        voteList.put("CWBW", 0);
        voteList.put("Spring", 0);
        voteList.put("FlatLand", 0);
        voteList.put("Architecture", 0);
    }

    public String getMostVoted() {
        Random random = new Random();
        int most = 0;
        List<String> maps = new ArrayList<>();
        for(String string : mapList) {
            if(most < voteList.get(string) && !string.equals(WorldManager.getInstance().getCurrentMap())) {
                most = voteList.get(string);
                Iterator<String> it = maps.iterator();
                while(it.hasNext()) {
                    it.next();
                    it.remove();
                }
                maps.add(string);
            }
            else if(most == voteList.get(string) && !string.equals(WorldManager.getInstance().getCurrentMap())) {
                maps.add(string);
            }
        }
        Bukkit.getLogger().info(Arrays.toString(maps.toArray()));
        int r;
        if(maps.size() > 1) {
            r = random.nextInt(maps.size() - 1);
        }
        else {
            r = 0;
        }

        return maps.get(r);
    }

    public String getRandomMap() {
        Random random = new Random();
        return mapList.get(random.nextInt(mapList.size() - 1));
    }

    public String getVotingStatusMessage(Player player) {
        StringBuilder sb = new StringBuilder();
        sb.append(LangUtils.getMessage(player, MessageEnum.VOTE_CURRENT_INFO));
        sb.append(StringUtils.color("&r"));
        for(String string : mapList) {
            sb.append("\n          ");
            sb.append(LangUtils.getMessage(player, MessageEnum.VOTE_NUMBER_OF_VOTE).replace("%map%", string).replace("%int%", String.valueOf(voteList.get(string))));
            sb.append(StringUtils.color("&r"));
        }
        sb.append("\n          " + LangUtils.getMessage(player, MessageEnum.VOTE_COMMAND) + StringUtils.color("&r"));
        sb.append(StringUtils.color("&e&l----------------------------------"));
        return sb.toString();
    }


}
