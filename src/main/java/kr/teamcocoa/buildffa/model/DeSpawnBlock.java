package kr.teamcocoa.buildffa.model;

import kr.teamcocoa.buildffa.main.BuildFFA;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockBreakAnimation;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class DeSpawnBlock extends BukkitRunnable {

    private int i = 0;
    private Block block;
    private int random;
    private int x, y, z;
    private Player whoPlaced;
    private boolean giveAgain;
    private World world;

    public DeSpawnBlock(Player whoPlaced, Block block){
        this.random = new Random().nextInt(10000);
        this.block = block;
        this.whoPlaced = whoPlaced;
        this.x = block.getX();
        this.y = block.getY();
        this.z = block.getZ();
        this.world = block.getWorld();
        this.giveAgain = block.getType() == Material.SANDSTONE;
    }

    public DeSpawnBlock(Block block){
        this.random = new Random().nextInt(10000);
        this.block = block;
        this.x = block.getX();
        this.y = block.getY();
        this.z = block.getZ();
        this.world = block.getWorld();
        this.giveAgain = false;
    }

    public void run() {
        if(whoPlaced != null) {
            try {
                if (!BuildFFA.playerData.get(whoPlaced).isInGame()) {
                    this.giveAgain = false;
                }
            }
            catch(Exception e) {
                makeAir();
                this.giveAgain = false;
                BuildFFA.worldData.removeBlock(block);
                cancel();
            }
        }
        if(i < 10) {
            PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(
                    random,
                    new BlockPosition(block.getX(), block.getY(), block.getZ()),
                    i);
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            }
            i++;
        }
        else{
            makeAir();
            PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(
                    random, new BlockPosition(block.getX(), block.getY(), block.getZ()), 0);
            for(Player player : Bukkit.getOnlinePlayers()){
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            }
            BuildFFA.worldData.removeBlock(block);
            if(this.giveAgain){
                try {
                    event.getPlayer().getInventory().addItem(new ItemStack(Material.SANDSTONE));
                    event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ITEM_PICKUP, 100.0F, 0.0F);
                }
                catch(Exception e){
                    makeAir();
                    cancel();
                }
            }
            cancel();
        }
    }

    private void makeAir() {
        Bukkit.getScheduler().runTask(BuildFFA.getInstance(), () -> {
            new Location(world, x, y, z).getBlock().setType(Material.AIR);
        });
    }
}
