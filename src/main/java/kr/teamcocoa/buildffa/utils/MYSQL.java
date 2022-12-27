package kr.teamcocoa.buildffa.utils;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MYSQL {
    public static String host = "localhost";

    public static String port = "3306";

    public static String database = "teamcocoa_buildffa";

    public static String username = "root";

    public static String password = "root";

    public Connection con;

    public void connect() {
        if (!isConnected()) {
            try {
                con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
                System.out.println("| [BuildFFA] Successfully connected to MYSQL!                 |");
            } catch (SQLException e) {
                System.out.println("| [BuildFFA] Failed to connect to MYSQL server.               |");
            }
        }
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                con.close();
                System.out.println("| [BuildFFA] MYSQL               Disconnected.                |");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isConnected() {
        try {
            if (con == null || con.isClosed()) {
                return false;
            }
            else {
                return true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void update(String qry) {
        try (PreparedStatement ps = con.prepareStatement(qry)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PreparedStatement getPreparedStatement(String query) {
        if(isConnected()) {
            try {
                return con.prepareStatement(query);
            }
            catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }
        else {
            return null;
        }
    }
}
