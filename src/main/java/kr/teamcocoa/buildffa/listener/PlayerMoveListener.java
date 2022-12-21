package kr.teamcocoa.buildffa.listener;

import kr.teamcocoa.buildffa.main.BuildFFA;
import kr.teamcocoa.buildffa.model.BuildFFAPlayer;
import kr.teamcocoa.buildffa.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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
                if (loc.getY() <= WorldManager.getInstance().getDeathHeight() && BuildFFA.playerData.get(p).isDied() == false)
                    if (!BuildFFA.playerData.get(p).isBuild()) {
                        BuildFFA.playerData.get(p).setDied(true);
                        BuildFFA.playerData.get(p).death(false);
                        Bukkit.getScheduler().runTaskLater(BuildFFA.getInstance(), () -> {
                            try {
                                BuildFFA.playerData.get(p).setDied(false);
                            } catch (Exception e1) {

                            }
                        }, 10L);
                    }
                if (loc.getY() <= WorldManager.getInstance().getArenaHeight()) {
                    if (!BuildFFA.playerData.get(p).isInGame() && !BuildFFA.playerData.get(p).isBuild()) {
                        BuildFFAPlayer buildFFAPlayer = BuildFFA.playerData.get(p);
                        p.closeInventory();
                        p.getInventory().clear();
                        buildFFAPlayer.setInGame(true);
                        buildFFAPlayer.setCurrentKillStreak(0);
                        buildFFAPlayer.resetDamage(false);
                        buildFFAPlayer.resetKBStickDurability();
                        p.setHealth(20.0D);
                        p.setLevel(0);
                        ItemStack[] inventory = buildFFAPlayer.getInventory();
                        ItemStack[] armor = BuildFFA.getInstance().kitData.getArmor();
                        p.getInventory().setContents(inventory);
                        p.getInventory().setArmorContents(armor);
                        p.playSound(p.getLocation(), Sound.ORB_PICKUP, 100.0F, 0.0F);
                        if(buildFFAPlayer.isBowBought()) {
                            ItemStack itemStack = new ItemStack(Material.BOW);
//                            itemStack.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
                            p.getInventory().addItem(itemStack, new ItemStack(Material.ARROW, 16));
                        }
                        if(buildFFAPlayer.isSnowBallBought()) {
                            p.getInventory().addItem(new ItemStack(Material.SNOW_BALL, 16));
                        }
                    }
                }
            }
        } catch (NullPointerException e1) {

        }
    }
}
