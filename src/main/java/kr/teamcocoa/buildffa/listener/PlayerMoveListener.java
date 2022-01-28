package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.main.Main;
import kr.teamcocoa.buildffa.kit.BffaPlayer;
import kr.teamcocoa.buildffa.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerMoveListener implements Listener {
  
  @EventHandler
  public void onPlayerMove(PlayerMoveEvent e) {
    try {
      final Player p = e.getPlayer();
      Location loc = p.getLocation();
      if (WorldManager.getInstance().getCurrentMap() != null) {
        if (loc.getY() <= WorldManager.getInstance().getDeathHeight() && Main.playerData.get(p).isDied() == false)
          if (!Main.playerData.get(p).isBuild()) {
            p.setHealth(0.0D);
            Main.playerData.get(p).setDied(true);
            Bukkit.getScheduler().runTaskLater(Main.inst(), () -> {
              try {
                Main.playerData.get(p).setDied(false);
              }
              catch(Exception e1) {

              }
            }, 10L);
          }
        if (loc.getY() <= WorldManager.getInstance().getArenaHeight()) {
          if (!Main.playerData.get(p).isInGame() && !Main.playerData.get(p).isBuild()) {
            BffaPlayer bffaPlayer = Main.playerData.get(p);
            p.closeInventory();
            p.getInventory().clear();
            Main.playerData.get(p).setInGame(true);
            bffaPlayer.setPlayerKillStreak(0);
            p.setHealth(20.0D);
            p.setLevel(0);
            ItemStack[] inventory = bffaPlayer.getInventory();
            ItemStack[] armor = Main.inst().kitData.getArmor();
            p.getInventory().setContents(inventory);
            p.getInventory().setArmorContents(armor);
            p.playSound(p.getLocation(), Sound.ORB_PICKUP, 100.0F, 0.0F);
            /*
             * 여기에 킷 지급 코드를 작성하세요
             * 개같이 짜면 나중에 니가 힘들다 개샛기야^^7
             * */
          }
        }
      }
    }
    catch(NullPointerException e1) {

    }
  }
}
