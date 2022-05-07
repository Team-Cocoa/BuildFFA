package kr.teamcocoa.buildffa.listener;

import ch.dkrieger.permissionsystem.bukkit.BukkitBootstrap;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroup;
import ch.dkrieger.permissionsystem.lib.group.PermissionGroupManager;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayer;
import ch.dkrieger.permissionsystem.lib.player.PermissionPlayerManager;
import ch.dkrieger.permissionsystem.lib.player.PlayerDesign;
import de.fct.NickSystem.MySQL;
import kr.teamcocoa.buildffa.utils.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.text.MessageFormat;

public class AsyncPlayerChatListener implements Listener {

    private String format = "{0} {1}{2}{3} &8> &f{4}";
    /**
     * 0 : Prestige
     * 1 : Rank (Includes color code)
     * 2 : Name
     * 3 : Suffix (Includes color code)
     * 4 : Chat Message
     */

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        String message = e.getMessage();

        PermissionGroup group = PermissionPlayerManager.getInstance().getPermissionPlayer(player.getUniqueId()).getHighestGroup();
        if (group == null) {
            return;
        }

        PlayerDesign design = group.getPlayerDesign();
        if (design == null) {
            return;
        }

        PermissionPlayer permplayer = PermissionPlayerManager.getInstance().getPermissionPlayer(player.getUniqueId());

        String prestige = StringUtils.color("");
        if(MySQL.containsPlayer(player.getUniqueId().toString())) {
            e.setMessage(MessageFormat.format("{0} {1} {2} {3}",
                    prestige,
                    StringUtils.color("&7" + player.getName()),
                    StringUtils.color("&8>&f"),
                    message));
            return;
        }

        String prefix = StringUtils.color(design.getPrefix()).replace("_", " ");
        String display = StringUtils.color(design.getDisplay()).replace("_", " ");
        StringBuilder suffix = new StringBuilder();
        if(permplayer.isInGroup("verified") && !group.getName().equals("Verified")) {
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
        if (BukkitBootstrap.getInstance().getPlaceHolderAPI() != null) {
            BukkitBootstrap.getInstance().getPlaceHolderAPI().set(player, prefix);
        }
        if (e.getPlayer().hasPermission("dkperms.chat.color")) {
            message = (StringUtils.color(message));
        }
        e.setMessage(MessageFormat.format(format, prestige, display, player.getName(), suffix, message));
    }
}
