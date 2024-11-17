package net.hypr.doki.commands.utils;

import com.freya02.botcommands.api.annotations.CommandMarker;
import com.freya02.botcommands.api.application.ApplicationCommand;
import com.freya02.botcommands.api.application.CommandScope;
import com.freya02.botcommands.api.application.slash.GlobalSlashEvent;
import com.freya02.botcommands.api.application.slash.GuildSlashEvent;
import com.freya02.botcommands.api.application.slash.annotations.JDASlashCommand;
import com.freya02.botcommands.api.prefixed.annotations.Category;
import com.freya02.botcommands.api.prefixed.annotations.Description;

@CommandMarker
@Category("Utils")
@Description("Pong!")
public class Ping extends ApplicationCommand {
    @JDASlashCommand(
            name = "ping",
            description = "Pong!"
    )
    public void onPing(GuildSlashEvent event) {
        event.deferReply().queue();

        final long gatewayPing = event.getJDA().getGatewayPing();
        event.getJDA().getRestPing()
                .queue(l -> event.getHook()
                        .sendMessageFormat("Gateway ping: **%d ms**\nRest ping: **%d ms**", gatewayPing, l)
                        .queue());
    }
}
