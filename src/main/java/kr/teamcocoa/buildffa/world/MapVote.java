package kr.teamcocoa.buildffa.world;

import kr.teamcocoa.buildffa.enums.MessageEnum;
import kr.teamcocoa.buildffa.utils.LangUtils;
import kr.teamcocoa.buildffa.world.maps.Maps;
import kr.teamcocoa.core.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.security.SecureRandom;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Getter
public class MapVote {

    private ArrayList<Maps> mapList = new ArrayList<>();
    private HashMap<Maps, Integer> voteList = new HashMap<>();
    private HashMap<Player, Maps> playerVoteList = new HashMap<>();

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
        mapList.add(Maps.CWBW);
        mapList.add(Maps.SPRING);
        mapList.add(Maps.FLATLAND);
        mapList.add(Maps.ARCHITECTURE);

        voteList.put(Maps.CWBW, 0);
        voteList.put(Maps.SPRING, 0);
        voteList.put(Maps.FLATLAND, 0);
        voteList.put(Maps.ARCHITECTURE, 0);
    }

    public void addVote(Player player, Maps maps) {
        if(mapList.contains(maps)) {
            if(playerVoteList.containsKey(player)) {
                removeVote(player, playerVoteList.get(player));
            }
            playerVoteList.put(player, maps);
            int votes = voteList.get(maps);
            voteList.put(maps, ++votes);
        }
    }

    public void removeVote(Player player, Maps maps) {
        if(mapList.contains(maps)) {
            if(playerVoteList.containsKey(player)) {
                int votes = voteList.get(maps);
                if(votes - 1 >= 0) {
                    playerVoteList.remove(player);
                    voteList.put(maps, --votes);
                }
            }
        }
    }

    public Maps getWhatPlayerVoted(Player player) {
        return playerVoteList.getOrDefault(player, null);
    }

    public int getVote(Maps maps) {
        if(mapList.contains(maps)) {
            return voteList.get(maps);
        }
        return 0;
    }

    public void resetVotes() {
        voteList.put(Maps.CWBW, 0);
        voteList.put(Maps.SPRING, 0);
        voteList.put(Maps.FLATLAND, 0);
        voteList.put(Maps.ARCHITECTURE, 0);
    }

    public Maps getMostVoted() {
        SecureRandom random = new SecureRandom();

        Maps[] list = ((Maps[]) voteList.entrySet().stream()
                .sorted(Map.Entry.<Maps, Integer>comparingByValue().reversed())
                .filter(mapsIntegerEntry -> mapsIntegerEntry.getKey() != WorldManager.getInstance().getCurrentMap().getMaps())
                .map(mapsIntegerEntry -> mapsIntegerEntry.getKey()).toArray());

        if(voteList.get(list[0]) == voteList.get(list[1]) &&
                voteList.get(list[1]) == voteList.get(list[2])) {
            return list[random.nextInt(list.length - 1)];
        }
        else {
            return list[0];
        }

    }

    public Maps getRandomMap() {
        SecureRandom random = new SecureRandom();
        return mapList.get(random.nextInt(mapList.size() - 1));
    }

    public String getVotingStatusMessage(Player player) {
        StringBuilder sb = new StringBuilder();
        sb.append(LangUtils.getMessage(player, MessageEnum.VOTE_CURRENT_INFO));
        sb.append(StringUtils.color("&r"));
        for(Maps maps : mapList) {
            sb.append("\n          ");
            sb.append(MessageFormat.format(LangUtils.getMessage(player, MessageEnum.VOTE_NUMBER_OF_VOTE),
                            maps.getName(),
                            voteList.get(maps)));
            sb.append(StringUtils.color("&r"));
        }
        sb.append("\n          " + LangUtils.getMessage(player, MessageEnum.VOTE_COMMAND) + StringUtils.color("&r"));
        sb.append(StringUtils.color("&e&l----------------------------------"));
        return sb.toString();
    }


}
