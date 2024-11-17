package net.hypr.doki;

import com.freya02.botcommands.api.CommandsBuilder;
import com.freya02.botcommands.api.Logging;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import org.slf4j.Logger;

import java.io.IOException;

public class Doki {
    private static final Logger log = Logging.getLogger();
    private static JDA jda;
    private static Config config;

    private Doki(JDA jda, Config config) {
        Doki.jda = jda;
        Doki.config = config;
    }

    public static JDA getJDA() {
        return jda;
    }

    public static void start() throws IOException, InterruptedException {
        config = Config.readConfig();

        final JDA jda = JDABuilder.createLight(config.getToken())
                .setActivity(Activity.customStatus("Banned from everywhere"))
                .build()
                .awaitReady();

        // Print some information about the bot
        log.info("Bot connected as {}", jda.getSelfUser().getAsTag());
        log.info("The bot is present in the following guilds:");
        for (Guild guild : jda.getGuildCache()) {
            log.info("\t- {} ({})", guild.getName(), guild.getId());
        }

        new Doki(jda, config);
    }

    public static void main(String[] args) {
        try {
            start();
            jda = getJDA();
            CommandsBuilder.newBuilder(437970062922612737L)
                    .textCommandBuilder(textCommandsBuilder -> textCommandsBuilder.disableHelpCommand(true))
                    .build(jda, "net.hypr.doki.commands"); //Registering listeners is taken care of by the lib
        } catch (Exception e) {
            log.error("Failed to start the bot", e);
            System.exit(-1);
        }
    }
}
