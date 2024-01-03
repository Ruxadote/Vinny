package com.bot.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Config {

    private static Config config = null;
    private final File configFile;
    private Map<String, String> configs;

    // Token to connect to discord
    public static final String DISCORD_TOKEN = "DISCORD_TOKEN";
    // Token to connect to reddit
    public static final String REDDIT_TOKEN = "REDDIT_TOKEN";
    // Reddit client id
    public static final String REDDIT_CLIENT_ID = "REDDIT_CLIENT_ID";
    // Bot owner id
    public static final String OWNER_ID = "OWNER_ID";
    // Token for the webm command
    public static final String P90_TOKEN = "P90_TOKEN";


    // Deploy with no commands active
    public static final String SILENT_DEPLOY = "SILENT_DEPLOY";

    public static final String DB_URI = "DB_URI";
    public static final String DB_USERNAME = "DB_USERNAME";
    public static final String DB_PASSWORD = "DB_PASSWORD";
    public static final String DB_SCHEMA = "DB_SCHEMA";

    // Default prefix
    public static final String PREFIX = "PREFIX";
    // bool to enable or disable the data loader on boot
    public static final String DATA_LOADER = "DATA_LOADER";

    // Hostname to send to the dd agent
    public static final String DATADOG_HOSTNAME = "DATADOG_HOSTNAME";

    // Max items in the guild prefs cache
    public static final String GUILD_PREFS_CACHE_MAX_ITEMS = "GUILD_PREFS_CACHE_MAX_ITEMS";
    // Max lifetime for a guild to live in the cache
    public static final String GUILD_PREFS_CACHE_OBJECT_LIFETIME = "GUILD_PREFS_OBJECT_LIFETIME";
    // How oftern to run the cleanup process
    public static final String GUILD_PREFS_CACHE_CLEANUP_INTERVAL = "GUILD_PREFS_CACHE_CLEANUP_INTERVAL";

    public static final String MARKOV_CACHE_MAX_ITEMS = "MARKOV_CACHE_MAX_ITEMS";
    public static final String MARKOV_CACHE_OBJECT_LIFETIME = "MARKOV_CACHE_OBJECT_LIFETIME";
    public static final String MARKOV_CACHE_CLEANUP_INTERVAL = "MARKOV_CACHE_CLEANUP_INTERVAL";

    // Configs around posting user counts to external apis
    public static final String ENABLE_EXTERNAL_APIS = "ENABLE_EXTERNAL_APIS";
    public static final String DISCORD_BOT_ID = "BOT_ID";
    public static final String BOTS_FOR_DISCORD_API_TOKEN = "BOTS_FOR_DISCORD_API_TOKEN";
    public static final String DISCORD_BOT_LIST_API_TOKEN = "DISCORD_BOT_LIST_API_TOKEN";
    public static final String DISCORD_BOTS_ORG_API_TOKEN = "DISCORD_BOTS_ORG_API_TOKEN";
    public static final String BOTS_ON_DISCORD_API_TOKEN = "BOTS_ON_DISCORD_API_TOKEN";
    public static final String BOTS_GG_API_TOKEN = "BOTS_GG_API_TOKEN";
    public static final String DISCORD_BOATS_TOKEN = "DISCORD_BOATS_TOKEN";
    public static final String BOTLIST_SPACE_TOKEN = "BOTLIST_SPACE_TOKEN";
    public static final String DIVINE_BOTLIST_TOKEN = "DIVINE_BOTLIST_TOKEN";
    public static final String MYTHICAL_BOTLIST_TOKEN = "MYTHICAL_BOTLIST_TOKEN";
    public static final String EXTREME_BOTLIST_TOKEN = "EXTREME_BOTLIST_TOKEN";

    // Enable logging channeld
    public static final String ENABLE_LOGGING_CHANNELS = "ENABLE_LOGGING_CHANNELS";
    // Webhook urls to post different log levels to
    public static final String ERROR_WEBHOOK = "ERROR_WEBHOOK";
    public static final String WARN_WEBHOOK = "WARN_WEBHOOK";
    public static final String INFO_WEBHOOK = "INFO_WEBHOOK";
    public static final String DEBUG_WEBHOOK = "DEBUG_WEBHOOK";
    public static final String HOST_IDENTIFIER = "HOST_IDENTIFIER";

    public static final String ONLINE_EMOJI = "ONLINE_EMOJI";
    public static final String IDLE_EMOJI = "IDLE_EMOJI";
    public static final String DND_EMOJI = "DND_EMOJI";
    public static final String OFFLINE_EMOJI = "OFFLINE_EMOJI";

    // Enable IP block routing
    public static final String ENABLE_YT_IP_ROUTING = "ENABLE_YT_IP_ROUTING";
    // The IPv4 CIDR block to use
    public static final String IPV4_IP_BLOCK = "IPV4_IP_BLOCK";
    // Excluded addresses from the ip block (comma separated)
    public static final String EXCLUDED_ADDRESSES = "EXCLUDED_IPV4";

    // Enable IP block routing
    public static final String ENABLE_YT_IP_ROUTING_V6 = "ENABLE_YT_IP_ROUTING_V6";
    // The IPv4 CIDR block to use
    public static final String IPV6_IP_BLOCK = "IPV6_IP_BLOCK";

    public static final String PIXIV_USER = "PIXIV_USER";
    public static final String PIXIC_PASS = "PIXIV_PASS";

    // Total shards
    public static final String TOTAL_SHARDS = "TOTAL_SHARDS";
    // 0 indexed shard to start with
    public static final String LOCAL_SHARD_START = "LOCAL_SHARD_START";
    // shard to end with on this process
    public static final String LOCAL_SHARD_END = "LOCAL_SHARD_END";

    public static final String ENABLE_SCHEDULED_COMMANDS = "ENABLE_SCHEDULED_COMMANDS";

    public static final String DEFAULT_SEARCH_PROVIDER = "DEFAULT_SEARCH_PROVIDER";

    public static final String SAUCE_PROXY = "SAUCE_PROXY";
    public static final String SAUCE_TOKEN = "SAUCE_TOKEN";

    public static final String ENABLE_NATS = "ENABLE_NATS";
    public static final String NATS_SERVER = "NATS_SERVER";
    public static final String NATS_TOKEN = "NATS_TOKEN";
    public static final String RSS_SUBJECT = "RSS_SUBJECT";

    public static final String TWITCH_CLIENT_ID = "TWITCH_CLIENT_ID";
    public static final String LAVALINK_ADDRESS = "LL_ADDRESS";
    public static final String LAVALINK_PASSWORD = "LL_PASSWORD";

    private Config() {
        this.configFile = new File("res/config/config.conf");
        configs = new HashMap<>();

        try {
            setConfig();
        }
        catch (FileNotFoundException f) {
            // If config file is gone we can try using env vars
            configs = new HashMap<>(System.getenv());
        }
    }

    public static Config getInstance() {
        if (config == null) {
            config = new Config();
        }
        return config;
    }

    private void setConfig() throws FileNotFoundException {
        Scanner scanner = new Scanner(configFile);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.startsWith("***")){
                String key = line.substring(3, line.length()-3);
                configs.put(key, scanner.nextLine());
            }
        }
    }

    public void setConfig(String key, String value) {
        configs.put(key, value);
    }


    public String getConfig(String key) {
            return configs.get(key);
    }

    public String getConfig(String key, String defaultValue) {
        return configs.getOrDefault(key, defaultValue);
    }
}
