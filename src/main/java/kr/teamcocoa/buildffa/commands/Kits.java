package kr.teamcocoa.buildffa.commands;

import kr.teamcocoa.buildffa.kit.KitData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Kits implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player) {
            ((Player)commandSender).openInventory(KitData.getKitSelection());
        }
        return true;
    }
}
