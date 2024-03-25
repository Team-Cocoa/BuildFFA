package kr.teamcocoa.buildffa.tabs;

import com.google.common.base.Preconditions;
import eu.cloudnetservice.driver.permission.PermissionGroup;
import eu.cloudnetservice.driver.permission.PermissionManagement;
import eu.cloudnetservice.driver.permission.PermissionUser;
import kr.teamcocoa.buildffa.main.BuildFFA;
import kr.teamcocoa.buildffa.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.models.BuildFFAPlayerManager;
import kr.teamcocoa.buildffa.prestige.Prestige;
import kr.teamcocoa.buildffa.prestige.PrestigeManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class TabManager {

    public static void updateNameTags(Player player) {
        updateNameTags(player, null);
    }

    public static void updateNameTags(Player player, Function<Player, PermissionGroup> playerIPermissionGroupFunction) {
        updateNameTags(player, playerIPermissionGroupFunction, null);
    }

    public static void updateNameTags(Player player, Function<Player, PermissionGroup> playerIPermissionGroupFunction,
                                      Function<Player, PermissionGroup> allOtherPlayerPermissionGroupFunction) {
        PermissionManagement permissionManagement = BuildFFA.getPermissionManagement();

        Preconditions.checkNotNull(player);

        PermissionUser playerPermissionUser = permissionManagement.user(player.getUniqueId());
        AtomicReference<PermissionGroup> playerPermissionGroup = new AtomicReference<>(
                playerIPermissionGroupFunction != null ? playerIPermissionGroupFunction.apply(player) : null);

        if (playerPermissionUser != null && playerPermissionGroup.get() == null) {

            playerPermissionGroup
                    .set(permissionManagement.highestPermissionGroup(playerPermissionUser));

            if (playerPermissionGroup.get() == null) {
                playerPermissionGroup.set(permissionManagement.defaultPermissionGroup());
            }
        }

        int sortIdLength = permissionManagement.groups().stream()
                .map(PermissionGroup::sortId)
                .map(String::valueOf)
                .mapToInt(String::length)
                .max()
                .orElse(0);

        initScoreboard(player);

        BuildFFAPlayer targetBuildFFAPlayer = BuildFFAPlayerManager.getPlayer(player);

        Bukkit.getOnlinePlayers().forEach(all -> {

            initScoreboard(all);

            BuildFFAPlayer receiveBuildFFAPlayer = BuildFFAPlayerManager.getPlayer(all);

            if (playerPermissionGroup.get() != null) {
                addTeamEntry(player, all, playerPermissionGroup.get(), sortIdLength,
                        targetBuildFFAPlayer.getPrestige(),
                        targetBuildFFAPlayer.getBuildFFAStats().getKills());
            }

            PermissionUser targetPermissionUser = permissionManagement
                    .user(all.getUniqueId());
            PermissionGroup targetPermissionGroup =
                    allOtherPlayerPermissionGroupFunction != null ? allOtherPlayerPermissionGroupFunction.apply(all) : null;

            if (targetPermissionUser != null && targetPermissionGroup == null) {
                targetPermissionGroup = permissionManagement
                        .highestPermissionGroup(targetPermissionUser);

                if (targetPermissionGroup == null) {
                    targetPermissionGroup = permissionManagement.defaultPermissionGroup();
                }
            }

            if (targetPermissionGroup != null) {
                addTeamEntry(all, player, targetPermissionGroup, sortIdLength,
                        receiveBuildFFAPlayer.getPrestige(),
                        receiveBuildFFAPlayer.getBuildFFAStats().getKills());
            }
        });
    }

    private static void addTeamEntry(
            Player target,
            Player all,
            PermissionGroup permissionGroup,
            int highestSortIdLength,
            Prestige prestige,
            int kills) {
        int sortIdLength = String.valueOf(permissionGroup.sortId()).length();
        String teamName = (
                highestSortIdLength == sortIdLength ?
                        permissionGroup.sortId() :
                        String.format("%0" + highestSortIdLength + "d", permissionGroup.sortId())
        ) + permissionGroup.name() + prestige.getShortName();

        if (teamName.length() > 16) {
            teamName = teamName.substring(0, 16);
        }

        Team team = all.getScoreboard().getTeam(teamName);
        if (team == null) {
            team = all.getScoreboard().registerNewTeam(teamName);
        }

        String prefix = permissionGroup.prefix();
        String color = permissionGroup.color();
        String suffix =
                prestige == Prestige.BEGINNER
                        ? MessageFormat.format("&8 | {0}{1}", prestige.toBukkitColor(), prestige.getShortName())
                        : MessageFormat.format("&8 | {0}{1} &8| {2}{3}",
                        prestige.toBukkitColor(),
                        prestige.getShortName(),
                        prestige.toBukkitColor(),
                        PrestigeManager.getInstance().arabicToRome(PrestigeManager.getInstance().getPrestigeRank(prestige, kills)));

        try {
            Method method = team.getClass().getDeclaredMethod("setColor", ChatColor.class);
            method.setAccessible(true);

            if (color != null && !color.isEmpty()) {
                ChatColor chatColor = ChatColor.getByChar(color.replaceAll("&", "").replaceAll("§", ""));
                if (chatColor != null) {
                    method.invoke(team, chatColor);
                }
            } else {
                color = ChatColor.getLastColors(prefix.replace('&', '§'));
                if (!color.isEmpty()) {
                    ChatColor chatColor = ChatColor.getByChar(color.replaceAll("&", "").replaceAll("§", ""));
                    if (chatColor != null) {
                        BuildFFA.getPermissionManagement().updateGroup(PermissionGroup.builder(permissionGroup).color(color).build());
                        method.invoke(team, chatColor);
                    }
                }
            }
        } catch (NoSuchMethodException ignored) {
        } catch (IllegalAccessException | InvocationTargetException exception) {
            exception.printStackTrace();
        }

        team.setPrefix(ChatColor.translateAlternateColorCodes('&', prefix));

        team.setSuffix(ChatColor.translateAlternateColorCodes('&', suffix));

        team.addEntry(target.getName());

        target.setDisplayName(ChatColor.translateAlternateColorCodes('&', permissionGroup.display() + target.getName()));

    }

    private static void initScoreboard(Player all) {
        if (all.getScoreboard().equals(all.getServer().getScoreboardManager().getMainScoreboard())) {
            all.setScoreboard(all.getServer().getScoreboardManager().getNewScoreboard());
        }
    }
}
