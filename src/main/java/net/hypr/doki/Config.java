package net.hypr.doki;

import com.google.gson.Gson;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

//You can add more fields in this class, if your input json matches the structure
//You will need a valid config.json in the package com.freya02.bot for this to work
public class Config {
    @SuppressWarnings("unused") private String token;
    @SuppressWarnings("unused") private DBConfig dbConfig;

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
            throw new IOException("""
					config.json was not found in the root folder (of the project), did you forget to put it ?
					Example structure:

					{
						"token": "[your_bot_token_here]"
					}
					""", e);
        }
    }

    public String getToken() {
        return token;
    }

    public DBConfig getDbConfig() {
        return dbConfig;
    }

    public static class DBConfig {
        @SuppressWarnings("unused") private String serverName, user, password, dbName;
        @SuppressWarnings("unused") private int portNumber;

        public String getServerName() {
            return serverName;
        }

        public String getUser() {
            return user;
        }

        public String getPassword() {
            return password;
        }

        public String getDbName() {
            return dbName;
        }

        public int getPortNumber() {
            return portNumber;
        }
    }
}