package net.hypr.doki.utils;

import org.slf4j.Logger;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;

public class LevellingUtils {
    public static void incrementXp(Logger logger, UserRecord userRecord) throws SQLException {
        Random random = new Random();
        // Random increment between 15 and 25
        int xpInc = (random.nextInt(10) + 16) * 3;
        int xpNew = userRecord.xp + xpInc;
        int totalXp = userRecord.xp + xpInc;
        int levelNew = userRecord.level;
        if (xpNew >= ((userRecord.level + 1) * 5) * 40) {
            levelNew ++;
            xpNew = 0;
        }
        DBUtils.updateUserRecord(
                userRecord.user_id,
                userRecord.server_id,
                Timestamp.valueOf(LocalDateTime.now()),
                xpNew,
                totalXp,
                levelNew
        );
        logger.info("Incremented {}'s XP in {} ({} -> {})", userRecord.username, userRecord.server_id, userRecord.xp, xpNew);
        if (levelNew > userRecord.level) {
            logger.info("Incremented {}'s level in {} ({} -> {})", userRecord.username, userRecord.server_id, userRecord.level, levelNew);
        }
    }
}
