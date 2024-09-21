package net.hypr.doki.utils;


import java.sql.Timestamp;

public class UserRecord {
    public final long user_id;
    public final long server_id;
    public final Timestamp lastMessage;
    public final int xp;
    public int totalXp;
    public final int level;

    /**
     * Instantiates a new UserRecord
     * @param user_id   The User ID
     * @param server_id The Server/Guild ID
     */
    public UserRecord (long user_id, long server_id) {
        this.user_id = user_id;
        this.server_id = server_id;
        this.lastMessage = new Timestamp(0);
        this.xp = 0;
        this.level = 1;
    }

    /**
     * Instantiates a new UserRecord
     * @param user_id     The User ID
     * @param server_id   The Server/Guild ID
     * @param lastMessage The timestamp of the user's last message
     * @param xp          The user's XP
     * @param totalXp     The user's total XP
     * @param level       The user's level
     */
    public UserRecord (long user_id, long server_id, Timestamp lastMessage, int xp, int totalXp, int level) {
        this.user_id = user_id;
        this.server_id = server_id;
        this.lastMessage = lastMessage;
        this.xp = xp;
        this.totalXp = totalXp;
        this.level = level;
    }
}
