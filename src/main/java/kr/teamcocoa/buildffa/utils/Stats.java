package kr.teamcocoa.buildffa.utils;

import kr.teamcocoa.buildffa.kit.BffaPlayer;
import kr.teamcocoa.buildffa.main.Main;
import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Stats {
    public boolean playerExists(String uuid) {
        try (   PreparedStatement preparedStatement = Main.getInstance().mysql.getPreparedStatement("SELECT `UUID` FROM Stats WHERE UUID= '" + uuid + "'");
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

    public void createPlayer(String uuid) {
        if (!playerExists(uuid)) {
            Main.getInstance().mysql.update("INSERT INTO stats(UUID) VALUES ('" + uuid + "');");
            Main.getInstance().mysql.update("INSERT INTO `inventory`(uuid) VALUES(\"" + uuid + "\");");
        }
    }

    public int getMaxKillStreak(String uuid) {
        try (   PreparedStatement preparedStatement = Main.getInstance().mysql.getPreparedStatement("SELECT `max_killstreak` FROM Stats WHERE UUID= '" + uuid + "'");
                ResultSet rs = preparedStatement.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("max_killstreak");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getKills(String uuid) {
        try (   PreparedStatement preparedStatement = Main.getInstance().mysql.getPreparedStatement("SELECT `KILLS` FROM Stats WHERE UUID= '" + uuid + "'");
                ResultSet rs = preparedStatement.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("KILLS");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getDeaths(String uuid) {
        try (   PreparedStatement preparedStatement = Main.getInstance().mysql.getPreparedStatement("SELECT `DEATHS` FROM Stats WHERE UUID= '" + uuid + "'");
                ResultSet rs = preparedStatement.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("DEATHS");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void updatePlayer(BffaPlayer bffaPlayer) {
        int kills = bffaPlayer.getKills();
        int deaths = bffaPlayer.getDeaths();
        int bestKills = bffaPlayer.getBestKillStreaks();
        Main.getInstance().mysql.update("UPDATE `stats` SET `KILLS` = '" + kills
                + "', `DEATHS` = '" + deaths
                + "', `max_killstreak` = '" + bestKills
                + "' WHERE `UUID` = '" + bffaPlayer.getPlayer().getUniqueId().toString()
                + "';"
        );
    }

    public void updateRanking() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(),
                () -> Main.playerData.forEach(((player, bffaPlayer) -> updatePlayer(bffaPlayer))),
                0L, 6000L);
    }
}
