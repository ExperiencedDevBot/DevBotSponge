package net.experienceddev.devbot;

import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.slf4j.Logger;
import org.spongepowered.api.Game;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@ConfigSerializable
public class Config {
    public final static TypeToken<Config> type = TypeToken.of(Config.class);

    @Setting public String mqttBrokerIP = "";
    @Setting public int mqttBrokerPort = 1883;
    @Setting public String mqttUser = "";
    @Setting public String mqttPass = "";
    @Setting public String mqttTopic = "";
    @Setting public String discordChannelID = "";
    @Setting public Map<String, String> chatChannelDiscordChannel = ImmutableMap.of();
    @Setting public String discordChatFormat = "";

}
