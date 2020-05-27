package net.experienceddev.devbot.events;

import com.google.gson.Gson;
import net.experienceddev.devbot.DevBot;
import net.experienceddev.devbot.DevMessageObject;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.network.ClientConnectionEvent;

public class PlayerEvents {

    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join e, @Root Player player){
        DevMessageObject msgObj = new DevMessageObject();
        msgObj.setDiscordUUID(null);
        msgObj.setMinecraftUUID(player.getUniqueId());
        msgObj.setMinecraftName(player.getName());
        msgObj.setType(DevMessageObject.TYPE.CONNECTED);
        msgObj.setTopic(DevBot.cfg.mqttTopic);
        msgObj.setMsg(null);
        DevBot.devMqttClient.publishMessage(msgObj);
    }

    @Listener
    public void onPlayerLeave(ClientConnectionEvent.Disconnect e, @Root Player player){
        DevMessageObject msgObj = new DevMessageObject();
        msgObj.setDiscordUUID(null);
        msgObj.setMinecraftUUID(player.getUniqueId());
        msgObj.setMinecraftName(player.getName());
        msgObj.setType(DevMessageObject.TYPE.DISCONNECTED);
        msgObj.setTopic(DevBot.cfg.mqttTopic);
        msgObj.setMsg(null);
        DevBot.devMqttClient.publishMessage(msgObj);
    }

}
