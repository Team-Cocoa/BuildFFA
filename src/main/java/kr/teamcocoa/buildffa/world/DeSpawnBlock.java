package kr.teamcocoa.buildffa.world;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import java.security.SecureRandom;
import kr.teamcocoa.buildffa.game.models.BuildFFAPlayer;
import kr.teamcocoa.core.bukkit.packetevents.api.PacketEvents;
import kr.teamcocoa.core.bukkit.packetevents.api.util.Vector3i;
import kr.teamcocoa.core.bukkit.packetevents.api.wrapper.play.server.WrapperPlayServerBlockBreakAnimation;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

@Getter
public class DeSpawnBlock {

  public interface Factory {
    DeSpawnBlock create(BuildFFAPlayer buildFFAPlayer, Block block, boolean giveAgain);
  }

  private static SecureRandom secureRandom = new SecureRandom();

  @Getter(AccessLevel.NONE)
  private final Plugin plugin;

  private byte i; // 자료형이 byte인 이유는 packet 클래스 생성자가 byte를 받아서...

  private BuildFFAPlayer buildFFAPlayer;

  private int random;

  private Block block;

  private int x, y, z;

  private boolean giveAgain;

  @Setter private boolean expire;

  private Vector3i packetBlockPosition;

  private World world;

  @Inject
  public DeSpawnBlock(
      @Assisted BuildFFAPlayer buildFFAPlayer,
      @Assisted Block block,
      @Assisted boolean giveAgain,
      Plugin plugin) {
    this.plugin = plugin;
    this.i = 0;
    this.buildFFAPlayer = buildFFAPlayer;
    this.random = secureRandom.nextInt(10000);
    this.block = block;
    this.x = block.getX();
    this.y = block.getY();
    this.z = block.getZ();
    this.world = block.getWorld();
    this.giveAgain = giveAgain;
    this.expire = false;
    this.packetBlockPosition = new Vector3i(x, y, z);
  }

  public void tick() {
    if (expire) {
      return;
    }
    if (i < 10) {
      WrapperPlayServerBlockBreakAnimation packet = getNewPacket(i);
      for (Player player : Bukkit.getOnlinePlayers()) {
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
      }
      i++;
    } else {
      makeAir();

      if (this.giveAgain) {
        Bukkit.getScheduler().runTask(plugin, this::giveBlockBack);
      }
    }
  }

  public void makeAir() {
    this.expire = true;
    WrapperPlayServerBlockBreakAnimation packet = getNewPacket((byte) 0);
    for (Player player : Bukkit.getOnlinePlayers()) {
      PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }
    i++;
    Bukkit.getScheduler()
        .runTask(
            plugin,
            () -> {
              new Location(world, x, y, z).getBlock().setType(Material.AIR);
            });
  }

  private void giveBlockBack() {
    Player player = buildFFAPlayer.getPlayer();
    if (!player.isOnline()) {
      return;
    }
    player.getInventory().addItem(new ItemStack(Material.SANDSTONE));
    player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 100.0F, 0.0F);
  }

  private WrapperPlayServerBlockBreakAnimation getNewPacket(byte strength) {
    return new WrapperPlayServerBlockBreakAnimation(random, packetBlockPosition, strength);
  }
}
