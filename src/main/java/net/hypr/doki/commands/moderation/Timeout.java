package net.hypr.doki.commands.moderation;

import com.freya02.botcommands.api.annotations.BotPermissions;
import com.freya02.botcommands.api.annotations.CommandMarker;
import com.freya02.botcommands.api.annotations.Optional;
import com.freya02.botcommands.api.annotations.UserPermissions;
import com.freya02.botcommands.api.application.ApplicationCommand;
import com.freya02.botcommands.api.application.annotations.AppOption;
import com.freya02.botcommands.api.application.slash.GuildSlashEvent;
import com.freya02.botcommands.api.application.slash.annotations.JDASlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.hypr.doki.utils.DurationUtils;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Objects;

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
                     @AppOption(name = "duration", description = "ex: 2h5m, must be between 1m and 7d") String duration,
                     @Optional @AppOption(name = "reason") String reason) {
        Duration timeoutDuration = DurationUtils.parseDuration(duration);
        if (!DurationUtils.isDurationBetween(timeoutDuration, Duration.ofMinutes(1), Duration.ofDays(7))) {
            event.replyFormat("Invalid duration %s!, must be between 1m and 7d", duration).queue();
            return;
        }

        try {
            member.timeoutFor(timeoutDuration)
                    .reason(Objects.requireNonNullElse(reason, "No reason provided")).queue();
        } catch (HierarchyException ex) {
            event.replyFormat("Failed to time out %s (member has roles above me!)", member.getAsMention()).setEphemeral(true).queue();
            return;
        }
        event.replyFormat("Timed out %s for %s", member.getAsMention(), duration).queue();
    }

    @JDASlashCommand(
            name = "timeout",
            subcommand = "cancel",
            description = "Cancels the specified users timeout"
    )
    public void cancelTimeout(GuildSlashEvent event,
                           @AppOption(name = "member") Member member,
                           @Optional @AppOption(name = "reason") String reason) {
        OffsetDateTime timeoutEnd = member.getTimeOutEnd();
        try {
            member.removeTimeout()
                    .reason(Objects.requireNonNullElse(reason, "No reason provided")).queue();
        } catch (HierarchyException ex) {
            event.replyFormat("Failed to remove %s's timeout (member has roles above me!)", member.getAsMention()).setEphemeral(true).queue();
            return;
        }
        event.replyFormat("Removed %s's timeout (%s remaining)", member.getAsMention(), DurationUtils.getTimeDifference(timeoutEnd)).queue();
    }

    @JDASlashCommand(
            name = "timeout",
            subcommand = "get",
            description = "Gets the specified users timeout status"
    )
    public void getTimeout(GuildSlashEvent event,
                           @AppOption(name = "member") Member member) {
        if (member.isTimedOut()) {
            OffsetDateTime timeoutEnd = member.getTimeOutEnd();
            event.replyFormat("%s is timed out for another %s", member.getAsMention(), DurationUtils.getTimeDifference(timeoutEnd)).setEphemeral(true).queue();
        } else {
            event.replyFormat("%s isn't timed out!", member.getAsMention()).setEphemeral(true).queue();
        }
    }
}
