package kr.teamcocoa.buildffa.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Title {

    public static void sendTitle(Player player, String title, String subtitle, int fadeInTick, int stayTick, int fadeOutTick) {
        PacketPlayOutTitle timePacket = new PacketPlayOutTitle(fadeInTick, stayTick, fadeOutTick);
        PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + StringUtils.color(title) + "\"}"));
        PacketPlayOutTitle subTitlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + StringUtils.color(subtitle) + "\"}"));

        PlayerUtils.sendPackets(player, timePacket, titlePacket, subTitlePacket);
    }

}
