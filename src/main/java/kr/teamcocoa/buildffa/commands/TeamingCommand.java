package kr.teamcocoa.buildffa.commands;

import kr.teamcocoa.buildffa.BuildFFABootstrap;
import kr.teamcocoa.core.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamingCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("teamcocoa.moderator")) {
                if (BuildFFABootstrap.teaming) {
                    BuildFFABootstrap.teaming = false;
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendMessage(StringUtils.color("&a[&dTeamCocoa&a] &7Teaming is &4&lPROHIBITED &7from now on!"));
                    }
                } else {
                    BuildFFABootstrap.teaming = true;
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendMessage(StringUtils.color("&a[&dTeamCocoa&a] &7Teaming is &a&lALLOWED &7from now on!"));
                    }
                }
            }
            else {
                p.sendMessage(StringUtils.color("&a[&dTeamCocoa&a] &7This command does not exist or is deactivated."));
            }
        }
        return false;
    }
}
