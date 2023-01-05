package kr.teamcocoa.buildffa.commands;

import kr.teamcocoa.buildffa.translate.MessageNode;
import kr.teamcocoa.buildffa.utils.LangUtils;
import kr.teamcocoa.buildffa.world.MapVote;
import kr.teamcocoa.buildffa.gui.MapVoteInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VoteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player) {
            MapVoteInventory inventory = MapVoteInventory.getInstance();
            Player player = (Player) commandSender;
            if(MapVote.getInstance().isVoteAble()) {
                player.openInventory(inventory.getInventory(player));
            }
            else {
                player.sendMessage(LangUtils.getMessage(player, MessageNode.VOTE_CANNOT_VOTE));
            }
            return true;
        }
        else {
            return false;
        }
    }
}
