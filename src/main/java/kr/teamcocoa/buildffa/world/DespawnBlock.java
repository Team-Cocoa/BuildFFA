package kr.teamcocoa.buildffa.world;

import kr.teamcocoa.buildffa.main.BuildFFA;
import kr.teamcocoa.core.bukkit.utils.PacketUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.security.SecureRandom;

public class DespawnBlock extends BukkitRunnable {

    private static SecureRandom secureRandom = new SecureRandom();

    private int i = 0;
    private final Block block;
    private final int random;
    private final int x, y, z;
    private BlockPlaceEvent event;
    private boolean giveAgain;
    private final World world;

    public DespawnBlock(BlockPlaceEvent event, Block block){
        this.random = secureRandom.nextInt(10000);
        this.block = block;
        this.event = event;
        this.x = block.getX();
        this.y = block.getY();
        this.z = block.getZ();
        this.world = block.getWorld();
        this.giveAgain = block.getType() == Material.SANDSTONE;
    }

    public DespawnBlock(Block block){
        this.random = secureRandom.nextInt(10000);
        this.block = block;
        this.x = block.getX();
        this.y = block.getY();
        this.z = block.getZ();
        this.world = block.getWorld();
        this.giveAgain = false;
    }

    public void tick() {

    }

    public void removeFromWorld() {

    }

    public void run() {
        if(event != null) {
            try {
                if (!BuildFFA.playerData.get(event.getPlayer()).isInGame()) {
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
            ClientboundBlockDestructionPacket packet = new ClientboundBlockDestructionPacket(
                    random,
                    new BlockPos(block.getX(), block.getY(), block.getZ()),
                    i);
            for(Player player : Bukkit.getOnlinePlayers()) {
                PacketUtils.sendPackets(player, packet);
            }
            i++;
        }
        else{
            makeAir();
            ClientboundBlockDestructionPacket packet = new ClientboundBlockDestructionPacket(
                    random, new BlockPos(block.getX(), block.getY(), block.getZ()), 0);

            for(Player player : Bukkit.getOnlinePlayers()){
                PacketUtils.sendPackets(player, packet);
            }
            BuildFFA.worldData.removeBlock(block);
            if(this.giveAgain){
                try {
                    event.getPlayer().getInventory().addItem(new ItemStack(Material.SANDSTONE));
                    event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_ITEM_PICKUP, 100.0F, 0.0F);
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
