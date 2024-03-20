package kr.teamcocoa.buildffa.databases;

import kr.teamcocoa.mysql.mysql.MySQL;
import kr.teamcocoa.mysql.mysql.MySQLManager;
import lombok.Getter;

public class BuildFFADatabase {

    @Getter
    private static MySQL mySQL;

    private static final String DATABASE_NAME = "buildffa";

    public static void init() {
        if(mySQL != null) {
            return;
        }

        if(MySQLManager.hasConnection(DATABASE_NAME)) {
            mySQL = MySQLManager.getConnection(DATABASE_NAME);
        }
        else {
            mySQL = MySQLManager.createConnection(DATABASE_NAME);
        }

        mySQL.connect();
    }

}
