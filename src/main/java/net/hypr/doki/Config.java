package net.hypr.doki;

import com.google.gson.Gson;

import java.io.*;
import java.nio.charset.StandardCharsets;

//You can add more fields in this class, if your input json matches the structure
//You will need a valid config.json in the resources folder for this to work
public class Config {
    @SuppressWarnings("unused") private String token;
    @SuppressWarnings("unused") private String prefix;
    @SuppressWarnings("unused") private DBConfig mariadb;

    /**
     * Returns the configuration object for this bot
     *
     * @return The config
     * @throws IOException if the config JSON could not be read
     */
    public static Config readConfig() throws IOException {
        try (InputStream in=Thread.currentThread().getContextClassLoader().getResourceAsStream("config.json")) {
            assert in != null;
            Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
            return new Gson().fromJson(reader, Config.class);
        } catch (IOException | NullPointerException e) {
            throw new IOException("config.json was not found, did you forget to create it?", e);
        }
    }

    public String getToken() {
        return token;
    }
    public String getPrefix() { return prefix; }

    public DBConfig getDbConfig() {
        return mariadb;
    }

    public static class DBConfig {
        @SuppressWarnings("unused") private String host, user, password, database;
        @SuppressWarnings("unused") private int portNumber;

        public String getHost() { return host; }

        public String getUser() {
            return user;
        }

        public String getPassword() {
            return password;
        }

        public String getDatabase() { return database; }
        public int getPortNumber() {
            return portNumber;
        }
    }
}