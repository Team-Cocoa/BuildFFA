package kr.teamcocoa.buildffa.model;

import kr.teamcocoa.buildffa.main.BuildFFA;
import kr.teamcocoa.buildffa.managers.PlayerManager;
import kr.teamcocoa.buildffa.utils.PlayerUtils;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockBreakAnimation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DeSpawnBlock {

    private static int count = 0;
    private static final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(100);
    private static final ConcurrentHashMap<Integer, ScheduledFuture> map = new ConcurrentHashMap<>();

    private int i = 0;
    private Block block;
    private int index;
    private Player whoPlaced;
    private boolean giveAgain;

    public DeSpawnBlock(Player whoPlaced, Block block) {
        this.block = block;
        this.whoPlaced = whoPlaced;
        this.giveAgain = block.getType() == Material.SANDSTONE;
    }

    public DeSpawnBlock(Block block) {
        this.block = block;
        this.giveAgain = false;
    }

    public synchronized void initIndex() {
        this.index = count;
        count++;
    }

    public void start() {
        ScheduledFuture future = executor.scheduleAtFixedRate(() -> {
            if (i < 10) {
                PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(
                        index,
                        new BlockPosition(block.getX(), block.getY(), block.getZ()),
                        i);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    PlayerUtils.sendPackets(player, packet);
                }
                i++;
            }
            else {
                makeAir();
                PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(
                        index, new BlockPosition(block.getX(), block.getY(), block.getZ()), 0);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    PlayerUtils.sendPackets(player, packet);
                }
                stop();
            }
        }, 0, 500, TimeUnit.MILLISECONDS);
        map.put(index, future);
    }

    private void stop() {
        ScheduledFuture future = map.getOrDefault(index, null);
        if(future == null) {
            return;
        }
        future.cancel(true);
        map.remove(index);
    }

    private void makeAir() {
        Bukkit.getScheduler().runTask(BuildFFA.getInstance(), () -> {
            new Location(block.getWorld(), block.getX(), block.getY(), block.getZ()).getBlock().setType(Material.AIR);
        });
    }
}
