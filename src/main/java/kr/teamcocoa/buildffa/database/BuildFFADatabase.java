package kr.teamcocoa.buildffa.database;

import kr.teamcocoa.mysql.mysql.MySQL;
import kr.teamcocoa.mysql.mysql.MySQLManager;
import lombok.Getter;

public class BuildFFADatabase {

    private static String database = "teamcocoa_buildffa";

    @Getter
    private static MySQL mySQL;

    @Getter
    private static StatsDatabase statsDatabase;

    @Getter
    private static InventoryDatabase inventoryDatabase;

    public static void init() {
        if(mySQL != null) {
            return;
        }
        mySQL = MySQLManager.createConnection(database);
        mySQL.connect();
        statsDatabase = new StatsDatabase(mySQL);
        inventoryDatabase = new InventoryDatabase(mySQL);
    }

}
