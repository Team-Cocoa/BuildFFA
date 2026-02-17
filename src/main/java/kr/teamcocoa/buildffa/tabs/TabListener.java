package kr.teamcocoa.buildffa.tabs;

import dev.derklaro.aerogel.Inject;
import dev.derklaro.aerogel.Singleton;
import eu.cloudnetservice.driver.event.EventListener;
import eu.cloudnetservice.driver.event.events.permission.PermissionUpdateGroupEvent;
import eu.cloudnetservice.driver.event.events.permission.PermissionUpdateUserEvent;
import eu.cloudnetservice.driver.permission.PermissionManagement;
import eu.cloudnetservice.driver.permission.PermissionUser;
import kr.teamcocoa.buildffa.BuildFFABootstrap;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

@Singleton
public class TabListener implements Listener {

    private PermissionManagement permissionManagement;

    @Inject
    public TabListener(
            @NonNull PermissionManagement permissionManagement
    ) {
        this.permissionManagement = permissionManagement;
    }

    @EventListener
    public void handle(PermissionUpdateUserEvent event) {
        Bukkit.getScheduler().runTask(BuildFFABootstrap.getInstance(), () -> Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.getUniqueId().equals(event.permissionUser().uniqueId()))
                .findFirst()
                .ifPresent(TabManager::updateNameTags));
    }

    @EventListener
    public void handle(PermissionUpdateGroupEvent event) {
        Bukkit.getScheduler().runTask(BuildFFABootstrap.getInstance(), () -> Bukkit.getOnlinePlayers().forEach(player -> {
            PermissionUser permissionUser = permissionManagement.user(player.getUniqueId());

            if (permissionUser != null && permissionUser.inGroup(event.permissionGroup().name())) {
                TabManager.updateNameTags(player);
            }
        }));
    }

}
