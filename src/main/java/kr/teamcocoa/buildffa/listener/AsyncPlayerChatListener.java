package kr.teamcocoa.buildffa.listener;

import eu.cloudnetservice.driver.permission.PermissionGroup;
import eu.cloudnetservice.driver.permission.PermissionManagement;
import eu.cloudnetservice.driver.permission.PermissionUser;
import kr.teamcocoa.buildffa.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.main.BuildFFA;
import kr.teamcocoa.buildffa.models.BuildFFAPlayerManager;
import kr.teamcocoa.buildffa.prestige.PrestigeManager;
import kr.teamcocoa.core.utils.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.text.MessageFormat;

public class AsyncPlayerChatListener implements Listener {

    private String format = "§8[{0}§8] {1}{2} {3} §8> §f%2$s";
    /**
     * 0 : Prestige
     * 1 : Rank (Includes color code)
     * 2 : Name
     * 3 : Suffix (Includes color code)
     */

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent e) {
//        Bukkit.getLogger().info(e.getFormat());
        Player player = e.getPlayer();
        String message = e.getMessage();
        BuildFFAPlayer buildFFAPlayer = BuildFFAPlayerManager.getPlayer(player);

        PermissionManagement permissionManagement = BuildFFA.getPermissionManagement();

        PermissionUser permissionUser = permissionManagement.user(player.getUniqueId());
        PermissionGroup permissionGroup = permissionManagement.highestPermissionGroup(permissionUser);

        String prestige = StringUtils.color(PrestigeManager.getInstance().getPrestigeName(buildFFAPlayer));

        String display = StringUtils.color(permissionGroup.display());
        String suffix = StringUtils.color(permissionGroup.suffix());

        if (player.hasPermission("teamcocoa.premium")) {
            message = (StringUtils.color(message));
        }
        e.setFormat(
                MessageFormat.format(format, prestige, display, player.getName(), suffix));
        e.setMessage(message);
    }
}
