package net.hypr.doki;

import com.freya02.botcommands.api.CommandsBuilder;
import com.freya02.botcommands.api.Logging;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;

import java.io.IOException;

public class Doki {
    private static final Logger LOGGER = Logging.getLogger();
    private static JDA jda;
    private static Config config;
    private static String prefix;
    private static final BasicDataSource dataSource = new BasicDataSource();

    private Doki(JDA jda, Config config) {
        Doki.jda = jda;
        Doki.config = config;
    }

    public JDA getJDA() {
        return jda;
    }

    public static Doki start() throws IOException, InterruptedException {
        config = Config.readConfig();

        final JDA jda = JDABuilder.createLight(config.getToken())
                .setActivity(Activity.customStatus("Banned from everywhere"))
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build()
                .awaitReady();

        //Print some information about the bot
        LOGGER.info("Bot connected as {}", jda.getSelfUser().getAsTag());
        LOGGER.info("The bot is present on these guilds :");
        for (Guild guild : jda.getGuildCache()) {
            LOGGER.info("\t- {} ({})", guild.getName(), guild.getId());
        }

        return new Doki(jda, config);
    }

    public static void main(String[] args) {
        try {
            jda = start().getJDA();
            CommandsBuilder.newBuilder(437970062922612737L)
                    .textCommandBuilder(textCommandsBuilder -> textCommandsBuilder.addPrefix("oki!"))
                    .build(jda, "net.hypr.doki.commands"); //Registering listeners is taken care of by the lib
        } catch (Exception e) {
            LOGGER.error("Unable to start the bot", e);
            System.exit(-1);
        }
    }
}
