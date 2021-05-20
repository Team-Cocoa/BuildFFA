package utils;

import main.Main;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockBreakAnimation;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class RemoveBlockAnimation extends BukkitRunnable {
    int i = 0;
    private Player player;
    private Block block;
    public RemoveBlockAnimation(Player player, Block block){
        this.block = block;
        this.player = player;
    }

    public void run(){
        if(i < 10) {
            PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(new Random().nextInt(2000), new BlockPosition(block.getX(), block.getY(), block.getZ()), i);
            for(Player player : Bukkit.getOnlinePlayers()){
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            }
            i++;
        }
        else{
            block.setType(Material.AIR);
            Main.worldData.removeBlock(block);
            this.cancel();
        }
    }

}
