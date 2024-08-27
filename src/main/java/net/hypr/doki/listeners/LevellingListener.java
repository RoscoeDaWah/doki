package net.hypr.doki.listeners;

import com.freya02.botcommands.api.Logging;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.hypr.doki.Doki;
import net.hypr.doki.utils.DBUtils;
import net.hypr.doki.utils.LevellingUtils;
import net.hypr.doki.utils.UserRecord;
import org.slf4j.Logger;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;

public class LevellingListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Logger log = Logging.getLogger();

        /* Ignore the message if one of the following conditions is met:
          - Message is from self user
          - Message is from a bot
          - Message is command (starts with bot prefix)
          - Message is a Direct Message to the bot
        */
        try {
            event.getGuild();
        } catch (IllegalStateException ex) {
            log.debug("Ignoring direct message with ID {}", event.getMessageId());
            return;
        }

        if (
                Doki.getJDA().getSelfUser().getId().equals(event.getAuthor().getId()) ||
                        event.getAuthor().isBot() ||
                        event.getMessage().getContentStripped().startsWith(Doki.getPrefix())
        ) {
            log.debug("Ignoring self/bot message with ID {}", event.getMessageId());
            return;
        }

        User user = event.getAuthor();
        Guild guild = event.getGuild();
        boolean userIsInDb = DBUtils.doesUserRecordExist(user.getIdLong(), guild.getIdLong());
        if (userIsInDb) {
            try {
                UserRecord rec = DBUtils.getUserRecord(user.getIdLong(), guild.getIdLong());
                Duration timeSinceLastMessage = Duration.between(
                        rec.lastMessage.toLocalDateTime(),
                        LocalDateTime.now()
                );
                if (timeSinceLastMessage.compareTo(Duration.ofHours(1)) > 0) {
                    // it has been over an hour since the user last sent a message that affected XP
                    LevellingUtils.incrementXp(log, rec);
                } else {
                    log.debug("Ignoring message ID {} as not enough time has passed", event.getMessageId());
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            log.info("No record of user ID {} in server {}, creating blank record", user.getId(), guild.getId());
            try {
                DBUtils.createUserRecord(user.getIdLong(), guild.getIdLong(), user.getName());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
