package kr.teamcocoa.buildffa.commands;

import kr.teamcocoa.buildffa.main.BuildFFA;
import kr.teamcocoa.buildffa.managers.PlayerManager;
import kr.teamcocoa.buildffa.model.BuildFFAPlayer;
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
        BuildFFAPlayer buildFFAPlayer = PlayerManager.getPlayer(player);

        if(buildFFAPlayer == null) {
            return false;
        }

        if(player.hasPermission("teamcocoa.moderator")) {
            if(args.length == 0) {
                if(!buildFFAPlayer.isBuild()) {
                    player.sendMessage(StringUtils.color("&a[&dBuildFFA&a] &aThe Build mode has been activated."));
                    player.setGameMode(GameMode.CREATIVE);
                    player.getInventory().clear();
                    player.getInventory().setArmorContents(null);
                    buildFFAPlayer.setInGame(false);
                    buildFFAPlayer.setBuild(true);
                }
                else {
                    player.sendMessage(StringUtils.color("&a[&dBuildFFA&a] &cThe Build mode has been deactivated."));
                    buildFFAPlayer.setJoinInventory();
                    player.setGameMode(GameMode.SURVIVAL);
                    buildFFAPlayer.setBuild(false);
                }
            }
        }
        else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a[&dTeamCocoa&a] &7This command does not exist or is deactivated."));
        }
        return true;
    }
}
