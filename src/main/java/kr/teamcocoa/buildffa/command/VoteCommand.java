package kr.teamcocoa.buildffa.command;

import com.google.inject.Inject;
import kr.teamcocoa.buildffa.translation.LangUtils;
import kr.teamcocoa.buildffa.translation.MessageEnum;
import kr.teamcocoa.buildffa.world.MapVote;
import kr.teamcocoa.buildffa.world.MapVoteInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VoteCommand implements CommandExecutor {

  private final MapVote mapVote;

  private final MapVoteInventory mapVoteInventory;

  @Inject
  public VoteCommand(MapVote mapVote, MapVoteInventory mapVoteInventory) {
    this.mapVote = mapVote;
    this.mapVoteInventory = mapVoteInventory;
  }

  @Override
  public boolean onCommand(
      CommandSender commandSender, Command command, String s, String[] strings) {
    if (commandSender instanceof Player) {
      Player player = ((Player) commandSender);
      if (mapVote.isVoteAble()) {
        player.openInventory(mapVoteInventory.getInventory(player));
      } else {
        player.sendMessage(LangUtils.getMessage(player, MessageEnum.VOTE_CANNOT_VOTE));
      }
      return true;
    } else {
      return false;
    }
  }
}
