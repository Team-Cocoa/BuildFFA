package kr.teamcocoa.buildffa.utils;

import kr.teamcocoa.buildffa.translate.OtherNode;
import kr.teamcocoa.buildffa.world.WorldManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Bar {

    public static void sendBar(Player player, String message) {
        IChatBaseComponent msg = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + StringUtils.color(message) + "\"}");
        PacketPlayOutChat packet = new PacketPlayOutChat(msg, (byte) 2);
        PlayerUtils.sendPackets(player, packet);
    }

    public static void sendDefaultBar(Player player, String time) {
        try {
            String barMap = LangUtils.getMessage(player, OtherNode.BAR_MAP);
            String currentMap = WorldManager.getInstance().getCurrentMap();
            String timeLeft = LangUtils.getMessage(player, OtherNode.BAR_TIME_LEFT);
            String format = "&8» {0} &f: &e{1} &8» {2} &f: &e{3}";
            sendBar(player, MessageFormat.format(format, barMap, currentMap, timeLeft, time));
        }
        catch(NullPointerException e) {

        }
    }
}
