package net.hypr.doki.commands.moderation;

import com.freya02.botcommands.api.annotations.BotPermissions;
import com.freya02.botcommands.api.annotations.CommandMarker;
import com.freya02.botcommands.api.annotations.UserPermissions;
import com.freya02.botcommands.api.application.ApplicationCommand;
import com.freya02.botcommands.api.application.annotations.AppOption;
import com.freya02.botcommands.api.application.slash.GuildSlashEvent;
import com.freya02.botcommands.api.application.slash.annotations.JDASlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

@CommandMarker
@BotPermissions(Permission.MODERATE_MEMBERS)
@UserPermissions(Permission.MODERATE_MEMBERS)
public class Warn extends ApplicationCommand {
    @JDASlashCommand(
            name = "warn",
            description = "Warns a member"
    )
    public void warn(GuildSlashEvent event,
                     @AppOption(name = "member", description = "The member to warn") Member member,
                     @AppOption(name = "reason", description = "The reason for warning them") String reason) {
        EmbedBuilder warnEmbed = new EmbedBuilder()
                .setTitle(String.format("You were warned in %s", event.getGuild().getName()))
                .setDescription(String.format("**Reason:** %s", reason));
        member.getUser().openPrivateChannel().queue((dm) -> dm.sendMessageEmbeds(warnEmbed.build()).queue(
                success -> event.replyFormat("Warned %s for %s", member.getAsMention(), reason).setEphemeral(true).queue(),
                // User has blocked bot or disabled DMs
                error -> event.replyFormat("Warned %s for %s\n-# (Unable to DM user)", member.getAsMention(), reason).queue()
        ));
    }
}