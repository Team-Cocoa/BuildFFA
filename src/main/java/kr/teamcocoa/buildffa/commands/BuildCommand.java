package kr.teamcocoa.buildffa.commands;

import kr.teamcocoa.buildffa.main.BuildFFA;
import kr.teamcocoa.buildffa.utils.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuildCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;
        if(player.hasPermission("teamcocoa.moderator")) {
            if(args.length == 0) {
                if(!BuildFFA.playerData.get(player).isBuild()) {
                    player.sendMessage(StringUtils.color("&a[&dBuildFFA&a] &aThe Build mode has been activated."));
                    player.setGameMode(GameMode.CREATIVE);
                    player.getInventory().clear();
                    player.getInventory().setArmorContents(null);
                    BuildFFA.playerData.get(player).setInGame(false);
                    BuildFFA.playerData.get(player).setBuild(true);
                }
                else {
                    player.sendMessage(StringUtils.color("&a[&dBuildFFA&a] &cThe Build mode has been deactivated."));
                    BuildFFA.playerData.get(player).setJoinInventory();
                    player.setGameMode(GameMode.SURVIVAL);
                    BuildFFA.playerData.get(player).setBuild(false);
                }
            }
        }
        else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a[&dTeamCocoa&a] &7This command does not exist or is deactivated."));
        }
        return true;
    }
}
