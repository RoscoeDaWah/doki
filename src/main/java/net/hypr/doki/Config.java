package net.hypr.doki;

import com.freya02.botcommands.api.Logging;
import com.google.gson.Gson;
import org.slf4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;

//You can add more fields in this class, if your input json matches the structure
//You will need a valid config.json in the resources folder for this to work
public class Config {
    @SuppressWarnings("unused") private String token;
    @SuppressWarnings("unused") private long warningChannel;

    /**
     * Returns the configuration object for this bot
     *
     * @return The config
     * @throws IOException if the config JSON could not be read
     */
    public static Config readConfig() throws IOException {
        Logger log = Logging.getLogger();
        try (InputStream in = new FileInputStream("./config.json")) {
            Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
            log.info("Loaded config");
            return new Gson().fromJson(reader, Config.class);
        } catch (IOException | NullPointerException e) {
            log.error("Failed to load config.json, does the file exist?");
            throw new IOException(e);
        }
    }

    public String getToken() {
        return token;
    }
    public long getWarningChannel() {
        return warningChannel;
    }
}