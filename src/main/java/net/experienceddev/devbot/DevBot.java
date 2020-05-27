package net.experienceddev.devbot;

import br.net.fabiozumbi12.UltimateChat.Sponge.UChat;
import com.google.inject.Inject;
import net.experienceddev.devbot.events.PlayerEvents;
import net.experienceddev.devbot.events.SpongeChatEvents;
import net.experienceddev.devbot.events.UltimateChatEvents;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppedEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.channel.MessageChannel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Plugin(
        id = "devbot",
        name = "DevBot",
        description = "Discord bridge",
        authors = {
                "ExperiencedDev"
        }
)
public class DevBot {
    public static DevBot instance;

    @Inject
    private Logger logger;
    @Inject
    Game game;
    @Inject @DefaultConfig(sharedRoot = false)
    Path path;
    @Inject @DefaultConfig(sharedRoot = false)
    ConfigurationLoader<CommentedConfigurationNode> loader;

    public static MessageChannel publicChat = Sponge.getServer().getBroadcastChannel();

    public static Config cfg;
    public static DevMqtt devMqttClient;

    @Listener
    public void preInit(GamePreInitializationEvent e){
        logger.info("Starting up");
        loadConfig();
        instance = this;
    }

    @Listener
    public void onInit(GameInitializationEvent event){
        registerEvents();
        try {
            devMqttClient = new DevMqtt(logger);
        }catch (Exception e){
            logger.error("Somethings fucky! " + e.getMessage());
            e.printStackTrace();
        }

        logger.info("Plugin started");
        if(devMqttClient != null){
            //devMqttClient.publishMessage("test", "I'm online now!");
        }else{
            logger.warn("client is null");
        }
    }

    @Listener
    public void onGameStarted(GameStartedServerEvent e){
        DevMessageObject msgObj = new DevMessageObject();
        msgObj.setType(DevMessageObject.TYPE.STATUS_ONLINE);
        msgObj.setTopic(DevBot.cfg.mqttTopic);
        devMqttClient.publishMessage(msgObj);
    }

    @Listener
    public void onGameStopping(GameStoppedEvent e){
        DevMessageObject msgObj = new DevMessageObject();
        msgObj.setType(DevMessageObject.TYPE.STATUS_OFFLINE);
        msgObj.setTopic(DevBot.cfg.mqttTopic);
        devMqttClient.publishMessage(msgObj);
    }

    @Listener
    public void reload(GameReloadEvent e) {
        loadConfig();
        devMqttClient = new DevMqtt(logger);
    }

    private void registerEvents(){
        Sponge.getEventManager().registerListeners(this, new PlayerEvents());

        if (Sponge.getPluginManager().getPlugin("ultimatechat").isPresent()) {
            // register uchat events
            Sponge.getEventManager().registerListeners(this, new UltimateChatEvents());
            logger.info("UChat found. Registering corresponding events");
        }
        else {
            // register vanilla events
            Sponge.getEventManager().registerListeners(this, new SpongeChatEvents());
            logger.info("UChat not found. Registering sponge events");
        }
    }

    public boolean loadConfig() {
        cfg = null;
        logger.info("Loading config...");
        if (!Files.exists(path)){
            logger.info("Config does not exist! Creating default config");
            try {
                game.getAssetManager().getAsset(this, "default.conf").get().copyToFile(path);// Load Default config
            } catch (IOException e) {
                logger.error("IOException while copying skyejoin config");
                e.printStackTrace();
                return false;
            }
            logger.info("Default config loaded!");
        }
        try {
            cfg = loader.load().getValue(Config.type);
        } catch (ObjectMappingException | IOException e) {
            logger.error(e.getMessage() + " while loading config to memory!");
            e.printStackTrace();
            return false;
        }
        logger.info("Config loaded!");
        return true;
    }
}
