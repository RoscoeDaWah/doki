package net.hypr.doki;

import com.freya02.botcommands.api.CommandsBuilder;
import com.freya02.botcommands.api.Logging;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.hypr.doki.listeners.LevellingListener;
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
        Doki.prefix = config.getPrefix();
    }

    @SuppressWarnings("InstantiationOfUtilityClass")
    private static void start() throws IOException, InterruptedException {
        config = Config.readConfig();

        final JDA jda = JDABuilder.createLight(config.getToken())
                .setActivity(Activity.customStatus("Banned from everywhere"))
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new LevellingListener())
                .build()
                .awaitReady();

        //Print some information about the bot
        LOGGER.info("Bot connected as {}", jda.getSelfUser().getAsTag());
        LOGGER.info("The bot is present on these guilds :");
        for (Guild guild : jda.getGuildCache()) {
            LOGGER.info("\t- {} ({})", guild.getName(), guild.getId());
        }
        LOGGER.info("Registered prefix: " + config.getPrefix());

        Config.DBConfig dbConfig = config.getDbConfig();
        dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
        dataSource.setUrl("jdbc:mariadb://" + dbConfig.getHost() + "/" + dbConfig.getDatabase());
        dataSource.setUsername(dbConfig.getUser());
        dataSource.setPassword(dbConfig.getPassword());
        dataSource.setMaxTotal(15);
        dataSource.setMaxIdle(5);
        dataSource.setMinIdle(2);
        dataSource.setInitialSize(20);

        new Doki(jda, config);
    }

    public static BasicDataSource getDataSource() { return dataSource; }

    public static JDA getJDA() { return jda; }

    public static String getPrefix() { return prefix; }

    public static void main(String[] args) {
        try {
            start();
            jda = getJDA();
            CommandsBuilder.newBuilder(437970062922612737L)
                    .textCommandBuilder(textCommandsBuilder -> textCommandsBuilder.addPrefix(config.getPrefix()))
                    .build(jda, "net.hypr.doki.commands");
        } catch (Exception e) {
            LOGGER.error("Unable to start the bot", e);
            System.exit(-1);
        }
    }
}
