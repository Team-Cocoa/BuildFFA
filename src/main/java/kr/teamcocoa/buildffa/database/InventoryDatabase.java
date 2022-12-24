package kr.teamcocoa.buildffa.database;

import kr.teamcocoa.mysql.mysql.MySQL;
import kr.teamcocoa.mysql.mysql.PlaceHolder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class InventoryDatabase {

    private MySQL mySQL;

    public boolean setInventory(UUID uuid, ItemStack[] inventory) {
        String sql = "UPDATE inventory SET sword = ?, stick = ?, block = ?, web = ?, pearl = ? WHERE uuid = ?";
        PlaceHolder placeHolder = new PlaceHolder(6);
        for(int i = 0; i < inventory.length; i++) {

        }
        mySQL.update(sql, placeHolder);
    }

    public boolean resetInventory(UUID uuid) {
        String sql = "INSERT INTO inventory(uuid) VALUES(?) ON DUPLICATE KEY UPDATE sword = 0, stick = 1, block = 2, web = 7, pearl = 8;";
        mySQL.update(sql, uuid.toString());
    }



}
