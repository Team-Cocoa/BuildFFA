package kr.teamcocoa.buildffa.world;

import kr.teamcocoa.buildffa.enums.MessageEnum;
import kr.teamcocoa.buildffa.enums.OtherEnum;
import kr.teamcocoa.buildffa.utils.LangUtils;
import kr.teamcocoa.buildffa.world.maps.BuildFFAMap;
import kr.teamcocoa.buildffa.world.maps.Maps;
import kr.teamcocoa.core.bukkit.utils.PacketUtils;
import kr.teamcocoa.core.utils.StringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MapChangeScheduler {

    private static MapChangeScheduler instance;

    public static MapChangeScheduler getInstance() {
        if(instance == null) {
            instance = new MapChangeScheduler();
        }
        return instance;
    }

    @Setter
    private int sec = 600;

    public void mapChangeUpdater() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            if (sec < 0) {
                return;
            }

            LocalTime localTime = LocalTime.ofSecondOfDay(sec);
            String time = localTime.toString();

            MapVote mapVote = MapVote.getInstance();
            BuildFFAMap currentMap = WorldManager.getInstance().getCurrentMap();

            for (Player player : Bukkit.getOnlinePlayers()) {

                PacketUtils.sendBar(
                        player,
                        StringUtils.color(
                                "&8» " + LangUtils.getMessage(player, OtherEnum.BAR_MAP) + " : &e" + WorldManager.getInstance().getCurrentMap().getMaps().getName()
                                        + " &r&8» " + LangUtils.getMessage(player, OtherEnum.BAR_TIME_LEFT) + " : &e"
                                        + time));
            }

            switch (sec) {
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
                    Maps maps = mapVote.getMostVoted();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendMessage(LangUtils.getMessage(player, MessageEnum.VOTE_ENDED));
                        player.sendMessage(MessageFormat.format(
                                LangUtils.getMessage(player, MessageEnum.VOTE_MAP_SELECTED),
                                maps.getName()));
                    }
                    break;


                case 5: {
                    sendCountdownMessage();
                    currentMap.mapSessionStop();
                    break;
                }
                case 4:
                case 3:
                case 2:
                    sendCountdownMessage();
                    break;

                case 1:
                    sendCountdownMessage();
                    currentMap.removeAllBlocks();
                    break;


                case 0:
                    WorldManager.getInstance().mapChange(MapVote.getInstance().getMostVoted());
                    mapVote.resetVotes();
                    mapVote.setVoteAble(true);
                    break;
            }
            sec--;
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void sendAllPlayer(MessageEnum node, int sec) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(MessageFormat.format(
                    LangUtils.getMessage(player, node),
                    sec));
        }
    }

    public void sendAllVotePlayer(MessageEnum node, int sec) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(MessageFormat.format(
                    LangUtils.getMessage(player, node),
                    sec));
        }
    }

    public void sendCountdownMessage() {
        if(sec >= 60) {
            sendAllPlayer(
                    (sec / 60 == 1)
                            ? MessageEnum.MAP_CHANGE_MINUTE
                            : MessageEnum.MAP_CHANGE_MINUTES,
                    sec / 60);
        }
        else {
            sendAllPlayer(
                    (sec != 1)
                            ? MessageEnum.MAP_CHANGE_SECONDS
                            : MessageEnum.MAP_CHANGE_SECOND,
                    sec);
        }
    }

    public void sendVoteEndMessage() {
        if(sec - 10 >= 60) {
            sendAllVotePlayer(
                    ((sec - 10) / 60 == 1)
                            ? MessageEnum.VOTE_END_MINUTE
                            : MessageEnum.VOTE_END_MINUTES,
                    (sec - 10) / 60);
        }
        else {
            sendAllVotePlayer(
                    ((sec - 10) != 1)
                            ? MessageEnum.VOTE_END_SECONDS
                            : MessageEnum.VOTE_END_SECOND,
                    sec - 10);
        }
    }

}
