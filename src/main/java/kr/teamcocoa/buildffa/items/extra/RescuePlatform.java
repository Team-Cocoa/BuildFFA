package kr.teamcocoa.buildffa.items.extra;

import kr.teamcocoa.buildffa.model.DeSpawnBlock;
import kr.teamcocoa.buildffa.translate.ItemNode;
import kr.teamcocoa.buildffa.translate.MessageNode;
import kr.teamcocoa.buildffa.main.BuildFFA;
import kr.teamcocoa.buildffa.utils.LangUtils;
import kr.teamcocoa.buildffa.world.WorldManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RescuePlatform extends AbstractExtraItem implements UseAble {

    private static RescuePlatform instance;

    private RescuePlatform() {

    }

    public static RescuePlatform getInstance() {
        if(instance == null) {
            instance = new RescuePlatform();
        }
        return instance;
    }

    @Override
    public ItemStack getItemStack(Player player, int count) {
        ItemStack itemStack = new ItemStack(Material.BLAZE_ROD, count);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(LangUtils.getMessage(player, ItemNode.RESCUE_PLATFORM));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public String getName(Player player) {
        return LangUtils.getMessage(player, ItemNode.RESCUE_PLATFORM);
    }

    @Override
    public void onClick(PlayerInteractEvent e) {

        Player player = e.getPlayer();
        if (player.getItemInHand().hasItemMeta() && player.getItemInHand().getItemMeta().getDisplayName() != null) {
            String displayName = player.getItemInHand().getItemMeta().getDisplayName();
            if(displayName.equals(LangUtils.getMessage(player, ItemNode.RESCUE_PLATFORM))) {
                if (WorldManager.getInstance().getDeathHeight() + 100 > player.getLocation().getY()) {
                    player.getInventory().setItem(player.getInventory().getHeldItemSlot(),
                            player.getItemInHand().getAmount() - 1 == 0 ? null : RescuePlatform.getInstance().getItemStack(player, player.getItemInHand().getAmount() - 1));
                    Location location = player.getLocation().clone().add(0, -5, 0);
                    for (int i = -1; i < 2; i++) {
                        for (int j = -1; j < 2; j++) {
                            Location cloned = location.clone().add(i, 0, j);
                            Block block = cloned.getBlock();
                            block.setType(Material.SLIME_BLOCK);
                            BuildFFA.worldData.addBlock(block);
                            new DeSpawnBlock(block).runTaskTimerAsynchronously(BuildFFA.getInstance(), 0L, 10L);
                        }
                    }
                    player.sendMessage(LangUtils.getMessage(player, MessageNode.USED_PLATFORM));
                } else {
                    player.sendMessage(LangUtils.getMessage(player, MessageNode.CANNOT_USE_PLATFORM));
                }
            }

        }
    }
}
