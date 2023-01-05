package kr.teamcocoa.buildffa.commands;

import kr.teamcocoa.buildffa.main.BuildFFA;
import kr.teamcocoa.buildffa.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamingCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("teamcocoa.moderator")) {
                player.sendMessage(StringUtils.color("&a[&dTeamCocoa&a] &7This command does not exist or is deactivated."));
            }
            if (BuildFFA.isTeaming()) {
                BuildFFA.setTeaming(false);
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.sendMessage(StringUtils.color("&a[&dTeamCocoa&a] &7Teaming is &4&lPROHIBITED &7from now on!"));
                }
            }
            else {
                BuildFFA.setTeaming(true);
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.sendMessage(StringUtils.color("&a[&dTeamCocoa&a] &7Teaming is &a&lALLOWED &7from now on!"));
                }
            }
        }
        return true;
    }

}
