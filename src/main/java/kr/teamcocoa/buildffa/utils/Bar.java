package kr.teamcocoa.buildffa.utils;

import kr.teamcocoa.buildffa.enums.OtherEnum;
import kr.teamcocoa.buildffa.main.Main;
import kr.teamcocoa.buildffa.world.WorldManager;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Bar {
    public static void sendBar(Player player, String message) {
        IChatBaseComponent msg = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat packet = new PacketPlayOutChat(msg, (byte) 2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public static void sendDefaultBar(Player player, String time) {
        sendBar(player, ChatColor.translateAlternateColorCodes('&', "&8» " + LangUtils.getMessage(player, OtherEnum.BAR_MAP) + " : &e" + WorldManager.getInstance().getCurrentMap()
                + " &r&8» " + LangUtils.getMessage(player, OtherEnum.BAR_KIT) + " : &e"
                + Main.inst().kitData.getKitByInt(Main.playerData.get(player).getKit())
                + " &r&8» " + LangUtils.getMessage(player, OtherEnum.BAR_TIME_LEFT) + " : &e"
                + time));
    }
}
