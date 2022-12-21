package kr.teamcocoa.buildffa.database;

import kr.teamcocoa.buildffa.model.BuildFFAPlayer;
import kr.teamcocoa.buildffa.main.BuildFFA;
import kr.teamcocoa.buildffa.model.BuildFFAStats;
import kr.teamcocoa.mysql.mysql.MySQL;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class StatsDatabase {

    private MySQL mySQL;

    public boolean playerExists(UUID uuid) {
        String sql = "SELECT uuid FROM Stats WHERE uuid = ?";
        try (   PreparedStatement preparedStatement = mySQL.getPreparedStatement(sql, uuid);
                ResultSet rs = preparedStatement.executeQuery()) {
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
            // 위에 값이 false가 된다면...?
            // 만약에 단순히 에러이고 커넥션이 살아있다면...?
            // 모든 플레이어의 스탯은 초기화 될것이다...
            // 그건 막아야 겠지...? 그래서 에러가 나도 true 값을 return 하는것...
        }
    }

    public void initNewPlayer(UUID uuid) {
        String statsInitSql = "INSERT INTO stats(uuid) VALUES(?);";
        String invInitSql = "INSERT INTO inventory(uuid) VALUES(?);";
        mySQL.update(statsInitSql);
        mySQL.update(invInitSql);
    }

    public BuildFFAStats getStats(UUID uuid) {
        String sql = "SELECT * FROM stats WHERE uuid = ?";
        try(    PreparedStatement preparedStatement = mySQL.getPreparedStatement(sql, uuid.toString());
                ResultSet rs = preparedStatement.executeQuery()) {
            if(rs.next()) {
                int kills = rs.getInt("kills");
                int deaths = rs.getInt("deaths");
                int killStreak = rs.getInt("killStreak");
                BuildFFAStats stats = new BuildFFAStats(kills, deaths, killStreak);
                return stats;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return new BuildFFAStats(0, 0, 0);
    }

    public void updatePlayer(BuildFFAPlayer buildFFAPlayer) {
        String sql = "UPDATE stats SET kills = ?, deaths = ?, killStreak = ? WHERE uuid = ?";
        mySQL.update(sql);
    }

    public void updateRanking() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(BuildFFA.getInstance(),
                () -> BuildFFA.playerData.forEach(((player, bffaPlayer) -> updatePlayer(bffaPlayer))),
                0L, 6000L);
    }
}
