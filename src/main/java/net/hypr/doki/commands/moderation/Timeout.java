package net.hypr.doki.commands.moderation;

import com.freya02.botcommands.api.annotations.BotPermissions;
import com.freya02.botcommands.api.annotations.CommandMarker;
import com.freya02.botcommands.api.annotations.UserPermissions;
import com.freya02.botcommands.api.application.ApplicationCommand;
import com.freya02.botcommands.api.application.annotations.AppOption;
import com.freya02.botcommands.api.application.slash.GuildSlashEvent;
import com.freya02.botcommands.api.application.slash.annotations.JDASlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.hypr.doki.utils.DurationUtils;

import java.time.Duration;
import java.time.OffsetDateTime;

@CommandMarker
@BotPermissions(Permission.MODERATE_MEMBERS)
@UserPermissions(Permission.MODERATE_MEMBERS)
public class Timeout extends ApplicationCommand {
    @JDASlashCommand(
            name = "timeout",
            subcommand = "set",
            description = "Times out a member"
    )
    public void timeout(GuildSlashEvent event,
                     @AppOption(name = "member") Member member,
                     @AppOption(name = "duration", description = "ex: 2h5m, must be between 1m and 7d") String duration) {
        Duration timeoutDuration = DurationUtils.parseDuration(duration);
        member.timeoutFor(timeoutDuration).queue();
        event.replyFormat("Timed out %s for %s", member.getAsMention(), duration).queue();
    }

    @JDASlashCommand(
            name = "timeout",
            subcommand = "cancel",
            description = "Cancels the specified users timeout"
    )
    public void cancelMute(GuildSlashEvent event,
                           @AppOption(name = "member") Member member) {
        OffsetDateTime timeoutEnd = member.getTimeOutEnd();
        member.removeTimeout().queue();
        event.replyFormat("Removed %s's timeout (%s remaining)", member.getAsMention(), DurationUtils.getTimeDifference(timeoutEnd)).queue();
    }
}
