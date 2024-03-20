package kr.teamcocoa.buildffa.listener;

import ch.dkrieger.permissionsystem.bukkit.BukkitBootstrap;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupManager;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayer;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerManager;
import ch.dkrieger.permissionsystem.lib.player.PlayerDesign;
import eu.cloudnetservice.driver.permission.PermissionGroup;
import eu.cloudnetservice.driver.permission.PermissionManagement;
import eu.cloudnetservice.driver.permission.PermissionUser;
import kr.teamcocoa.buildffa.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.main.BuildFFA;
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
        BuildFFAPlayer buildFFAPlayer = BuildFFA.playerData.get(player);

        PermissionManagement permissionManagement = BuildFFA.getPermissionManagement();

        PermissionUser permissionUser = permissionManagement.user(player.getUniqueId());
        PermissionGroup permissionGroup = permissionManagement.highestPermissionGroup(permissionManagement.user(player.getUniqueId()));

        String prestige = StringUtils.color(PrestigeManager.getInstance().getPrestigeName(buildFFAPlayer));

        String prefix = StringUtils.color(design.getPrefix()).replace("_", " ");
        String display = StringUtils.color(design.getDisplay()).replace("_", " ");
        StringBuilder suffix = new StringBuilder();
        if(permplayer.isInGroup("verified") && !permissionGroup.getName().equals("Verified")) {
            suffix.append(StringUtils.color(PermissionGroupManager.getInstance().getGroup("Verified").getPlayerDesign().getSuffix().trim()).replace("_", " "));
        }
        suffix.append(StringUtils.color(design.getSuffix().replace("-1", "")));

        if (prefix.equalsIgnoreCase("-1")) {
            prefix = "";
        }
        if (suffix.toString().equalsIgnoreCase("-1")) {
            suffix = new StringBuilder("");
        }
        if (display.equalsIgnoreCase("-1")) {
            display = "";
        }
        if (player.hasPermission("teamcocoa.premium")) {
            message = (StringUtils.color(message));
        }
        e.setFormat(MessageFormat.format(format, prestige, (display.equals("-1") || display.equals("") ? "&7" : display + " "), player.getName(), suffix));
        e.setMessage(message);
    }
}
