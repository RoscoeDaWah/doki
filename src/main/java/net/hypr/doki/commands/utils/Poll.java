package net.hypr.doki.commands.utils;

import com.freya02.botcommands.api.annotations.BotPermissions;
import com.freya02.botcommands.api.annotations.CommandMarker;
import com.freya02.botcommands.api.annotations.Optional;
import com.freya02.botcommands.api.annotations.UserPermissions;
import com.freya02.botcommands.api.application.ApplicationCommand;
import com.freya02.botcommands.api.application.annotations.AppOption;
import com.freya02.botcommands.api.application.slash.GuildSlashEvent;
import com.freya02.botcommands.api.application.slash.annotations.JDASlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.utils.messages.MessagePollBuilder;
import net.dv8tion.jda.api.utils.messages.MessagePollData;
import net.hypr.doki.utils.DurationUtils;

import java.time.Duration;
import java.util.Objects;

@CommandMarker
@BotPermissions(Permission.MESSAGE_SEND_POLLS)
@UserPermissions(Permission.MESSAGE_SEND_POLLS)
public class Poll extends ApplicationCommand {
    @JDASlashCommand(
            name = "poll",
            description = "Creates a poll"
    )
    public void poll(GuildSlashEvent event,
                     @AppOption(name = "title") String pollTitle,
                     @AppOption(name = "duration", description = "ex: 2h, must be between 1h and 7d") String duration,
                     @AppOption(name = "options", description = "Comma-seperated poll options") String options,
                     @Optional @AppOption(name = "multiple-choice", description = "Allow multiple choices? (defaults to false)") Boolean multiChoice) {
        Boolean pollMultiChoice = Objects.requireNonNullElse(multiChoice, false);
        Duration pollDuration = DurationUtils.parseDuration(duration);
        if (!DurationUtils.isDurationBetween(pollDuration, Duration.ofHours(1), Duration.ofDays(7))) {
            event.replyFormat("Invalid duration %s!, must be between 1h and 7d", duration).queue();
            return;
        }
        String[] pollOptions = options.split(",");
        MessagePollBuilder poll = MessagePollData.builder(pollTitle)
                .setDuration(pollDuration);
        for (String option: pollOptions) {
            poll.addAnswer(option);
        }
        event.getChannel().sendMessagePoll(poll.build()).queue();
        event.replyFormat("Created %s poll \"%s\" in %s which will last %s",
                (pollMultiChoice) ? "multi-choice" : "single-choice",
                pollTitle,
                event.getChannel().getAsMention(),
                duration).setEphemeral(true).queue();

    }
}
