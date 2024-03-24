package kr.teamcocoa.buildffa.world;

import kr.teamcocoa.buildffa.main.BuildFFA;
import kr.teamcocoa.buildffa.models.BuildFFAPlayer;
import kr.teamcocoa.core.bukkit.utils.PacketUtils;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.security.SecureRandom;

@Getter
public class DeSpawnBlock {

    private static SecureRandom secureRandom = new SecureRandom();

    private int i;

    private BuildFFAPlayer buildFFAPlayer;

    private int random;

    private Block block;

    private int x, y, z;

    private boolean giveAgain;

    @Setter
    private boolean expire;

    private World world;

    public DeSpawnBlock(BuildFFAPlayer buildFFAPlayer, Block block, boolean giveAgain){
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
    }

    public void tick() {
        if(expire) {
            return;
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

            if(this.giveAgain){
                try {
                    Player player = buildFFAPlayer.getPlayer();
                    player.getInventory().addItem(new ItemStack(Material.SANDSTONE));
                    player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 100.0F, 0.0F);
                }
                catch(Exception e){

                }
            }
        }
    }

    public void makeAir() {
        this.expire = true;
        ClientboundBlockDestructionPacket packet = new ClientboundBlockDestructionPacket(
                random,
                new BlockPos(block.getX(), block.getY(), block.getZ()),
                0);
        for(Player player : Bukkit.getOnlinePlayers()) {
            PacketUtils.sendPackets(player, packet);
        }
        Bukkit.getScheduler().runTask(BuildFFA.getInstance(), () -> {
            new Location(world, x, y, z).getBlock().setType(Material.AIR);
        });
    }

}
