package kr.teamcocoa.buildffa.block;

import kr.teamcocoa.buildffa.main.Main;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockBreakAnimation;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Random;

public class DespawnBlock {
    int i = 0;
    private final Block block;
    private final int random;
    private final BlockPlaceEvent event;
    private boolean giveAgain;
    public DespawnBlock(BlockPlaceEvent event, Block block){
        this.random = new Random().nextInt(2000);
        this.block = block;
        this.event = event;
        this.giveAgain = block.getType() == Material.SANDSTONE;
    }

    public boolean run() {
        try {
            if (!Main.playerData.get(event.getPlayer()).isInGame()) {
                this.giveAgain = false;
            }
        }
        catch(NullPointerException e) {
            block.setType(Material.AIR);
            this.giveAgain = false;
            return false;
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
            return true;
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
                    int index = Arrays.asList(Main.playerData.get(event.getPlayer()).getInventory()).indexOf(new ItemStack(Material.SANDSTONE, 64));
                    int amount = event.getPlayer().getInventory().getItem(index).getAmount();
                    ItemStack blockItem = new ItemStack(Material.SANDSTONE, amount + 1);
                    event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ITEM_PICKUP, 100.0F, 0.0F);
                    event.getPlayer().getInventory().setItem(index, blockItem);
                }
                catch(NullPointerException e){
                    return false;
                }
            }
            return false;
        }
    }
}
