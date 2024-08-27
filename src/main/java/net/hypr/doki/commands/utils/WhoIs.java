package net.hypr.doki.commands.utils;

import com.freya02.botcommands.api.annotations.CommandMarker;
import com.freya02.botcommands.api.prefixed.BaseCommandEvent;
import com.freya02.botcommands.api.prefixed.CommandEvent;
import com.freya02.botcommands.api.prefixed.TextCommand;
import com.freya02.botcommands.api.prefixed.annotations.Category;
import com.freya02.botcommands.api.prefixed.annotations.Description;
import com.freya02.botcommands.api.prefixed.annotations.JDATextCommand;
import com.freya02.botcommands.api.prefixed.annotations.TextOption;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.stream.Collectors;

@CommandMarker
@Category("Utils")
@Description("Get information about a user")
public class WhoIs extends TextCommand {
    @JDATextCommand(name = "whois", order = 1)
    public void execute(BaseCommandEvent event, @TextOption(example = "<@437970062922612737>") Member user) {
        EmbedBuilder whoIsEmbed = buildUserEmbed(user, (CommandEvent) event);
        event.reply(whoIsEmbed.build()).queue();
    }

    @JDATextCommand(name = "whois", order = 2)
    public void execute(BaseCommandEvent event, @TextOption(example = "<@437970062922612737>") User user) {
        EmbedBuilder whoIsEmbed = buildUserEmbed((Member) user, (CommandEvent) event);
        event.reply(whoIsEmbed.build()).queue();
    }

    @JDATextCommand(name = "whois", order = 3)
    public void execute(CommandEvent event) {
        EmbedBuilder whoIsEmbed = buildUserEmbed(event.getMember(), event);
        event.reply(whoIsEmbed.build()).queue();
    }

    private EmbedBuilder buildUserEmbed(Member user, CommandEvent event) {
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
