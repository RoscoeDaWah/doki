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
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.hypr.doki.Doki;
import org.slf4j.Logger;

import java.awt.*;

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
        Logger log = Doki.getLogger();
        Member actingMod = event.getMember();
        EmbedBuilder warnEmbed = new EmbedBuilder()
                .setTitle(String.format("You were warned in %s", event.getGuild().getName()))
                .setDescription(String.format("**Reason:** %s", reason))
                .setFooter(String.format("Warned by %s (%s)", actingMod.getEffectiveName(), actingMod.getId()), actingMod.getEffectiveAvatarUrl())
                .setColor(new Color(239, 148, 60));
        log.debug(String.format("%s", Doki.getConfig().getWarningChannel()));
        TextChannel warningChannel = Doki.getJDA().getTextChannelById(Doki.getConfig().getWarningChannel());
        member.getUser().openPrivateChannel().queue(
                (dm) -> dm.sendMessageEmbeds(warnEmbed.build()).queue(
                    success -> event.replyFormat("Warned %s for %s", member.getAsMention(), reason).setEphemeral(true).queue(),
                    // User has blocked bot or disabled DMs
                    error -> event.replyFormat("Warned %s for %s\n-# (Unable to DM user)", member.getAsMention(), reason).queue()
                ),
                error -> event.replyFormat("Warned %s for %s\n-# (Unable to DM user)", member.getAsMention(), reason).queue()
        );
        warnEmbed.setTitle(String.format("%s (%s) was warned", member.getEffectiveName(), member.getId()));
        try {
            assert warningChannel != null;
            warningChannel.sendMessageEmbeds(warnEmbed.build()).queue();
        } catch (NullPointerException ex) {
            log.warn("Failed to send message to warning log channel");
        }
    }
}