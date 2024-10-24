package net.hypr.doki.commands.moderation;

import com.freya02.botcommands.api.annotations.CommandMarker;
import com.freya02.botcommands.api.prefixed.BaseCommandEvent;
import com.freya02.botcommands.api.prefixed.CommandEvent;
import com.freya02.botcommands.api.prefixed.TextCommand;
import com.freya02.botcommands.api.prefixed.annotations.Category;
import com.freya02.botcommands.api.prefixed.annotations.Description;
import com.freya02.botcommands.api.prefixed.annotations.JDATextCommand;
import com.freya02.botcommands.api.prefixed.annotations.TextOption;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.hypr.doki.utils.DurationUtils;

import java.time.Duration;

@CommandMarker
@Category("Moderation")
@Description("Mutes a user for a specified amount of time")
public class Mute extends TextCommand {
    @JDATextCommand(name = "mute", order = 1)
    public void execute(BaseCommandEvent event, @TextOption(example = "<@437970062922612737>") Member member, @TextOption(example = "30m") String duration) {
        Member commandExecutor = event.getMember();
        if (!commandExecutor.hasPermission(Permission.KICK_MEMBERS)) {
            return;
        }
        Duration timeoutDuration;
        try {
             timeoutDuration = DurationUtils.parseDuration(duration);
        } catch (IllegalArgumentException ex) {
            event.reply("Invalid duration format!").queue();
            return;
        }
        if (Math.abs(timeoutDuration.toDays()) > 28) {
            event.reply("Duration must be less than 28 days!").queue();
            return;
        }
        member.timeoutFor(timeoutDuration).queue();
        event.reply("Muted " + member.getAsMention() + " (" + member.getEffectiveName() + ") for " + duration).queue();
    }

    @JDATextCommand(name = "mute", order = 2)
    public void execute(CommandEvent event) {
        event.reply("You must specify a user and duration!").queue();
    }
}
