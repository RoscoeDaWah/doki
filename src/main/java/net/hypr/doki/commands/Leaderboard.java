package net.hypr.doki.commands;

import com.freya02.botcommands.api.annotations.CommandMarker;
import com.freya02.botcommands.api.prefixed.CommandEvent;
import com.freya02.botcommands.api.prefixed.TextCommand;
import com.freya02.botcommands.api.prefixed.annotations.Category;
import com.freya02.botcommands.api.prefixed.annotations.Description;
import com.freya02.botcommands.api.prefixed.annotations.JDATextCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.hypr.doki.utils.DBUtils;
import net.hypr.doki.utils.UserRecord;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;

@CommandMarker
@Category("Levelling")
@Description("Get the global leaderboard")
public class Leaderboard extends TextCommand {
    @JDATextCommand(name = "leaderboard", order = 1, aliases = { "lb" })
    public void execute(CommandEvent event) throws SQLException {
        List<UserRecord> userRecords = DBUtils.getAllUserRecords(event.getGuild().getIdLong());
        // Sort the leaderboard highest level first
        userRecords.sort((o1, o2) -> o2.totalXp - o1.totalXp);
        EmbedBuilder whoIsEmbed = buildLeaderboardEmbed(userRecords, event);
        event.reply(whoIsEmbed.build()).queue();
    }

    private EmbedBuilder buildLeaderboardEmbed(List<UserRecord> userRecords , CommandEvent event) {
        StringBuilder leaderboard = new StringBuilder();
        int idx = 1;
        for (UserRecord record : userRecords) {
            leaderboard
                    .append("**")
                    .append(idx)
                    .append(".** ")
                    .append(record.username)
                    .append(" - ")
                    .append(record.level)
                    .append(" (")
                    .append(record.totalXp)
                    .append(" Total XP)\n");
            idx += 1;
            if (idx > 10) break; // Break out after 10 records is reached
        }
        return new EmbedBuilder()
                .setTitle(event.getGuild().getName() + " Leaderboard")
                .setDescription(leaderboard)
                .setColor(new Color(210,138,39));
    }
}
