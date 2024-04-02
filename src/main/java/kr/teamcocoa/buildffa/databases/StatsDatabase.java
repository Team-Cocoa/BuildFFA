package kr.teamcocoa.buildffa.databases;

import kr.teamcocoa.buildffa.models.BuildFFAStats;
import kr.teamcocoa.core.mysql.MySQL;
import kr.teamcocoa.core.mysql.PlaceHolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatsDatabase {

    public static boolean loadStats(BuildFFAStats stats) {
        MySQL mySQL = BuildFFADatabase.getMySQL();

        String sql = "SELECT uuid, kills, deaths, kill_streaks, best_kill_streaks FROM stats WHERE uuid = ?";

        try(PreparedStatement preparedStatement = mySQL.getPreparedStatement(sql, stats.getUuid().toString());
            ResultSet rs = preparedStatement.executeQuery()) {
            if(rs.next()) {
                int kills = rs.getInt("kills");
                int deaths = rs.getInt("deaths");
                int killStreaks = rs.getInt("kill_streaks");
                int bestKillStreaks = rs.getInt("best_kill_streaks");

                stats.init(kills, deaths, killStreaks, bestKillStreaks);
                return true;
            }
            else {
                stats.init(0, 0, 0, 0);
                return false;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException();
    }

    public static void upsertStats(BuildFFAStats stats) {
        MySQL mySQL = BuildFFADatabase.getMySQL();

        String sql = "INSERT INTO stats(uuid) VALUES(?) ON DUPLICATE KEY UPDATE kills = ?, deaths = ?, kill_streaks = ?, best_kill_streaks = ?";

        PlaceHolder placeHolder = new PlaceHolder(5);
        placeHolder.addPlaceHolder(stats.getUuid().toString());
        placeHolder.addPlaceHolder(stats.getKills());
        placeHolder.addPlaceHolder(stats.getDeaths());
        placeHolder.addPlaceHolder(stats.getKillStreaks());
        placeHolder.addPlaceHolder(stats.getBestKillStreaks());

        mySQL.update(sql, placeHolder);
    }

}
