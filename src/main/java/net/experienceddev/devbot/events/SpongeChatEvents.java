package net.experienceddev.devbot.events;

import net.experienceddev.devbot.DevBot;
import net.experienceddev.devbot.DevMessageObject;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.message.MessageChannelEvent;

public class SpongeChatEvents {

    @Listener
    public void onPlayerChat(MessageChannelEvent.Chat e, @Root Player player){
        DevMessageObject msgObj = new DevMessageObject();
        msgObj.setDiscordUUID(null);
        msgObj.setMinecraftUUID(player.getUniqueId());
        msgObj.setMinecraftName(player.getName());
        msgObj.setType(DevMessageObject.TYPE.CHAT);
        msgObj.setTopic(DevBot.cfg.mqttTopic);
        msgObj.setMsg(e.getMessage().toPlain()); // TODO: set only the message text. not the player name pls
        DevBot.devMqttClient.publishMessage(msgObj);
    }
}
