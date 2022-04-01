package kr.teamcocoa.buildffa.block;

import kr.teamcocoa.buildffa.main.Main;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockBreakAnimation;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Random;

public class DespawnBlock extends BukkitRunnable {
    int i = 0;
    private final Block block;
    private final int random;
    private final int x, y, z;
    private final BlockPlaceEvent event;
    private boolean giveAgain;
    private final World world;
    public DespawnBlock(BlockPlaceEvent event, Block block){
        this.random = new Random().nextInt(2000);
        this.block = block;
        this.event = event;
        this.x = block.getX();
        this.y = block.getY();
        this.z = block.getZ();
        this.world = block.getWorld();
        this.giveAgain = block.getType() == Material.SANDSTONE;
    }

    public void run() {
        try {
            if (!Main.playerData.get(event.getPlayer()).isInGame()) {
                this.giveAgain = false;
            }
        }
        catch(Exception e) {
            new Location(world, x, y, z).getBlock().setType(Material.AIR);
            this.giveAgain = false;
            cancel();
        }
        if(i < 10) {
            PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(
                    random,
                    new BlockPosition(block.getX(), block.getY(), block.getZ()),
                    i);
            for(Player player : Bukkit.getOnlinePlayers()){
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            }
            i++;
        }
        else{
            block.setType(Material.AIR);
            PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(
                    random, new BlockPosition(block.getX(), block.getY(), block.getZ()), 0);
            for(Player player : Bukkit.getOnlinePlayers()){
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            }
            Main.worldData.removeBlock(block);
            if(this.giveAgain){
                try {
                    event.getPlayer().getInventory().addItem(new ItemStack(Material.SANDSTONE));
                    event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ITEM_PICKUP, 100.0F, 0.0F);
                    cancel();
                }
                catch(Exception e){
                    new Location(world, x, y, z).getBlock().setType(Material.AIR);
                    cancel();
                }
            }
        }
    }
}
