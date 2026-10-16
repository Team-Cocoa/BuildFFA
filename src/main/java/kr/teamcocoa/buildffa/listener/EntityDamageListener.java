package kr.teamcocoa.buildffa.listener;

import com.google.inject.Inject;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayer;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayerManager;
import kr.teamcocoa.buildffa.world.WorldManager;
import kr.teamcocoa.core.bukkit.packetevents.api.PacketEvents;
import kr.teamcocoa.core.bukkit.packetevents.api.wrapper.play.server.WrapperPlayServerTitle;
import kr.teamcocoa.core.bukkit.packetevents.api.wrapper.play.server.WrapperPlayServerTitle.TitleAction;
import kr.teamcocoa.core.bukkit.packetevents.impl.adventure.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class EntityDamageListener implements Listener {

  private final BuildFFAPlayerManager buildFFAPlayerManager;

  private final WorldManager worldManager;

  @Inject
  public EntityDamageListener(
      BuildFFAPlayerManager buildFFAPlayerManager, WorldManager worldManager) {
    this.buildFFAPlayerManager = buildFFAPlayerManager;
    this.worldManager = worldManager;
  }

  @EventHandler
  public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
    if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
      e.setCancelled(true);
      return;
    }
    if (!worldManager.getCurrentMap().isPvpAble()) {
      e.setCancelled(true);
      return;
    }
    if (e.getEntity() instanceof Player) {
      if (e.getDamager() instanceof Projectile || e.getDamager() instanceof Player) {
        Player damagedPlayer = (Player) e.getEntity();
        Player damager =
            e.getDamager() instanceof Player
                ? (Player) e.getDamager()
                : (Player) ((Projectile) e.getDamager()).getShooter();

        BuildFFAPlayer damagedBFFAPlayer = buildFFAPlayerManager.getPlayer(damagedPlayer);
        BuildFFAPlayer damagerBFFAPlayer = buildFFAPlayerManager.getPlayer(damager);

        if (!damagerBFFAPlayer.isInGame() || !damagedBFFAPlayer.isInGame()) {
          e.setCancelled(true);
        } else {
          damagedBFFAPlayer.setLastHitPlayer(damager);
          damagedBFFAPlayer.addDamage(damager, e.getFinalDamage());
          ItemStack itemStack = damager.getInventory().getItemInHand();
          if (itemStack != null && itemStack.getType() == Material.STICK) {
            int durability = damagerBFFAPlayer.getKbStickDurability() - 1;
            if (durability == 0) {
              damager.setItemInHand(new ItemStack(Material.AIR));
              damager.playSound(damager.getLocation(), Sound.ITEM_BREAK, 100, 0);
            }
            damagerBFFAPlayer.setKbStickDurability(durability);
            sendSubtitle(damager, "&c(" + durability + "/15)", 0, 10, 0);
          }
        }
      }
    }
  }

  @EventHandler
  public void onEntityDamage(EntityDamageEvent e) {
    if (e.getEntity() instanceof Player) {
      Player player = ((Player) e.getEntity());
      if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
        e.setCancelled(true);
        return;
      }

      if (!worldManager.getCurrentMap().isPvpAble()) {
        e.setCancelled(true);
        return;
      }

      BuildFFAPlayer buildFFAPlayer = buildFFAPlayerManager.getPlayer(player);

      if (!buildFFAPlayer.isInGame()) {
        e.setCancelled(true);
        return;
      }
    }
  }

  private void sendSubtitle(Player player, String subtitle, int fadeIn, int stay, int fadeOut) {
    Component subtitleComponent = LegacyComponentSerializer.legacyAmpersand().deserialize(subtitle);
    sendTitlePacket(player, TitleAction.SET_TIMES_AND_DISPLAY, null, fadeIn, stay, fadeOut);
    sendTitlePacket(player, TitleAction.SET_SUBTITLE, subtitleComponent, 0, 0, 0);
    sendTitlePacket(player, TitleAction.SET_TITLE, Component.empty(), 0, 0, 0);
  }

  private void sendTitlePacket(
      Player player, TitleAction action, Component text, int fadeIn, int stay, int fadeOut) {
    WrapperPlayServerTitle packet =
        new WrapperPlayServerTitle(action, text, text, text, fadeIn, stay, fadeOut);
    PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
  }
}
