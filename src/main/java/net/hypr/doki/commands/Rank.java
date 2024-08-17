package net.hypr.doki.commands;

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
import net.hypr.doki.utils.DBUtils;
import net.hypr.doki.utils.UserRecord;

import java.sql.SQLException;

@CommandMarker
@Category("Levelling")
@Description("Get a user's rank")
public class Rank extends TextCommand {
    @JDATextCommand(name = "rank", order = 1)
    public void execute(BaseCommandEvent event, @TextOption Member member) throws SQLException {
        UserRecord userRecord = DBUtils.getUserRecord(member.getIdLong(), event.getGuild().getIdLong());
        EmbedBuilder rankEmbed = buildRankEmbed(member, userRecord);
        event.reply(rankEmbed.build()).queue();
    }

    @JDATextCommand(name = "rank", order = 2)
    public void execute(CommandEvent event) throws SQLException {
        UserRecord userRecord = DBUtils.getUserRecord(event.getMember().getIdLong(), event.getGuild().getIdLong());
        EmbedBuilder whoIsEmbed = buildRankEmbed(event.getMember(), userRecord);
        event.reply(whoIsEmbed.build()).queue();
    }

    private EmbedBuilder buildRankEmbed(Member user, UserRecord userRecord) {
        int xpToNextLevel = (((userRecord.level + 1) * 5) * 40) - userRecord.xp;
        return new EmbedBuilder()
                .setAuthor(user.getUser().getName(), null, user.getEffectiveAvatarUrl())
                .setThumbnail(user.getEffectiveAvatarUrl())
                .addField("Level", String.valueOf(userRecord.level), true)
                .addField("XP", String.valueOf(userRecord.xp), true)
                .addField("XP Until level " + (userRecord.level + 1), String.valueOf(xpToNextLevel), true)
                .setColor(user.getColor());
    }
}
