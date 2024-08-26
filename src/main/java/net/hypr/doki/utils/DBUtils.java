package net.hypr.doki.utils;

import com.freya02.botcommands.api.Logging;
import net.hypr.doki.Doki;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;

import java.math.BigInteger;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DBUtils {
    /**
     * Checks if the user record exists
     * @param user_id   User ID to search for
     * @param server_id Server ID to search for
     * @return Whether the record exists or not
     */
    public static boolean doesUserRecordExist(long user_id, long server_id) {
        Logger log = Logging.getLogger();
        BasicDataSource dataSource = Doki.getDataSource();
        QueryRunner qr = new QueryRunner(dataSource);
        ResultSetHandler<Set<BigInteger>> resultSetHandler = rs -> {
            Set<BigInteger> rows = new HashSet<>();
            while (rs.next()) {
                rows.add(BigInteger.valueOf(rs.getLong(1)));
            }
            return rows;
        };

        // Find out if the user is already in the DB
        try {
            log.debug("Searching DB for (usr{},srv:{})", user_id, server_id);
            Set<BigInteger> foundIds = qr.query("SELECT user_id FROM users WHERE user_id = " + user_id + " AND server_id = " + server_id, resultSetHandler);
            log.debug("Matching records: {}", foundIds.size());
            return !foundIds.isEmpty();
        } catch (SQLException ignored) {
            log.debug("An SQL error occurred");
            return false;
        }
    }

    /**
     * Creates a new user record
     * @param user_id   The users ID
     * @param server_id The server ID
     * @param username  The users username
     * @throws SQLException A SQL exception
     */
    public static void createUserRecord(long user_id, long server_id, String username) throws SQLException {
        Logger log = Logging.getLogger();
        log.info("Creating record (usr:{},srv:{},unm:{})", user_id, server_id, username);
        BasicDataSource dataSource = Doki.getDataSource();
        Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO users (user_id, server_id, username, last_message) VALUES (?, ?, ?, ?)"
        );
        stmt.setLong(1, user_id);
        stmt.setLong(2, server_id);
        stmt.setString(3, username);
        stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
        stmt.execute();
        log.info("Record (usr:{},srv:{},unm:{}) created!", user_id, server_id, username);
    }

    /**
     * Updates a user record
     * @param user_id   User ID of the record to update
     * @param server_id Server ID of the record to update
     * @param now The current time
     * @param xp The new XP count
     * @param totalXp The new total XP count
     * @param level The new level
     * @throws SQLException A SQL exception
     */
    public static void updateUserRecord(long user_id, long server_id, Timestamp now, int xp, int totalXp, int level) throws SQLException {
        Logger log = Logging.getLogger();
        log.info("Updating record (usr:{},srv:{})", user_id, server_id);
        BasicDataSource dataSource = Doki.getDataSource();
        Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
                "UPDATE users SET last_message = ?, xp = ?, total_xp = ?, level = ? WHERE user_id = ? AND server_id = ?"
        );
        stmt.setTimestamp(1, now);
        stmt.setInt(2, xp);
        stmt.setInt(3, totalXp);
        stmt.setInt(4, level);
        stmt.setLong(5, user_id);
        stmt.setLong(6, server_id);
        stmt.executeQuery();
        log.info("Updated record (usr:{},srv:{})!", user_id, server_id);
    }

    /**
     * Gets a user record
     * @param user_id   The User ID of the record to get
     * @param server_id The Server ID of the record to get
     * @return The found UserRecord
     * @throws SQLException A SQL exception
     */
    public static UserRecord getUserRecord(long user_id, long server_id) throws SQLException {
        BasicDataSource dataSource = Doki.getDataSource();
        Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT user_id, server_id, username, last_message, xp, total_xp, level FROM users WHERE user_id = ? AND server_id = ?"
        );
        stmt.setLong(1, user_id);
        stmt.setLong(2, server_id);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        return new UserRecord(
                rs.getLong("user_id"),
                rs.getLong("server_id"),
                rs.getString("username"),
                rs.getTimestamp("last_message"),
                rs.getInt("xp"),
                rs.getInt("total_xp"),
                rs.getInt("level")
        );
    }

    /**
     * Get all user records for a server
     * @param server_id The Server ID
     * @return A List of UserRecords
     * @throws SQLException A SQL exception
     */
    public static List<UserRecord> getAllUserRecords(long server_id) throws SQLException {
        BasicDataSource dataSource = Doki.getDataSource();
        Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT user_id, username, last_message, xp, total_xp, level FROM users WHERE server_id = ?"
        );
        stmt.setLong(1, server_id);
        ResultSet rs = stmt.executeQuery();
        List<UserRecord> userRecordList = new ArrayList<>();
        while (rs.next()) {
            userRecordList.add(new UserRecord(
                    rs.getLong("user_id"),
                    server_id,
                    rs.getString("username"),
                    rs.getTimestamp("last_message"),
                    rs.getInt("xp"),
                    rs.getInt("total_xp"),
                    rs.getInt("level")
            ));
        }
        return userRecordList;
    }
}

