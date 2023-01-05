package kr.teamcocoa.buildffa.managers;

import kr.teamcocoa.buildffa.gui.AbstractGUI;
import kr.teamcocoa.buildffa.gui.KitEditInventory;
import kr.teamcocoa.buildffa.gui.MapVoteInventory;
import kr.teamcocoa.buildffa.gui.ShopInventory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.HashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GUIManager {

    private static HashMap<Class, AbstractGUI> map = new HashMap<>();

    public static void init() {
        map.put(ShopInventory.class, new ShopInventory());
        map.put(MapVoteInventory.class, new MapVoteInventory());
        map.put(KitEditInventory.class, new KitEditInventory());
    }

    public static AbstractGUI getGUI(Class clazz) {
        return map.getOrDefault(clazz, null);
    }

    public static void onClick(InventoryClickEvent e) {
        for (AbstractGUI gui : map.values()) {
            gui.onClick(e);
        }
    }

    public static void onClose(InventoryCloseEvent e) {
        for (AbstractGUI gui : map.values()) {
            gui.onClose(e);
        }
    }

}
