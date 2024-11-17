package net.hypr.doki.commands.utils;

import com.freya02.botcommands.api.annotations.CommandMarker;
import com.freya02.botcommands.api.annotations.Optional;
import com.freya02.botcommands.api.application.ApplicationCommand;
import com.freya02.botcommands.api.application.annotations.AppOption;
import com.freya02.botcommands.api.application.slash.GuildSlashEvent;
import com.freya02.botcommands.api.application.slash.annotations.JDASlashCommand;
import com.freya02.botcommands.api.prefixed.annotations.Category;
import com.freya02.botcommands.api.prefixed.annotations.Description;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.stream.Collectors;

@CommandMarker
@Category("Utils")
@Description("Get information about a user")
public class WhoIs extends ApplicationCommand {
    @JDASlashCommand(
            name = "whois",
            description = "Gets information on a user"
    )
    public void whois(GuildSlashEvent event,
                      @Optional @AppOption(name = "member") Member memberParam) {
        Member member;
        member = Objects.requireNonNullElseGet(memberParam, event::getMember);
        EmbedBuilder whoIsEmbed = buildUserEmbed(member, event);
        event.replyEmbeds(whoIsEmbed.build()).queue();
    }

    private EmbedBuilder buildUserEmbed(Member user, GuildSlashEvent event) {
         return new EmbedBuilder()
                 .setAuthor(user.getUser().getName(), null, user.getEffectiveAvatarUrl())
                 .setThumbnail(user.getEffectiveAvatarUrl())
                 .addField("Information",
                         "**Mention:** " + user.getAsMention() +
                         "\n**ID:** " + user.getId(), true)
                 .addField("Joined",
                         "**Discord:** " +
                                 Duration.between(user.getTimeCreated(), OffsetDateTime.now()).toDays() + " days ago\n" +
                         "**->** <t:" + user.getTimeCreated().toEpochSecond() + ":f>\n**Guild:** " +
                         Duration.between(user.getTimeJoined(), OffsetDateTime.now()).toDays() + " days ago\n" +
                         "**->** <t:" + user.getTimeJoined().toEpochSecond() + ":f>", true)
                 .addField("Guild-Specific",
                         "**Nickname:** " + user.getNickname() + "\n**Roles (" + ((long) user.getRoles().size() + 1) + "):** `@everyone`, " +
                                 user.getRoles().stream().map(Role::getAsMention)
                                         .collect(Collectors.joining(", ")) +
                         "\n**Owner:**" + (user.isOwner() ? "Yes" : "No"), false)
                 .addField("Guild", event.getGuild().getName(), false)
                 .setColor(user.getColor());
    }
}
