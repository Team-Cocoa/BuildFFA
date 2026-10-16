package kr.teamcocoa.buildffa.command;

import com.google.inject.Inject;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayerManager;
import kr.teamcocoa.core.utils.StringUtils;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuildCommand implements CommandExecutor {

  private final BuildFFAPlayerManager buildFFAPlayerManager;

  @Inject
  public BuildCommand(BuildFFAPlayerManager buildFFAPlayerManager) {
    this.buildFFAPlayerManager = buildFFAPlayerManager;
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (sender instanceof Player) {
      Player player = ((Player) sender);
      if (player.hasPermission("teamcocoa.moderator")) {
        BuildFFAPlayer buildFFAPlayer = buildFFAPlayerManager.getPlayer(player);
        if (!buildFFAPlayer.isBuild()) {
          player.sendMessage(
              StringUtils.color("&a[&dBuildFFA&a] &aThe Build mode has been activated."));
          player.setGameMode(GameMode.CREATIVE);
          player.getInventory().clear();
          player.getInventory().setArmorContents(null);
          buildFFAPlayer.setInGame(false);
          buildFFAPlayer.setBuild(true);
        } else {
          player.sendMessage(
              StringUtils.color("&a[&dBuildFFA&a] &cThe Build mode has been deactivated."));
          buildFFAPlayer.setJoinInventory();
          player.setGameMode(GameMode.SURVIVAL);
          buildFFAPlayer.setBuild(false);
        }

      } else {
        player.sendMessage(
            StringUtils.color(
                "&a[&dTeamCocoa&a] &7This command does not exist or is deactivated."));
      }
    }
    return false;
  }
}
