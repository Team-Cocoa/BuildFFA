package kr.teamcocoa.buildffa.task;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.text.MessageFormat;
import java.time.LocalTime;
import java.util.logging.Level;
import kr.teamcocoa.buildffa.translation.LangUtils;
import kr.teamcocoa.buildffa.translation.MessageEnum;
import kr.teamcocoa.buildffa.translation.OtherEnum;
import kr.teamcocoa.buildffa.world.MapVote;
import kr.teamcocoa.buildffa.world.WorldManager;
import kr.teamcocoa.buildffa.world.maps.BuildFFAMap;
import kr.teamcocoa.buildffa.world.maps.Maps;
import kr.teamcocoa.core.bukkit.utils.PacketUtils;
import kr.teamcocoa.core.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Singleton
public class MapChangeTask implements Runnable {

  private final MapVote mapVote;

  private final WorldManager worldManager;

  private int sec = 600;

  @Inject
  public MapChangeTask(MapVote mapVote, WorldManager worldManager) {
    this.mapVote = mapVote;
    this.worldManager = worldManager;
  }

  public void resetTimer() {
    if (this.sec < 0) {
      this.sec = 600;
    }
  }

  @Override
  public void run() {
    if (sec < 0) {
      return;
    }

    // Keep counting down after a failure, otherwise the same second is retried forever
    try {
      tick();
    } catch (Throwable t) {
      Bukkit.getLogger()
          .log(Level.SEVERE, "[BuildFFA] Map change countdown failed at sec " + sec, t);
    }
    sec--;
  }

  private void tick() {
    LocalTime localTime = LocalTime.ofSecondOfDay(sec);
    String time = localTime.toString();

    BuildFFAMap currentMap = worldManager.getCurrentMap();

    for (Player player : Bukkit.getOnlinePlayers()) {

      PacketUtils.sendBar(
          player,
          StringUtils.color(
              "&8» "
                  + LangUtils.getMessage(player, OtherEnum.BAR_MAP)
                  + " : &e"
                  + worldManager.getCurrentMap().getMaps().getName()
                  + " &r&8» "
                  + LangUtils.getMessage(player, OtherEnum.BAR_TIME_LEFT)
                  + " : &e"
                  + time));
    }

    switch (sec) {
      case 600:
      case 300:
      case 180:
      case 60:
      case 30:
        sendCountdownMessage();
        for (Player player : Bukkit.getOnlinePlayers()) {
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
          player.sendMessage(
              MessageFormat.format(
                  LangUtils.getMessage(player, MessageEnum.VOTE_MAP_SELECTED), maps.getName()));
        }
        break;
      case 5:
        sendCountdownMessage();
        currentMap.mapSessionStop();
        break;
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
        worldManager.mapChange(mapVote.getMostVoted());
        mapVote.resetVotes();
        mapVote.setVoteAble(true);
        break;
    }
  }

  public void sendAllPlayer(MessageEnum node, int sec) {
    for (Player player : Bukkit.getOnlinePlayers()) {
      player.sendMessage(MessageFormat.format(LangUtils.getMessage(player, node), sec));
    }
  }

  public void sendAllVotePlayer(MessageEnum node, int sec) {
    for (Player player : Bukkit.getOnlinePlayers()) {
      player.sendMessage(MessageFormat.format(LangUtils.getMessage(player, node), sec));
    }
  }

  public void sendCountdownMessage() {
    if (sec >= 60) {
      sendAllPlayer(
          (sec / 60 == 1) ? MessageEnum.MAP_CHANGE_MINUTE : MessageEnum.MAP_CHANGE_MINUTES,
          sec / 60);
    } else {
      sendAllPlayer(
          (sec != 1) ? MessageEnum.MAP_CHANGE_SECONDS : MessageEnum.MAP_CHANGE_SECOND, sec);
    }
  }

  public void sendVoteEndMessage() {
    if (sec - 10 >= 60) {
      sendAllVotePlayer(
          ((sec - 10) / 60 == 1) ? MessageEnum.VOTE_END_MINUTE : MessageEnum.VOTE_END_MINUTES,
          (sec - 10) / 60);
    } else {
      sendAllVotePlayer(
          ((sec - 10) != 1) ? MessageEnum.VOTE_END_SECONDS : MessageEnum.VOTE_END_SECOND, sec - 10);
    }
  }
}
